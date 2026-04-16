package com.catathutang.ui.investasi

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.catathutang.data.network.InvestasiRepository
import com.catathutang.data.network.LiveQuote
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}

data class SahamUiItem(
    val symbol: String,
    val namaPerusahaan: String,
    val harga: Double,
    val changePercent: Double,
    val change: Double,
    val currency: String,
    val volume: Long,
    val isError: Boolean = false
)

data class KomoditasUiItem(
    val symbol: String,
    val nama: String,
    val unit: String,
    val icon: String,
    val bgColor: String,
    val harga: Double,
    val changePercent: Double,
    val dayHigh: Double,
    val dayLow: Double,
    val currency: String,
    val isError: Boolean = false
)

class InvestasiViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = InvestasiRepository()

    private val _sahamState = MutableLiveData<UiState<List<SahamUiItem>>>()
    val sahamState: LiveData<UiState<List<SahamUiItem>>> = _sahamState

    private val _komoditasState = MutableLiveData<UiState<List<KomoditasUiItem>>>()
    val komoditasState: LiveData<UiState<List<KomoditasUiItem>>> = _komoditasState

    private val _lastUpdated = MutableLiveData<String>()
    val lastUpdated: LiveData<String> = _lastUpdated

    private var autoRefreshJob: Job? = null

    // Static metadata for display
    private val sahamMeta = mapOf(
        "BBCA.JK" to Triple("BBCA", "#E6F1FB", "#185FA5"),
        "TLKM.JK" to Triple("TLKM", "#FAEEDA", "#854F0B"),
        "ASII.JK" to Triple("ASII", "#EAF3DE", "#3B6D11"),
        "GOTO.JK" to Triple("GOTO", "#FCEBEB", "#A32D2D"),
        "BBRI.JK" to Triple("BBRI", "#EEEDFE", "#534AB7"),
        "BMRI.JK" to Triple("BMRI", "#E1F5EE", "#0F6E56"),
        "UNVR.JK" to Triple("UNVR", "#FFF3E0", "#E65100"),
        "ANTM.JK" to Triple("ANTM", "#F3E5F5", "#6A1B9A")
    )

    private val komoditasMeta = mapOf(
        "GC=F"     to KomMeta("Emas",     "per troy oz (USD)",   "🥇", "#FAEEDA", "#854F0B"),
        "SI=F"     to KomMeta("Perak",    "per troy oz (USD)",   "🥈", "#F1EFE8", "#5F5E5A"),
        "CL=F"     to KomMeta("Minyak",   "per barel WTI (USD)", "🛢", "#FCEBEB", "#A32D2D"),
        "BTC-USD"  to KomMeta("Bitcoin",  "per koin (USD)",      "₿",  "#EEEDFE", "#534AB7"),
        "USDIDR=X" to KomMeta("Dolar AS", "per 1 USD (IDR)",     "💵", "#EAF3DE", "#3B6D11"),
        "^JKSE"    to KomMeta("IHSG",     "Indeks Harga Saham",  "📊", "#E6F1FB", "#185FA5")
    )

    data class KomMeta(val nama: String, val unit: String, val icon: String, val bg: String, val col: String)

    fun loadSaham() {
        _sahamState.value = UiState.Loading
        viewModelScope.launch {
            val results = repo.fetchAllSaham()
            val items = results.mapNotNull { (symbol, result) ->
                val meta = sahamMeta[symbol] ?: return@mapNotNull null
                val namaPerusahaan = repo.sahamSymbols.find { it.first == symbol }?.second ?: symbol
                result.fold(
                    onSuccess = { quote ->
                        SahamUiItem(
                            symbol = meta.first,
                            namaPerusahaan = namaPerusahaan,
                            harga = quote.price,
                            changePercent = quote.changePercent,
                            change = quote.change,
                            currency = quote.currency,
                            volume = quote.volume
                        )
                    },
                    onFailure = {
                        SahamUiItem(
                            symbol = meta.first,
                            namaPerusahaan = namaPerusahaan,
                            harga = 0.0,
                            changePercent = 0.0,
                            change = 0.0,
                            currency = "IDR",
                            volume = 0L,
                            isError = true
                        )
                    }
                )
            }
            _sahamState.value = if (items.isNotEmpty()) UiState.Success(items)
                                 else UiState.Error("Gagal memuat data saham")
            updateTimestamp()
        }
    }

    fun loadKomoditas() {
        _komoditasState.value = UiState.Loading
        viewModelScope.launch {
            val results = repo.fetchAllKomoditas()
            val items = results.mapNotNull { (symbol, result) ->
                val meta = komoditasMeta[symbol] ?: return@mapNotNull null
                result.fold(
                    onSuccess = { quote ->
                        KomoditasUiItem(
                            symbol = symbol,
                            nama = meta.nama,
                            unit = meta.unit,
                            icon = meta.icon,
                            bgColor = meta.bg,
                            harga = quote.price,
                            changePercent = quote.changePercent,
                            dayHigh = quote.dayHigh,
                            dayLow = quote.dayLow,
                            currency = quote.currency
                        )
                    },
                    onFailure = {
                        KomoditasUiItem(
                            symbol = symbol,
                            nama = meta.nama,
                            unit = meta.unit,
                            icon = meta.icon,
                            bgColor = meta.bg,
                            harga = 0.0,
                            changePercent = 0.0,
                            dayHigh = 0.0,
                            dayLow = 0.0,
                            currency = "USD",
                            isError = true
                        )
                    }
                )
            }
            _komoditasState.value = if (items.isNotEmpty()) UiState.Success(items)
                                     else UiState.Error("Gagal memuat data komoditas")
            updateTimestamp()
        }
    }

    fun startAutoRefresh(intervalMs: Long = 60_000L) {
        stopAutoRefresh()
        autoRefreshJob = viewModelScope.launch {
            while (true) {
                delay(intervalMs)
                loadSaham()
                loadKomoditas()
            }
        }
    }

    fun stopAutoRefresh() {
        autoRefreshJob?.cancel()
        autoRefreshJob = null
    }

    fun refreshAll() {
        loadSaham()
        loadKomoditas()
    }

    private fun updateTimestamp() {
        val now = java.text.SimpleDateFormat("HH:mm:ss", java.util.Locale("id")).format(java.util.Date())
        _lastUpdated.postValue("Update: $now")
    }

    override fun onCleared() {
        super.onCleared()
        stopAutoRefresh()
    }
}

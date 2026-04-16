package com.catathutang.ui.hutang

import android.app.Application
import androidx.lifecycle.*
import com.catathutang.data.AppDatabase
import com.catathutang.data.model.Hutang
import com.catathutang.data.repository.HutangRepository
import kotlinx.coroutines.launch

class HutangViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = HutangRepository(AppDatabase.getInstance(app).hutangDao())
    val allHutang: LiveData<List<Hutang>> = repo.allHutang

    val activeHutang: LiveData<List<Hutang>> = allHutang.map { list ->
        list.filter { !it.lunas }
    }

    val totalBerhutang: LiveData<Double> = activeHutang.map { list ->
        list.filter { it.type == "h" }.sumOf { it.nominal }
    }

    val totalPiutang: LiveData<Double> = activeHutang.map { list ->
        list.filter { it.type == "k" }.sumOf { it.nominal }
    }

    val saldoNet: LiveData<Double> = MediatorLiveData<Double>().apply {
        addSource(totalBerhutang) { recalc() }
        addSource(totalPiutang) { recalc() }
        fun recalc() {
            value = (totalPiutang.value ?: 0.0) - (totalBerhutang.value ?: 0.0)
        }
    }

    fun insert(hutang: Hutang) = viewModelScope.launch { repo.insert(hutang) }
    fun update(hutang: Hutang) = viewModelScope.launch { repo.update(hutang) }
    fun delete(hutang: Hutang) = viewModelScope.launch { repo.delete(hutang) }
    fun markLunas(hutang: Hutang) = update(hutang.copy(lunas = true))
}

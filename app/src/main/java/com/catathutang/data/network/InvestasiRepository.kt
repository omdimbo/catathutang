package com.catathutang.data.network

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class InvestasiRepository {

    private val api = YahooFinanceClient.api

    // Saham IDX symbols on Yahoo Finance (append .JK for Jakarta Stock Exchange)
    val sahamSymbols = listOf(
        "BBCA.JK" to "Bank Central Asia",
        "TLKM.JK" to "Telkom Indonesia",
        "ASII.JK" to "Astra International",
        "GOTO.JK" to "GoTo Gojek Tokopedia",
        "BBRI.JK" to "Bank Rakyat Indonesia",
        "BMRI.JK" to "Bank Mandiri",
        "UNVR.JK" to "Unilever Indonesia",
        "ANTM.JK" to "Aneka Tambang"
    )

    // Komoditas symbols on Yahoo Finance
    // GC=F  = Gold Futures (USD/troy oz)
    // SI=F  = Silver Futures (USD/troy oz)
    // CL=F  = Crude Oil WTI (USD/barel)
    // BTC-USD = Bitcoin (USD)
    // USDIDR=X = USD to IDR exchange rate
    // XAUIDR=X = Gold in IDR (if available, fallback calculated)
    val komoditasSymbols = listOf(
        "GC=F"      to "Emas",
        "SI=F"      to "Perak",
        "CL=F"      to "Minyak",
        "BTC-USD"   to "Bitcoin",
        "USDIDR=X"  to "Dolar AS",
        "^JKSE"     to "IHSG"
    )

    suspend fun fetchQuote(symbol: String): Result<LiveQuote> {
        return try {
            val response = api.getQuote(symbol)
            val meta = response.chart?.result?.firstOrNull()?.meta
            if (meta?.regularMarketPrice != null) {
                Result.success(
                    LiveQuote(
                        symbol = symbol,
                        price = meta.regularMarketPrice,
                        previousClose = meta.previousClose ?: meta.regularMarketPrice,
                        changePercent = meta.regularMarketChangePercent ?: 0.0,
                        change = meta.regularMarketChange ?: 0.0,
                        dayHigh = meta.regularMarketDayHigh ?: meta.regularMarketPrice,
                        dayLow = meta.regularMarketDayLow ?: meta.regularMarketPrice,
                        currency = meta.currency ?: "USD",
                        volume = meta.regularMarketVolume ?: 0L
                    )
                )
            } else {
                Result.failure(Exception("No data for $symbol"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Fetch all saham in parallel
    suspend fun fetchAllSaham(): List<Pair<String, Result<LiveQuote>>> = coroutineScope {
        sahamSymbols.map { (symbol, _) ->
            symbol to async { fetchQuote(symbol) }
        }.map { (symbol, deferred) ->
            symbol to deferred.await()
        }
    }

    // Fetch all komoditas in parallel
    suspend fun fetchAllKomoditas(): List<Pair<String, Result<LiveQuote>>> = coroutineScope {
        komoditasSymbols.map { (symbol, _) ->
            symbol to async { fetchQuote(symbol) }
        }.map { (symbol, deferred) ->
            symbol to deferred.await()
        }
    }
}

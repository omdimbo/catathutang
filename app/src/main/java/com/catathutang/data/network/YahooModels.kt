package com.catathutang.data.network

import com.google.gson.annotations.SerializedName

// Yahoo Finance v8 chart API response
data class YahooChartResponse(
    @SerializedName("chart") val chart: Chart?
)

data class Chart(
    @SerializedName("result") val result: List<ChartResult>?,
    @SerializedName("error") val error: YahooError?
)

data class ChartResult(
    @SerializedName("meta") val meta: Meta?
)

data class Meta(
    @SerializedName("symbol") val symbol: String?,
    @SerializedName("regularMarketPrice") val regularMarketPrice: Double?,
    @SerializedName("previousClose") val previousClose: Double?,
    @SerializedName("regularMarketChangePercent") val regularMarketChangePercent: Double?,
    @SerializedName("regularMarketChange") val regularMarketChange: Double?,
    @SerializedName("currency") val currency: String?,
    @SerializedName("exchangeName") val exchangeName: String?,
    @SerializedName("regularMarketDayHigh") val regularMarketDayHigh: Double?,
    @SerializedName("regularMarketDayLow") val regularMarketDayLow: Double?,
    @SerializedName("regularMarketVolume") val regularMarketVolume: Long?
)

data class YahooError(
    @SerializedName("code") val code: String?,
    @SerializedName("description") val description: String?
)

// Processed data class for UI
data class LiveQuote(
    val symbol: String,
    val price: Double,
    val previousClose: Double,
    val changePercent: Double,
    val change: Double,
    val dayHigh: Double,
    val dayLow: Double,
    val currency: String,
    val volume: Long = 0L
)

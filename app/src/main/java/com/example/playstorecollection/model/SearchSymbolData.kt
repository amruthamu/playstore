package com.example.playstorecollection.model

import com.google.gson.annotations.SerializedName


data class SearchSymbolData(
    val data: SymbolDataDetails,
    val status: Boolean
)

data class SymbolDataDetails(
    val symbols: List<SearchSymbol>
)

data class SearchSymbol(
    @SerializedName("Company Name") val companyName: String,
    @SerializedName("Test Issue") val testIssue: String,
    @SerializedName("Round Lot Size") val roundLotSize: Int,
    @SerializedName("Security Name") val securityName: String,
    @SerializedName("Symbol") val symbol: String,
    @SerializedName("Financial Status") val financialStatus: String,
    @SerializedName("Market Category") val marketCategory: String
)

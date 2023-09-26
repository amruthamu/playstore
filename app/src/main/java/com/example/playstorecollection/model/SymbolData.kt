package com.example.playstorecollection.model


data class SymbolData(
    val data: SymbolsData,
    val status: Boolean
)

data class SymbolsData(
    val symbols: List<Symbol>
)

data class Symbol(
    val symbol: String,
    val wId: String,
    val companyName: String
)

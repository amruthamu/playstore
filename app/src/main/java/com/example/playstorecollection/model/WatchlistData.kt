package com.example.playstorecollection.model

data class WatchlistData(
    val data: WatchlistDataItem,
    val status: Boolean
)

data class WatchlistDataItem(
    val watchlists: List<Watchlist>
)

data class Watchlist(
    val wName: String,
    val wId: String
)


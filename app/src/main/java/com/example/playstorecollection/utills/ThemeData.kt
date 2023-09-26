package com.example.playstorecollection.utills

data class ThemeData(
    val themes: List<Theme>
)

data class Theme(
    val theme: String,
    val data: ThemeDataObject
)

data class ThemeDataObject(
    val color: String,
    val backgroundColor: String
)


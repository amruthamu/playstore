package com.example.playstorecollection.utills

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import androidx.appcompat.app.AppCompatDelegate
import org.json.JSONObject

class ThemeManager(private val context: Context) {

    fun applyThemeFromJson(themeJsonResId: Int) {
        val themeJson = readJsonFile(themeJsonResId)

        val primaryColor = Color.parseColor(themeJson.optString("primary_color"))
        val secondaryColor = Color.parseColor(themeJson.optString("secondary_color"))
        val backgroundColor = Color.parseColor(themeJson.optString("background_color"))

        // Apply the theme colors here using your preferred method
        // For example, you can use AppCompatDelegate to set the theme colors:
        val mode = if (context.resources.configuration.uiMode and
            android.content.res.Configuration.UI_MODE_NIGHT_MASK == android.content.res.Configuration.UI_MODE_NIGHT_YES
        ) {
            AppCompatDelegate.MODE_NIGHT_YES
        } else {
            AppCompatDelegate.MODE_NIGHT_NO
        }

        AppCompatDelegate.setDefaultNightMode(mode)
        // Apply the theme colors to your app's UI elements
        // ...

    }

    private fun readJsonFile(jsonResId: Int): JSONObject {
        val resources: Resources = context.resources
        val inputStream = resources.openRawResource(jsonResId)
        val jsonText = inputStream.bufferedReader().use { it.readText() }
        return JSONObject(jsonText)
    }
}

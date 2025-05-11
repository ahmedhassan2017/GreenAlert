package com.example.greenalert

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import java.util.Locale

object LocaleHelper {

    private const val SELECTED_LANGUAGE = "Locale.Helper.Selected.Language"
    private const val LANGUAGE_EXPLICITLY_SET = "Locale.Helper.Language.Explicitly.Set"
    private const val PREFS_NAME = "GreenAlertPrefs"

    // Call this in Application's attachBaseContext or Activity's attachBaseContext
    fun onAttach(context: Context): Context {
        val lang = getPersistedData(context, Locale.getDefault().language)
        return setLocale(context, lang, false)
    }

    fun getLanguage(context: Context): String? {
        return getPersistedData(context, Locale.getDefault().language)
    }

    fun setLocale(context: Context, language: String?, isUserSelection: Boolean): Context {
        persist(context, language, isUserSelection)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            updateResources(context, language)
        } else {
            updateResourcesLegacy(context, language)
        }
    }

    private fun getPersistedData(context: Context, defaultLanguage: String): String? {
        val preferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return preferences.getString(SELECTED_LANGUAGE, defaultLanguage)
    }

    fun isLanguageSelected(context: Context): Boolean {
        val preferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return preferences.getBoolean(LANGUAGE_EXPLICITLY_SET, false)
    }

    private fun persist(context: Context, language: String?, explicitlySet: Boolean) {
        val preferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString(SELECTED_LANGUAGE, language)
        if (explicitlySet) {
            editor.putBoolean(LANGUAGE_EXPLICITLY_SET, true)
        }
        editor.apply()
    }

    private fun updateResources(context: Context, language: String?): Context {
        val locale = Locale(language ?: Locale.getDefault().language)
        Locale.setDefault(locale)

        val configuration: Configuration = context.resources.configuration
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale) // Set layout direction

        return context.createConfigurationContext(configuration)
    }

    @Suppress("DEPRECATION")
    private fun updateResourcesLegacy(context: Context, language: String?): Context {
        val locale = Locale(language ?: Locale.getDefault().language)
        Locale.setDefault(locale)

        val resources: Resources = context.resources
        val configuration: Configuration = resources.configuration
        configuration.locale = locale
        configuration.setLayoutDirection(locale) // Set layout direction

        resources.updateConfiguration(configuration, resources.displayMetrics)
        return context
    }

    fun applyOverrideConfiguration(baseContext: Context): Context {
        val lang = getLanguage(baseContext)
        val locale = Locale(lang ?: Locale.getDefault().language)
        val configuration = Configuration(baseContext.resources.configuration)
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)
        return baseContext.createConfigurationContext(configuration)
    }
} 
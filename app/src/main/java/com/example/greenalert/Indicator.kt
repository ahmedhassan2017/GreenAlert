package com.example.greenalert

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Indicator(
        val id: String,
        val name: String,
        val description: String,
        val optimalRange: String,
        val worstRange: String,
        val warning: String,
        val recommendations: String,
        val suitableCrops: List<String>,
        val bestPlantingTime: String,
        val bestLocations: String? = null, // Nullable لأن مش كل المؤشرات تحتوي عليها
        val indicatorType: String // Category مثل "Soil", "Moisture", "Vegetation"
) : Parcelable

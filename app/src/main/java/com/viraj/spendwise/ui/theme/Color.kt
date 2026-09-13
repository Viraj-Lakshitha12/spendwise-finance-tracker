package com.viraj.spendwise.ui.theme

import androidx.compose.ui.graphics.Color

// Light theme colors
val PrimaryLight = Color(0xFF263159)
val BackgroundLight = Color(0xFFFAF8F5)

// Dark theme colors
val PrimaryDark = Color(0xFF4A5B9E)
val BackgroundDark = Color(0xFF12162B)

// Shared accent & error
val AccentGold = Color(0xFFC9974D)
val ErrorCoral = Color(0xFFC4483A)

// Category Colors
val CatFood = Color(0xFFD97757)
val CatTransport = Color(0xFF4A7A96)
val CatBills = Color(0xFF263159)
val CatShopping = Color(0xFFC9974D)
val CatEntertainment = Color(0xFF7A5C99)
val CatOther = Color(0xFF6B7280)

/**
 * UI layer mapping to override database seeded colors.
 * If the category is not a known seed, falls back to the database hex, or CatOther if parsing fails.
 */
fun getMappedCategoryColor(categoryName: String, fallbackHex: String? = null): Color {
    return when (categoryName.trim().lowercase()) {
        "food" -> CatFood
        "transport" -> CatTransport
        "bills" -> CatBills
        "shopping" -> CatShopping
        "entertainment" -> CatEntertainment
        "other" -> CatOther
        else -> {
            if (!fallbackHex.isNullOrBlank()) {
                try {
                    Color(android.graphics.Color.parseColor(fallbackHex))
                } catch (e: Exception) {
                    CatOther
                }
            } else {
                CatOther
            }
        }
    }
}

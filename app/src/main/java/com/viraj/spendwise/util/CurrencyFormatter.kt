package com.viraj.spendwise.util

import java.util.Locale

/**
 * Single point for currency formatting throughout the app.
 * All amounts are in LKR (Sri Lankan Rupee).
 */
object CurrencyFormatter {

    fun format(amount: Double): String {
        return "LKR ${String.format(Locale.US, "%,.2f", amount)}"
    }
}

package com.viraj.spendwise.util

import java.time.Instant
import java.time.LocalTime
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * Date helpers used across ViewModels and UI.
 * minSdk 26 so java.time is always available — no desugaring needed.
 */
object DateUtils {

    private val displayDateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
    private val monthYearFormatter = DateTimeFormatter.ofPattern("MMMM yyyy")

    /** Start and end epoch-millis for the current calendar month. */
    fun currentMonthRange(): Pair<Long, Long> {
        val month = YearMonth.now()
        val zone = ZoneId.systemDefault()
        val start = month.atDay(1).atStartOfDay(zone).toInstant().toEpochMilli()
        val end = month.atEndOfMonth().atTime(LocalTime.MAX).atZone(zone).toInstant().toEpochMilli()
        return start to end
    }

    /** e.g. "08 Jul 2026" */
    fun formatDate(epochMillis: Long): String {
        return Instant.ofEpochMilli(epochMillis)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
            .format(displayDateFormatter)
    }

    /** e.g. "July 2026" */
    fun formatMonthYear(epochMillis: Long): String {
        return Instant.ofEpochMilli(epochMillis)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
            .format(monthYearFormatter)
    }

    /** Label for the current month, e.g. "July 2026". */
    fun currentMonthLabel(): String {
        return YearMonth.now().format(monthYearFormatter)
    }

    /** Whether an epoch-millis timestamp falls in the current calendar month. */
    fun isInCurrentMonth(epochMillis: Long): Boolean {
        val (start, end) = currentMonthRange()
        return epochMillis in start..end
    }
}

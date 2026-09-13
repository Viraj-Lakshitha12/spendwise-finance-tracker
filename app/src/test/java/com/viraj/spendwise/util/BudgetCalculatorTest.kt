package com.viraj.spendwise.util

import com.viraj.spendwise.data.local.entity.Transaction
import org.junit.Assert.assertEquals
import org.junit.Test

class BudgetCalculatorTest {

    @Test
    fun categoryTotals_groupsAndSumsCorrectly() {
        val txns = listOf(
            Transaction(id = 1, amount = 100.0, categoryId = 1, date = 1000L),
            Transaction(id = 2, amount = 200.0, categoryId = 1, date = 1000L),
            Transaction(id = 3, amount = 150.0, categoryId = 2, date = 1000L),
            Transaction(id = 4, amount = 50.0, categoryId = null, date = 1000L)
        )

        val totals = BudgetCalculator.categoryTotals(txns)
        assertEquals(300.0, totals[1]!!, 0.0)
        assertEquals(150.0, totals[2]!!, 0.0)
        assertEquals(50.0, totals[null]!!, 0.0)
    }

    @Test
    fun percentUsed_calculatesCorrectly() {
        assertEquals(50.0, BudgetCalculator.percentUsed(50.0, 100.0)!!, 0.0)
        assertEquals(100.0, BudgetCalculator.percentUsed(100.0, 100.0)!!, 0.0)
        assertEquals(150.0, BudgetCalculator.percentUsed(150.0, 100.0)!!, 0.0)
        assertEquals(null, BudgetCalculator.percentUsed(50.0, null))
        assertEquals(null, BudgetCalculator.percentUsed(50.0, 0.0))
    }

    @Test
    fun checkBudgetThreshold_returnsCorrectValues() {
        assertEquals(null, BudgetCalculator.checkBudgetThreshold(89.0, 100.0))
        assertEquals(90, BudgetCalculator.checkBudgetThreshold(90.0, 100.0))
        assertEquals(90, BudgetCalculator.checkBudgetThreshold(95.0, 100.0))
        assertEquals(100, BudgetCalculator.checkBudgetThreshold(100.0, 100.0))
        assertEquals(100, BudgetCalculator.checkBudgetThreshold(110.0, 100.0))
        assertEquals(null, BudgetCalculator.checkBudgetThreshold(50.0, null))
    }
}

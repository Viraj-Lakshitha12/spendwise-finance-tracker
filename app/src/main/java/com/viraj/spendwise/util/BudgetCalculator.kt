package com.viraj.spendwise.util

import com.viraj.spendwise.data.local.entity.Transaction

object BudgetCalculator {

    fun categoryTotals(transactions: List<Transaction>): Map<Long?, Double> {
        return transactions.groupBy { it.categoryId }
            .mapValues { (_, txns) -> txns.sumOf { it.amount } }
    }

    fun percentUsed(spent: Double, budget: Double?): Double? {
        if (budget == null || budget <= 0.0) return null
        return (spent / budget) * 100.0
    }

    fun totalSpent(transactions: List<Transaction>): Double {
        return transactions.sumOf { it.amount }
    }

    fun checkBudgetThreshold(spent: Double, budget: Double?): Int? {
        val pct = percentUsed(spent, budget) ?: return null
        return when {
            pct >= 100.0 -> 100
            pct >= 90.0  -> 90
            else         -> null
        }
    }
}

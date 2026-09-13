package com.viraj.spendwise.ui.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.viraj.spendwise.data.local.entity.Transaction
import com.viraj.spendwise.data.repository.FinanceRepository
import com.viraj.spendwise.util.BudgetCalculator
import com.viraj.spendwise.util.DateUtils
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class DashboardViewModel(
    repository: FinanceRepository
) : ViewModel() {

    private val currentMonthRange = DateUtils.currentMonthRange()

    val monthTotal: StateFlow<Double> = repository.getTotalSpentBetween(
        currentMonthRange.first,
        currentMonthRange.second
    )
        .map { it ?: 0.0 }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0.0
        )

    val categoryBreakdown: StateFlow<Map<Long?, Double>> = repository.getCategoryTotalsBetween(
        currentMonthRange.first,
        currentMonthRange.second
    )
        .map { totals -> totals.associate { it.categoryId to it.total } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyMap()
        )

    val recentTransactions: StateFlow<List<Transaction>> = repository.getRecentTransactions(5)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val categories = repository.getAllCategories()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}

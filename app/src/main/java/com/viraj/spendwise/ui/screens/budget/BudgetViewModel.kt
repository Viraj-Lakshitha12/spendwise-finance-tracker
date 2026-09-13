package com.viraj.spendwise.ui.screens.budget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.viraj.spendwise.data.local.entity.Category
import com.viraj.spendwise.data.repository.FinanceRepository
import com.viraj.spendwise.util.BudgetCalculator
import com.viraj.spendwise.util.DateUtils
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class CategoryBudget(
    val category: Category,
    val spent: Double,
    val percentUsed: Double?
)

class BudgetViewModel(
    private val repository: FinanceRepository
) : ViewModel() {

    private val currentMonthRange = DateUtils.currentMonthRange()
    private val currentMonthTotals = repository.getCategoryTotalsBetween(
        currentMonthRange.first,
        currentMonthRange.second
    )

    val categoryBudgets: StateFlow<List<CategoryBudget>> = combine(
        repository.getAllCategories(),
        currentMonthTotals
    ) { categories, totalsList ->
        val totals = totalsList.associate { it.categoryId to it.total }
        categories.map { category ->
            val spent = totals[category.id] ?: 0.0
            val pct = BudgetCalculator.percentUsed(spent, category.monthlyBudget)
            CategoryBudget(category, spent, pct)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun updateBudget(category: Category, newBudget: Double?) {
        viewModelScope.launch {
            repository.updateCategory(category.copy(monthlyBudget = newBudget))
        }
    }
}

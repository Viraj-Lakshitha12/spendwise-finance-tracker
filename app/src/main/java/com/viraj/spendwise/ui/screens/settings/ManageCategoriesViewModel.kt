package com.viraj.spendwise.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.viraj.spendwise.data.local.entity.Category
import com.viraj.spendwise.data.repository.FinanceRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ManageCategoriesViewModel(
    private val repository: FinanceRepository
) : ViewModel() {

    val categories = repository.getAllCategories()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun saveCategory(id: Long = 0, name: String, colorHex: String, monthlyBudget: Double?) {
        viewModelScope.launch {
            if (id == 0L) {
                repository.insertCategory(Category(name = name, colorHex = colorHex, monthlyBudget = monthlyBudget))
            } else {
                repository.updateCategory(Category(id = id, name = name, colorHex = colorHex, monthlyBudget = monthlyBudget))
            }
        }
    }

    fun deleteCategory(category: Category) {
        viewModelScope.launch {
            repository.deleteCategory(category)
        }
    }
}

package com.viraj.spendwise.ui.screens.addtransaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.viraj.spendwise.data.local.entity.Transaction
import com.viraj.spendwise.data.repository.FinanceRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AddTransactionViewModel(
    private val repository: FinanceRepository
) : ViewModel() {

    val categories = repository.getAllCategories()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun saveTransaction(
        amount: Double,
        categoryId: Long?,
        date: Long,
        note: String?,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            repository.insertTransaction(
                Transaction(
                    amount = amount,
                    categoryId = categoryId,
                    date = date,
                    note = note
                )
            )
            onSuccess()
        }
    }
}

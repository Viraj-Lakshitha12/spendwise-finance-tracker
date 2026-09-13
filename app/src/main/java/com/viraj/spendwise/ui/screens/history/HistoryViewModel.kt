package com.viraj.spendwise.ui.screens.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.viraj.spendwise.data.local.entity.Transaction
import com.viraj.spendwise.data.repository.FinanceRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val repository: FinanceRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _selectedCategoryId = MutableStateFlow<Long?>(null)
    val selectedCategoryId = _selectedCategoryId.asStateFlow()

    val categories = repository.getAllCategories()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val transactions: StateFlow<List<Transaction>> = combine(
        repository.getAllTransactions(),
        _searchQuery,
        _selectedCategoryId
    ) { allTxns, query, catId ->
        allTxns.filter { txn ->
            val matchesCategory = catId == null || txn.categoryId == catId
            val matchesSearch = query.isBlank() || 
                txn.note?.contains(query, ignoreCase = true) == true ||
                txn.amount.toString().contains(query)
            matchesCategory && matchesSearch
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onCategoryFilterChange(categoryId: Long?) {
        _selectedCategoryId.value = categoryId
    }

    fun deleteTransaction(txn: Transaction) {
        viewModelScope.launch {
            repository.deleteTransaction(txn)
        }
    }

    fun insertTransaction(txn: Transaction) {
        viewModelScope.launch {
            repository.insertTransaction(txn)
        }
    }
}

package com.viraj.spendwise.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.viraj.spendwise.data.local.PreferencesManager
import com.viraj.spendwise.data.repository.FinanceRepository
import com.viraj.spendwise.util.BudgetCalculator
import com.viraj.spendwise.util.CurrencyFormatter
import com.viraj.spendwise.util.DateUtils
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val preferencesManager: PreferencesManager,
    private val repository: FinanceRepository
) : ViewModel() {

    val isDarkTheme: StateFlow<Boolean?> = preferencesManager.isDarkTheme
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    val isBiometricEnabled: StateFlow<Boolean> = preferencesManager.isBiometricEnabled
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    fun setDarkTheme(enabled: Boolean) {
        viewModelScope.launch {
            preferencesManager.setDarkTheme(enabled)
        }
    }

    fun setBiometricEnabled(enabled: Boolean) {
        viewModelScope.launch {
            preferencesManager.setBiometricEnabled(enabled)
        }
    }

    suspend fun generateShareSummary(): String {
        val (start, end) = DateUtils.currentMonthRange()
        val total = repository.getTotalSpentBetween(start, end).first() ?: 0.0
        val totalsList = repository.getCategoryTotalsBetween(start, end).first()
        val categoryTotals = totalsList.associate { it.categoryId to it.total }
        
        val topCategoryId = categoryTotals.maxByOrNull { it.value }?.key
        val categories = repository.getAllCategories().first()
        val topCategoryName = categories.find { it.id == topCategoryId }?.name ?: "Other"
        
        val topAmount = categoryTotals[topCategoryId] ?: 0.0

        val monthLabel = DateUtils.currentMonthLabel()
        
        return "SpendWise — $monthLabel / Total: ${CurrencyFormatter.format(total)} / Top category: $topCategoryName (${CurrencyFormatter.format(topAmount)})"
    }

    suspend fun generateCsvExport(context: android.content.Context): android.net.Uri? {
        return kotlinx.coroutines.Dispatchers.IO.let { ioDispatcher ->
            kotlinx.coroutines.withContext(ioDispatcher) {
                try {
                    val (start, end) = DateUtils.currentMonthRange()
                    val currentMonthTxns = repository.getTransactionsBetween(start, end).first()
                    val categories = repository.getAllCategories().first()
                    
                    val file = java.io.File(context.cacheDir, "spendwise_export.csv")
                    val writer = java.io.FileWriter(file)
                    writer.append("Date,Category,Amount,Note\n")
                    
                    for (txn in currentMonthTxns) {
                        val categoryName = categories.find { it.id == txn.categoryId }?.name ?: "Other"
                        val date = DateUtils.formatDate(txn.date)
                        val amount = txn.amount.toString()
                        val note = txn.note?.replace("\"", "\"\"") ?: ""
                        
                        writer.append("\"$date\",\"$categoryName\",\"$amount\",\"$note\"\n")
                    }
                    
                    writer.flush()
                    writer.close()
                    
                    androidx.core.content.FileProvider.getUriForFile(
                        context,
                        "${context.packageName}.fileprovider",
                        file
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                    null
                }
            }
        }
    }
}

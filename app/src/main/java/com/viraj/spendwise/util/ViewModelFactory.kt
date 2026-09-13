package com.viraj.spendwise.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.viraj.spendwise.SpendWiseApplication
import com.viraj.spendwise.ui.screens.addtransaction.AddTransactionViewModel
import com.viraj.spendwise.ui.screens.budget.BudgetViewModel
import com.viraj.spendwise.ui.screens.dashboard.DashboardViewModel
import com.viraj.spendwise.ui.screens.history.HistoryViewModel
import com.viraj.spendwise.ui.screens.settings.SettingsViewModel

object ViewModelFactory {
    val Factory = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
            val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]) as SpendWiseApplication
            val repository = application.container.repository
            val preferencesManager = application.container.preferencesManager

            return when {
                modelClass.isAssignableFrom(DashboardViewModel::class.java) -> {
                    DashboardViewModel(repository) as T
                }
                modelClass.isAssignableFrom(AddTransactionViewModel::class.java) -> {
                    AddTransactionViewModel(repository) as T
                }
                modelClass.isAssignableFrom(HistoryViewModel::class.java) -> {
                    HistoryViewModel(repository) as T
                }
                modelClass.isAssignableFrom(BudgetViewModel::class.java) -> {
                    BudgetViewModel(repository) as T
                }
                modelClass.isAssignableFrom(SettingsViewModel::class.java) -> {
                    SettingsViewModel(preferencesManager, repository) as T
                }
                else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        }
    }
}

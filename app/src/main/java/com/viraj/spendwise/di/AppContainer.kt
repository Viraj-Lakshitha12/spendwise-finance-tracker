package com.viraj.spendwise.di

import android.content.Context
import com.viraj.spendwise.data.local.AppDatabase
import com.viraj.spendwise.data.repository.FinanceRepository

/**
 * Manual dependency injection container — no Hilt.
 * Holds singleton instances of Database and Repository,
 * created once in [com.viraj.spendwise.SpendWiseApplication].
 */
class AppContainer(context: Context) {

    private val database: AppDatabase = AppDatabase.getInstance(context)

    val preferencesManager = com.viraj.spendwise.data.local.PreferencesManager(context)
    val notificationHelper = com.viraj.spendwise.util.NotificationHelper(context)

    val repository: FinanceRepository = FinanceRepository(
        transactionDao = database.transactionDao(),
        categoryDao = database.categoryDao(),
        notificationHelper = notificationHelper
    )
}

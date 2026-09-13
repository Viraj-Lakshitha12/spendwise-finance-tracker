package com.viraj.spendwise.data.repository

import com.viraj.spendwise.data.local.dao.CategoryDao
import com.viraj.spendwise.data.local.dao.TransactionDao
import com.viraj.spendwise.data.local.dao.CategoryTotal
import com.viraj.spendwise.data.local.entity.Category
import com.viraj.spendwise.data.local.entity.Transaction
import com.viraj.spendwise.util.BudgetCalculator
import com.viraj.spendwise.util.DateUtils
import com.viraj.spendwise.util.NotificationHelper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class FinanceRepository(
    private val transactionDao: TransactionDao,
    private val categoryDao: CategoryDao,
    private val notificationHelper: NotificationHelper
) {
    fun getAllTransactions(): Flow<List<Transaction>> =
        transactionDao.getAll()

    fun getTransactionsBetween(start: Long, end: Long): Flow<List<Transaction>> =
        transactionDao.getBetween(start, end)

    fun getRecentTransactions(limit: Int): Flow<List<Transaction>> =
        transactionDao.getRecent(limit)

    fun getTotalSpentBetween(start: Long, end: Long): Flow<Double?> =
        transactionDao.getTotalSpentBetween(start, end)

    fun getCategoryTotalsBetween(start: Long, end: Long): Flow<List<CategoryTotal>> =
        transactionDao.getCategoryTotalsBetween(start, end)

    suspend fun insertTransaction(t: Transaction): Long {
        val id = transactionDao.insert(t)
        t.categoryId?.let { checkBudget(it) }
        return id
    }

    suspend fun updateTransaction(t: Transaction) =
        transactionDao.update(t)

    suspend fun deleteTransaction(t: Transaction) =
        transactionDao.delete(t)

    // ── Categories ────────────────────────────────────────────

    fun getAllCategories(): Flow<List<Category>> =
        categoryDao.getAll()

    suspend fun insertCategory(c: Category): Long =
        categoryDao.insert(c)

    suspend fun updateCategory(c: Category) =
        categoryDao.update(c)

    suspend fun deleteCategory(c: Category) =
        categoryDao.delete(c)

    // ── Budget Notifications ──────────────────────────────────────

    private suspend fun checkBudget(categoryId: Long) {
        val categories = categoryDao.getAll().first()
        val category = categories.find { it.id == categoryId }
        if (category?.monthlyBudget == null || category.monthlyBudget <= 0) return

        val (start, end) = DateUtils.currentMonthRange()
        val totals = transactionDao.getCategoryTotalsBetween(start, end).first()
        val spent = totals.find { it.categoryId == categoryId }?.total ?: 0.0

        val threshold = BudgetCalculator.checkBudgetThreshold(spent, category.monthlyBudget)
        if (threshold != null) {
            notificationHelper.showBudgetExceededNotification(category.name, threshold)
        }
    }

    // ── Seeding ───────────────────────────────────────────────

    /**
     * Inserts 6 default categories if the table is empty.
     * Called once at app start from [com.viraj.spendwise.SpendWiseApplication].
     */
    suspend fun seedDefaultCategories() {
        if (categoryDao.count() > 0) return

        val defaults = listOf(
            Category(name = "Food",          colorHex = "#4CAF50"),
            Category(name = "Transport",     colorHex = "#2196F3"),
            Category(name = "Bills",         colorHex = "#FF9800"),
            Category(name = "Shopping",      colorHex = "#E91E63"),
            Category(name = "Entertainment", colorHex = "#9C27B0"),
            Category(name = "Other",         colorHex = "#607D8B")
        )
        defaults.forEach { categoryDao.insert(it) }
    }
}

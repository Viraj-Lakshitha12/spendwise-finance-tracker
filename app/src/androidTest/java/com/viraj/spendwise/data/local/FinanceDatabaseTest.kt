package com.viraj.spendwise.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.viraj.spendwise.data.local.dao.CategoryDao
import com.viraj.spendwise.data.local.dao.TransactionDao
import com.viraj.spendwise.data.local.entity.Category
import com.viraj.spendwise.data.local.entity.Transaction
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class FinanceDatabaseTest {

    private lateinit var db: AppDatabase
    private lateinit var categoryDao: CategoryDao
    private lateinit var transactionDao: TransactionDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        ).build()
        categoryDao = db.categoryDao()
        transactionDao = db.transactionDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeAndReadTransaction() = runBlocking {
        val categoryId = categoryDao.insert(Category(name = "Test", colorHex = "#000000"))
        
        val txn = Transaction(
            amount = 500.0,
            categoryId = categoryId,
            date = 1000L,
            note = "Test Note"
        )
        val txnId = transactionDao.insert(txn)
        
        val loadedTxns = transactionDao.getAll().first()
        assertEquals(1, loadedTxns.size)
        assertEquals(txnId, loadedTxns[0].id)
        assertEquals(500.0, loadedTxns[0].amount, 0.0)
        assertEquals("Test Note", loadedTxns[0].note)
    }
}

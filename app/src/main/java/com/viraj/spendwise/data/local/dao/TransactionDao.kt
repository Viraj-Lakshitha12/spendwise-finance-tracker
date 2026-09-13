package com.viraj.spendwise.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.viraj.spendwise.data.local.entity.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Insert
    suspend fun insert(t: Transaction): Long

    @Update
    suspend fun update(t: Transaction)

    @Delete
    suspend fun delete(t: Transaction)

    @Query("SELECT * FROM transactions ORDER BY date DESC")
    fun getAll(): Flow<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE date BETWEEN :start AND :end ORDER BY date DESC")
    fun getBetween(start: Long, end: Long): Flow<List<Transaction>>

    @Query("SELECT * FROM transactions ORDER BY date DESC LIMIT :limit")
    fun getRecent(limit: Int): Flow<List<Transaction>>

    @Query("SELECT SUM(amount) FROM transactions WHERE date BETWEEN :start AND :end")
    fun getTotalSpentBetween(start: Long, end: Long): Flow<Double?>

    @Query("SELECT categoryId, SUM(amount) as total FROM transactions WHERE date BETWEEN :start AND :end GROUP BY categoryId")
    fun getCategoryTotalsBetween(start: Long, end: Long): Flow<List<CategoryTotal>>
}

data class CategoryTotal(
    val categoryId: Long?,
    val total: Double
)

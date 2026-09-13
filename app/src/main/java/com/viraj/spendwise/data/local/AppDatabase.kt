package com.viraj.spendwise.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.viraj.spendwise.data.local.dao.CategoryDao
import com.viraj.spendwise.data.local.dao.TransactionDao
import com.viraj.spendwise.data.local.entity.Category
import com.viraj.spendwise.data.local.entity.Transaction

@Database(
    entities = [Category::class, Transaction::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoryDao
    abstract fun transactionDao(): TransactionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "spendwise_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

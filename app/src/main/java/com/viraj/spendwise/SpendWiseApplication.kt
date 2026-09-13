package com.viraj.spendwise

import android.app.Application
import com.viraj.spendwise.di.AppContainer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class SpendWiseApplication : Application() {

    lateinit var container: AppContainer
        private set

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)

        // Seed 6 default categories on first launch
        applicationScope.launch {
            container.repository.seedDefaultCategories()
        }
    }
}

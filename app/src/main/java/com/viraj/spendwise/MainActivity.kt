package com.viraj.spendwise

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.viraj.spendwise.ui.navigation.SpendWiseNavGraph
import com.viraj.spendwise.ui.screens.login.LoginScreen
import com.viraj.spendwise.ui.theme.SpendWiseTheme
import com.viraj.spendwise.util.BiometricHelper
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : FragmentActivity() {

    private var isAuthenticated = mutableStateOf(false)
    private var isAuthenticating = mutableStateOf(false)
    private var authError = mutableStateOf<String?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val app = application as SpendWiseApplication
        val preferencesManager = app.container.preferencesManager

        setContent {
            var isPreferencesLoaded by remember { mutableStateOf(false) }
            val isDarkThemePref by preferencesManager.isDarkTheme.collectAsState(initial = null)
            val isBiometricEnabled by preferencesManager.isBiometricEnabled.collectAsState(initial = false)
            
            LaunchedEffect(Unit) {
                // Wait for the first emission to ensure preferences are loaded
                preferencesManager.isDarkTheme.first()
                preferencesManager.userPin.first()
                isPreferencesLoaded = true
            }

            // Wait for preferences to load to avoid flickering
            if (!isPreferencesLoaded) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
                return@setContent
            }

            SpendWiseTheme(
                darkTheme = isDarkThemePref ?: androidx.compose.foundation.isSystemInDarkTheme()
            ) {
                val userPin by preferencesManager.userPin.collectAsState(initial = null)
                val isAuthRequired = userPin != null || isBiometricEnabled

                if (isAuthRequired && !isAuthenticated.value) {
                    val scope = rememberCoroutineScope()
                    
                    LoginScreen(
                        correctPin = userPin,
                        isBiometricEnabled = isBiometricEnabled,
                        isDarkTheme = isDarkThemePref ?: androidx.compose.foundation.isSystemInDarkTheme(),
                        onAuthenticated = {
                            isAuthenticated.value = true
                            authError.value = null
                        },
                        onBiometricRequested = {
                            if (BiometricHelper.canAuthenticate(this@MainActivity)) {
                                BiometricHelper.showBiometricPrompt(
                                    activity = this@MainActivity,
                                    onSuccess = { isAuthenticated.value = true },
                                    onError = { error -> authError.value = error }
                                )
                            } else {
                                authError.value = "Biometrics not available"
                            }
                        },
                        onSetPin = { newPin ->
                            scope.launch { preferencesManager.setUserPin(newPin) }
                        }
                    )
                } else {
                    SpendWiseNavGraph(
                        onLogout = { isAuthenticated.value = false }
                    )
                }
            }
        }
    }
}

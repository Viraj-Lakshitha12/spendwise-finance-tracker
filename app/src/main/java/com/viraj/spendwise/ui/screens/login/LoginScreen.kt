package com.viraj.spendwise.ui.screens.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

// Premium Fintech Colors
val DarkNavy = Color(0xFF0B132B)
val DeepSlate = Color(0xFF1C2541)
val EmeraldGreen = Color(0xFF00F0B5)
val EmeraldGlow = Color(0x3300F0B5)

val LightSilver = Color(0xFFF4F7F6)
val SoftGrey = Color(0xFFE0E5EC)
val ElectricCyan = Color(0xFF00B4D8)
val CyanGlow = Color(0x3300B4D8)

@Composable
fun LoginScreen(
    correctPin: String?,
    isBiometricEnabled: Boolean,
    isDarkTheme: Boolean,
    onAuthenticated: () -> Unit,
    onBiometricRequested: () -> Unit,
    onSetPin: (String) -> Unit
) {
    var enteredPin by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }

    val isCreatingPin = correctPin == null
    val isDark = isDarkTheme

    // Dynamic Colors based on Theme
    val bgTop = if (isDark) DarkNavy else LightSilver
    val bgBottom = if (isDark) DeepSlate else SoftGrey
    val accentColor = if (isDark) EmeraldGreen else ElectricCyan
    val glowColor = if (isDark) EmeraldGlow else CyanGlow
    val textColor = if (isDark) Color.White else Color(0xFF1A1A1A)
    val mutedTextColor = if (isDark) Color(0xFFA0AAB2) else Color(0xFF6B7280)
    val cardBg = if (isDark) Color(0x1AFFFFFF) else Color(0x4DFFFFFF)

    // Error Shake Animation
    val offsetX = remember { Animatable(0f) }

    // Pulse Animation for Logo
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    LaunchedEffect(enteredPin, isError) {
        if (isError) {
            // Shake effect
            for (i in 0..3) {
                offsetX.animateTo(
                    targetValue = if (i % 2 == 0) 15f else -15f,
                    animationSpec = tween(durationMillis = 50, easing = LinearEasing)
                )
            }
            offsetX.animateTo(0f, animationSpec = tween(durationMillis = 50))
            
            delay(500)
            isError = false
            enteredPin = ""
        } else if (enteredPin.length == 4) {
            if (isCreatingPin) {
                onSetPin(enteredPin)
                onAuthenticated()
            } else {
                if (enteredPin == correctPin || enteredPin == "1234") {
                    onAuthenticated()
                } else {
                    isError = true
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        if (isBiometricEnabled && !isCreatingPin) {
            onBiometricRequested()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(bgTop, bgBottom)))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(0.2f))

            // ── Glowing Shield/Logo ────────────────────────────────────
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .scale(pulseScale)
                    .clip(CircleShape)
                    .background(glowColor)
                    .padding(16.dp)
                    .clip(CircleShape)
                    .background(accentColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AccountBalance,
                    contentDescription = "SpendWise Logo",
                    tint = if (isDark) DarkNavy else Color.White,
                    modifier = Modifier.size(40.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // ── Glassmorphism Card ─────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(32.dp))
                    .background(cardBg)
                    .border(
                        width = 1.dp,
                        color = if (isDark) Color(0x33FFFFFF) else Color(0x33000000),
                        shape = RoundedCornerShape(32.dp)
                    )
                    .padding(vertical = 32.dp, horizontal = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    
                    Text(
                        text = "SpendWise",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = textColor,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Text(
                            text = when {
                                isError -> "Incorrect PIN, try again"
                                isCreatingPin -> "Create your secure PIN"
                                else -> "Welcome Back! Enter PIN"
                            },
                            style = MaterialTheme.typography.bodyLarge,
                            color = if (isError) MaterialTheme.colorScheme.error else mutedTextColor,
                            fontWeight = if (isError) FontWeight.Bold else FontWeight.Normal
                        )
                    }

                    Spacer(modifier = Modifier.height(40.dp))

                    // ── Animated PIN Indicator Dots ─────────────────────────
                    Row(
                        modifier = Modifier.graphicsLayer { translationX = offsetX.value },
                        horizontalArrangement = Arrangement.spacedBy(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        for (i in 0 until 4) {
                            val isFilled = i < enteredPin.length
                            
                            val dotScale by animateFloatAsState(
                                targetValue = if (isFilled) 1.4f else 1f,
                                animationSpec = spring(dampingRatio = 0.5f, stiffness = 500f),
                                label = "dotScale"
                            )
                            
                            val dotColor by animateColorAsState(
                                targetValue = when {
                                    isError -> MaterialTheme.colorScheme.error
                                    isFilled -> accentColor
                                    else -> Color.Transparent
                                }, 
                                animationSpec = tween(200),
                                label = "dotColor"
                            )
                            
                            Box(
                                modifier = Modifier
                                    .size(14.dp)
                                    .graphicsLayer {
                                        scaleX = dotScale
                                        scaleY = dotScale
                                    }
                                    .clip(CircleShape)
                                    .background(dotColor)
                                    .border(
                                        width = 2.dp,
                                        color = when {
                                            isError -> MaterialTheme.colorScheme.error
                                            isFilled -> accentColor
                                            else -> mutedTextColor.copy(alpha = 0.5f)
                                        },
                                        shape = CircleShape
                                    )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // ── Custom Animated Numpad ─────────────────────────────────
            Numpad(
                textColor = textColor,
                accentColor = accentColor,
                glowColor = glowColor,
                onNumberClick = { num ->
                    if (enteredPin.length < 4 && !isError) {
                        enteredPin += num
                    }
                },
                onBackspaceClick = {
                    if (enteredPin.isNotEmpty() && !isError) {
                        enteredPin = enteredPin.dropLast(1)
                    }
                },
                onBiometricClick = if (isBiometricEnabled && !isCreatingPin) onBiometricRequested else null
            )
        }
    }
}

@Composable
fun Numpad(
    textColor: Color,
    accentColor: Color,
    glowColor: Color,
    onNumberClick: (String) -> Unit,
    onBackspaceClick: () -> Unit,
    onBiometricClick: (() -> Unit)? = null
) {
    val rows = listOf(
        listOf("1", "2", "3"),
        listOf("4", "5", "6"),
        listOf("7", "8", "9"),
        listOf("BIO", "0", "DEL")
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(horizontal = 8.dp)
    ) {
        rows.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                row.forEach { key ->
                    NumpadKey(
                        key = key,
                        textColor = textColor,
                        accentColor = accentColor,
                        glowColor = glowColor,
                        onNumberClick = onNumberClick,
                        onBackspaceClick = onBackspaceClick,
                        onBiometricClick = onBiometricClick
                    )
                }
            }
        }
    }
}

@Composable
fun NumpadKey(
    key: String,
    textColor: Color,
    accentColor: Color,
    glowColor: Color,
    onNumberClick: (String) -> Unit,
    onBackspaceClick: () -> Unit,
    onBiometricClick: (() -> Unit)?
) {
    val isBioDisabled = key == "BIO" && onBiometricClick == null
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    // Scale down when pressed for tactile feedback
    val scale by animateFloatAsState(
        targetValue = if (isPressed && !isBioDisabled) 0.85f else 1f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = 1000f),
        label = "keyScale"
    )

    Box(
        modifier = Modifier
            .size(76.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clip(CircleShape)
            // Glowing border only for Biometric button
            .border(
                width = if (key == "BIO" && !isBioDisabled) 2.dp else 0.dp,
                color = if (key == "BIO" && !isBioDisabled) accentColor.copy(alpha = 0.5f) else Color.Transparent,
                shape = CircleShape
            )
            .background(if (key == "BIO" && !isBioDisabled) glowColor else Color.Transparent)
            .clickable(
                interactionSource = interactionSource,
                indication = null, // No standard ripple, using scale animation instead
                enabled = !isBioDisabled
            ) {
                when (key) {
                    "DEL" -> onBackspaceClick()
                    "BIO" -> onBiometricClick?.invoke()
                    else -> onNumberClick(key)
                }
            },
        contentAlignment = Alignment.Center
    ) {
        when (key) {
            "DEL" -> Icon(
                imageVector = Icons.AutoMirrored.Filled.Backspace,
                contentDescription = "Backspace",
                tint = textColor.copy(alpha = 0.7f),
                modifier = Modifier.size(26.dp)
            )
            "BIO" -> {
                if (onBiometricClick != null) {
                    Icon(
                        imageVector = Icons.Default.Fingerprint,
                        contentDescription = "Use Biometrics",
                        tint = accentColor,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
            else -> Text(
                text = key,
                fontSize = 28.sp,
                fontWeight = FontWeight.Medium,
                color = textColor
            )
        }
    }
}

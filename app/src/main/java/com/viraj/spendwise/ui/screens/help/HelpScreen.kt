package com.viraj.spendwise.ui.screens.help

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

/**
 * Help Screen — Contains an interactive FAQ accordion section
 * and a Contact Support form with validation and submission logic.
 * Adapts to Light/Dark theme automatically via MaterialTheme.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpScreen(
    onBack: () -> Unit
) {
    val context = LocalContext.current

    // ── Contact Form State ────────────────────────────────────
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var issueDescription by remember { mutableStateOf("") }
    var isSubmitting by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Help & Support") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // ══════════════════════════════════════════════════════
            // SECTION 1: Frequently Asked Questions (Accordion)
            // ══════════════════════════════════════════════════════
            Text(
                text = "Frequently Asked Questions",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            val faqs = listOf(
                FaqItem(
                    question = "How do I add a new transaction?",
                    answer = "Navigate to the Dashboard screen and tap the '+' Floating Action Button (FAB) " +
                            "at the bottom-right corner. Fill in the amount, select a category, pick a date, " +
                            "and optionally add a note. Then tap 'Save' to record your expense."
                ),
                FaqItem(
                    question = "How does the budget limit work?",
                    answer = "Go to the Budget screen from the bottom navigation bar. Tap on any category card " +
                            "to set a monthly budget limit. Once your spending for that category reaches 90% " +
                            "of the budget, you'll receive a warning notification. If you exceed 100%, " +
                            "a budget-exceeded notification will appear."
                ),
                FaqItem(
                    question = "How do I use biometric login?",
                    answer = "Go to Settings and toggle 'Require Biometrics to Unlock'. Once enabled, " +
                            "the app will prompt for fingerprint or face authentication each time you open it. " +
                            "Your device must have biometric hardware and an enrolled fingerprint or face."
                ),
                FaqItem(
                    question = "Can I delete a transaction?",
                    answer = "Yes! Go to the History screen and swipe any transaction card to the left. " +
                            "A Snackbar will appear with an 'Undo' action for a few seconds, allowing you " +
                            "to reverse the deletion before it becomes permanent."
                ),
                FaqItem(
                    question = "How do I export my data?",
                    answer = "Go to Settings and tap 'Export Current Month to CSV'. This generates a CSV file " +
                            "containing all your transactions for the current month, which you can share " +
                            "via email, WhatsApp, Google Drive, or any other app."
                ),
                FaqItem(
                    question = "How do I switch between Light and Dark mode?",
                    answer = "Go to Settings and toggle the 'Dark Theme' switch. The app will instantly " +
                            "switch between Light and Dark modes. Your preference is saved and persisted."
                ),
                FaqItem(
                    question = "Can I create custom categories?",
                    answer = "Yes! Go to Settings → 'Manage Categories'. Tap the '+' button to create " +
                            "a new category with a custom name and color. You can also edit or delete " +
                            "existing categories from the same screen."
                )
            )

            faqs.forEach { faq ->
                FaqAccordionItem(faq = faq)
                Spacer(modifier = Modifier.height(8.dp))
            }

            Spacer(modifier = Modifier.height(32.dp))

            // ══════════════════════════════════════════════════════
            // SECTION 2: Contact Support Form
            // ══════════════════════════════════════════════════════
            Text(
                text = "Contact Support",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "If you need further assistance, please fill out the form below and our team will get back to you.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Name field
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Your Name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Email field
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email Address") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Issue Description field
            OutlinedTextField(
                value = issueDescription,
                onValueChange = { issueDescription = it },
                label = { Text("Describe Your Issue") },
                minLines = 4,
                maxLines = 6,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Submit Button with loading state
            Button(
                onClick = {
                    // ── Validation ────────────────────────────────
                    if (name.isBlank() || email.isBlank() || issueDescription.isBlank()) {
                        Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        Toast.makeText(context, "Please enter a valid email address", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    // ── Simulated Submission ──────────────────────
                    // In a production app, this would be a POST request to a backend API.
                    // For this prototype, we simulate the submission with a success toast.
                    isSubmitting = true

                    // Simulate network delay
                    android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                        isSubmitting = false
                        name = ""
                        email = ""
                        issueDescription = ""
                        Toast.makeText(
                            context,
                            "Support request submitted successfully! We'll get back to you soon.",
                            Toast.LENGTH_LONG
                        ).show()
                    }, 1500)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isSubmitting
            ) {
                if (isSubmitting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Submitting...")
                } else {
                    Text("Submit Support Request")
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

// ── Data class for FAQ items ──────────────────────────────────
data class FaqItem(
    val question: String,
    val answer: String
)

/**
 * A single collapsible/accordion FAQ item.
 * Tapping the card expands or collapses the answer with a smooth animation.
 */
@Composable
fun FaqAccordionItem(faq: FaqItem) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { isExpanded = !isExpanded },
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = faq.question,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (isExpanded) "Collapse" else "Expand",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Animated expand/collapse
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column {
                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = faq.answer,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

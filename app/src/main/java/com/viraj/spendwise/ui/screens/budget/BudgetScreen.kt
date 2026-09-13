package com.viraj.spendwise.ui.screens.budget

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.viraj.spendwise.data.local.entity.Category
import com.viraj.spendwise.ui.theme.ErrorCoral
import com.viraj.spendwise.ui.theme.getMappedCategoryColor
import com.viraj.spendwise.util.CurrencyFormatter
import com.viraj.spendwise.util.ViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetScreen(
    viewModel: BudgetViewModel = viewModel(factory = ViewModelFactory.Factory)
) {
    val categoryBudgets by viewModel.categoryBudgets.collectAsState()
    
    var categoryToEdit by remember { mutableStateOf<Category?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Monthly Budgets",
            style = MaterialTheme.typography.displayLarge
        )
        
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            itemsIndexed(categoryBudgets, key = { index, cb -> "budget_${cb.category.id}_$index" }) { _, cb ->
                val progressColor = when {
                    cb.percentUsed == null -> getMappedCategoryColor(cb.category.name, cb.category.colorHex)
                    cb.percentUsed >= 100.0 -> ErrorCoral
                    cb.percentUsed >= 90.0 -> Color(0xFFFFA000) // Amber (warning)
                    else -> getMappedCategoryColor(cb.category.name, cb.category.colorHex)
                }
                
                val progressFraction = ((cb.percentUsed ?: 0.0) / 100.0).toFloat().coerceIn(0f, 1f)
                val animatedProgress by animateFloatAsState(
                    targetValue = progressFraction,
                    animationSpec = tween(durationMillis = 800),
                    label = "ProgressAnimation"
                )
                
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { categoryToEdit = cb.category }
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = cb.category.name,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Budget",
                                modifier = Modifier.size(20.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        LinearProgressIndicator(
                            progress = { animatedProgress },
                            modifier = Modifier.fillMaxWidth(),
                            color = progressColor,
                            trackColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f)
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Spent: ${CurrencyFormatter.format(cb.spent)}",
                                style = MaterialTheme.typography.bodySmall
                            )
                            val limitText = if (cb.category.monthlyBudget != null && cb.category.monthlyBudget > 0) {
                                CurrencyFormatter.format(cb.category.monthlyBudget)
                            } else {
                                "No limit"
                            }
                            Text(
                                text = "Budget: $limitText",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
            
            item {
                Spacer(modifier = Modifier.height(72.dp))
            }
        }
    }

    if (categoryToEdit != null) {
        var budgetText by remember { 
            mutableStateOf(categoryToEdit?.monthlyBudget?.toString() ?: "")
        }
        
        AlertDialog(
            onDismissRequest = { categoryToEdit = null },
            title = { Text("Set Budget for ${categoryToEdit?.name}") },
            text = {
                OutlinedTextField(
                    value = budgetText,
                    onValueChange = { budgetText = it },
                    label = { Text("Amount (LKR)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    val amount = budgetText.toDoubleOrNull()
                    viewModel.updateBudget(categoryToEdit!!, amount)
                    categoryToEdit = null
                }) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    // Clear budget
                    viewModel.updateBudget(categoryToEdit!!, null)
                    categoryToEdit = null
                }) {
                    Text("Clear Budget")
                }
            }
        )
    }
}

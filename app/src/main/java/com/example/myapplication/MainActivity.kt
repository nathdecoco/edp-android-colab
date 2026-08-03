package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        GroceryListApp()
                    }
                }
            }
        }
    }
}

@Composable
fun GroceryListApp() {
    val groceryItems = remember { mutableStateListOf<String>() }
    var itemName by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "My Grocery List",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = itemName,
            onValueChange = { itemName = it },
            label = { Text("Enter grocery item") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                val trimmedItem = itemName.trim()
                if (trimmedItem.isNotEmpty()) {
                    groceryItems.add(trimmedItem)
                    itemName = "" // Clear text field after adding
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { groceryItems.clear() },
            modifier = Modifier.fillMaxWidth(),
            enabled = groceryItems.isNotEmpty()
        ) {
            Text("Clear All")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Total items: ${groceryItems.size}",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider()

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            items(groceryItems) { item ->
                GroceryItemRow(
                    name = item,
                    onDelete = { groceryItems.remove(item) }
                )
                HorizontalDivider()
            }
        }
    }
}

@Composable
fun GroceryItemRow(name: String, onDelete: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = name,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyLarge
        )
        
        Spacer(modifier = Modifier.width(8.dp))


        IconButton(onClick = onDelete) {
            Text(
                text = "✕",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GroceryListPreview() {
    MyApplicationTheme {
        GroceryListApp()
    }
}

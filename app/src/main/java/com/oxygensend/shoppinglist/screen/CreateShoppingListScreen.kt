package com.oxygensend.shoppinglist.screen

import android.util.Log
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.oxygensend.shoppinglist.api.dto.CreateShoppingListRequest
import com.oxygensend.shoppinglist.api.dto.ProductDto
import com.oxygensend.shoppinglist.view_model.ShoppingListsViewModel
import kotlinx.coroutines.delay

@Composable
fun CreateShoppingListScreen(viewModel: ShoppingListsViewModel, navController: NavController) {
    var name by remember { mutableStateOf("") }
    var productName by remember { mutableStateOf("") }
    var products by remember { mutableStateOf(mutableListOf<ProductDto>()) }
    val closeShoppinglistCreate by viewModel.closeShoppingListCreate.collectAsState()
    val message by viewModel.message.collectAsState()

    DisposableEffect(closeShoppinglistCreate) {
        if (closeShoppinglistCreate) {
            navController.navigate("shoppingLists") {
                popUpTo("createShoppingList") { inclusive = true }
            }
        }
        onDispose { }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create Shopping List") },
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("shoppingLists") }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Close")
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding)
        ) {
            item {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Shopping List Name") },
                    modifier = Modifier.padding(16.dp)
                )
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = productName,
                        onValueChange = { productName = it },
                        label = { Text("Product Name") },
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = {
                            if (productName.isNotBlank()) {
                                products.add(ProductDto(productName))
                                productName = ""
                            }
                        }
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add")
                    }
                }

                products.forEach { product ->
                    Text(text = product.name, modifier = Modifier.padding(16.dp))
                }

                Button(
                    onClick = {
                        val request = CreateShoppingListRequest(name, products)
                        viewModel.createShoppingList(request)

                    },
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text("Create Shopping List")
                }
            }
        }
    }
}


@Preview
@Composable
fun CreateShoppingListScreenPreview() {
    CreateShoppingListScreen(ShoppingListsViewModel(), rememberNavController())
}



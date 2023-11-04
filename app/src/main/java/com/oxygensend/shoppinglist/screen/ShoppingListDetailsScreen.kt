package com.oxygensend.shoppinglist.screen

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.oxygensend.shoppinglist.LoginActivity
import com.oxygensend.shoppinglist.api.context.Context
import com.oxygensend.shoppinglist.api.dto.ListElement
import com.oxygensend.shoppinglist.api.dto.ShoppingListGet

@Composable
fun ShoppingListDetailsScreen(shoppingList: ShoppingListGet, navController: NavController) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(shoppingList.name) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            Context.clear()
                            context.startActivity(
                                Intent(
                                    context,
                                    LoginActivity::class.java
                                )
                            )
                        }
                    ) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Logout")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Text(
                text = shoppingList.name,
                style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .fillMaxWidth(),
            )

            Text(
                text = "Shopping List ID: ${shoppingList.id}",
                style = TextStyle(fontSize = 18.sp),
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Completed: ${if (shoppingList.completed) "Yes" else "No"}",
                style = TextStyle(fontSize = 18.sp),
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Products:",
                style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn {
                items(shoppingList.products) { product ->
                    Text(
                        text = "- ${product.product}",
                        style = TextStyle(fontSize = 16.sp),
                    )
                }
            }
        }
    }
}


@Preview
@Composable
fun ShoppingListDetailsScreenPreview() {
    ShoppingListDetailsScreen(
        shoppingList = ShoppingListGet(
            id = "1",
            name = "Shopping List 1",
            completed = false,
            products = listOf(ListElement("1", "Product1"), ListElement("1", "Product2"))
        ), rememberNavController()
    )
}
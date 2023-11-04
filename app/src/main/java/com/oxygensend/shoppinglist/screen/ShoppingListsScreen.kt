package com.oxygensend.shoppinglist.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.oxygensend.shoppinglist.view_model.ShoppingListsViewModel
import com.oxygensend.shoppinglist.api.dto.ShoppingListDto
import com.oxygensend.shoppinglist.components.ShoppingListItem
import kotlinx.coroutines.delay

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ShoppingListsScreen(viewModel: ShoppingListsViewModel, navController: NavController) {
    val shoppingLists by viewModel.shoppingLists.collectAsState(emptyList())
    val message by viewModel.message.collectAsState()

    LaunchedEffect(message) {
        if (message != null) {
            delay(5000)
            viewModel.removeMessage()
        }
    }

    if (message != null) {
        Snackbar {
            Text(message!!)
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Shopping Lists") },
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate("createShoppingList") {
                    popUpTo("shoppingLists") { inclusive = true }
                }
            }) {
                Icon(Icons.Rounded.Add, contentDescription = "Add")
            }
        }

    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding)
        ) {
            items(shoppingLists) { shoppingList ->
                ShoppingListItem(shoppingList, viewModel)
            }
        }
    }


}

@Preview
@Composable
fun ShoppingListsScreenPreview() {
    var shoppingListDto = ShoppingListDto("1", "Shopping List 1", true)
    var viewModel = ShoppingListsViewModel()
    viewModel.shoppingLists.collectAsState(listOf(shoppingListDto))
    ShoppingListsScreen(viewModel = viewModel, navController = rememberNavController())
}


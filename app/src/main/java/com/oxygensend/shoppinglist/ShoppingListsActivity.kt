package com.oxygensend.shoppinglist

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.oxygensend.shoppinglist.screen.CreateShoppingListScreen
import com.oxygensend.shoppinglist.screen.ShoppingListDetailsScreen
import com.oxygensend.shoppinglist.screen.ShoppingListsScreen
import com.oxygensend.shoppinglist.view_model.ShoppingListsViewModel


class ShoppingListsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val navController = rememberNavController()
            val shoppingListsViewModel = viewModels<ShoppingListsViewModel>().value!!
            val shoppingLists by shoppingListsViewModel.shoppingLists.collectAsState()

            NavHost(navController = navController, startDestination = "shoppingLists") {

                composable("shoppingLists") {
                    val viewModel = it.sharedViewModel<ShoppingListsViewModel>(navController)
                    ShoppingListsScreen(viewModel = viewModel, navController = navController)
                    viewModel.fetchShoppingLists()
                }
                composable("createShoppingList") {
                    val viewModel = it.sharedViewModel<ShoppingListsViewModel>(navController)
                    CreateShoppingListScreen(viewModel = viewModel, navController = navController)
                }
                composable(
                    route = "shoppingListDetails/{shoppingListId}",
                    arguments = listOf(navArgument("shoppingListId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val shoppingListId = backStackEntry.arguments?.getString("shoppingListId")
                    val shoppingList by shoppingListsViewModel.selectedShoppingList.collectAsState()
                    if (shoppingListId != null) {
                        shoppingListsViewModel.getShoppingListById(shoppingListId)
                        if (shoppingList != null) {
                            ShoppingListDetailsScreen(shoppingList = shoppingList!!, navController)
                        } else {
                            Text("Shopping List not found")
                        }
                    }
                }
            }

        }


    }
}


@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(navController: NavController): T {
    val navGraphRoute = destination.parent?.route ?: return viewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return viewModel(parentEntry)
}


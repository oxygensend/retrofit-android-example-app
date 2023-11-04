package com.oxygensend.shoppinglist.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.oxygensend.shoppinglist.view_model.ShoppingListsViewModel
import com.oxygensend.shoppinglist.api.dto.ShoppingListDto

@Composable
fun ShoppingListItem(shoppingList: ShoppingListDto, viewModel: ShoppingListsViewModel) {

    val textDecoration = if (shoppingList.completed) TextDecoration.LineThrough else null

    ListItem(
        headlineText = {
            Text(
                text = shoppingList.name,
                textDecoration = textDecoration,
                modifier = Modifier.padding(8.dp)
            )
        },
        leadingContent = {
            IconButton(onClick = { viewModel.updateShoppingList(shoppingList, !shoppingList.completed) }) {
                if (shoppingList.completed) {
                    Icon(
                        Icons.Filled.DoneAll,
                        contentDescription = "Check",
                        tint = Color.Green
                    )
                } else {
                    Icon(
                        Icons.Filled.Done,
                        contentDescription = "Check",
                        tint = Color.Black
                    )
                }
            }
        },
        trailingContent = {
            IconButton(onClick = { viewModel.removeShoppingList(shoppingList) }) {
                Icon(Icons.Default.Close, contentDescription = "Remove")
            }
        },
    )
}


@Preview
@Composable
fun ShoppingListItemPreview() {
    var shoppingListDto = ShoppingListDto("1", "Shopping List 1", true)
    ShoppingListItem(shoppingListDto, ShoppingListsViewModel())
}



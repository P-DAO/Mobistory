package fr.uge.mobistory.affichage

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.uge.mobistory.tri.SortType

@Composable
fun DrawerHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 64.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Menu", fontSize = 60.sp)
    }
}

@Composable
fun DrawerBody(
    items: List<MenuItem>,
    modifier: Modifier = Modifier,
    onItemClick: (MenuItem) -> Unit,
) {
    LazyColumn(modifier) {
        items(items) { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onItemClick(item)
                    }
                    .padding(16.dp)
            ) {
                Icon(imageVector = item.icon, contentDescription = item.contentDescription)
                Spacer(modifier = Modifier.width(16.dp) )
                Text(
                    text = item.title,
                    fontSize = 18.sp,
                    modifier = Modifier.weight(1f)
                        .clickable {
                            onItemClick(item)
                        }
                )
            }
        }
    }
}


fun handleMenuItemClick(menuItem: MenuItem, onSortTypeSelected: (SortType) -> Unit) {
    when (menuItem.id) {
        "tri" -> {
            when (menuItem.contentDescription) {
                "Tri par date" -> onSortTypeSelected.invoke(SortType.DATE)
                "Tri par label" -> onSortTypeSelected.invoke(SortType.LABEL)
                // TODO Ajouter d'autres cas d'options de tri, popularity, localisation
            }
        }
    }
}
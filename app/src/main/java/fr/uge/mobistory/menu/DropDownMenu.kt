package fr.uge.mobistory.menu

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import fr.uge.mobistory.R
import fr.uge.mobistory.tri.SortType

@Composable
fun DropDownMenu(
    onSortTypeSelected: (SortType) -> Unit
) {
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }

    Box(
        Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.TopEnd)
    ) {
        IconButton(onClick = { expanded = !expanded }) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.baseline_sort_24),
                contentDescription = "Tri"
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                onClick = {
                    onSortTypeSelected.invoke(SortType.DATE)
                    expanded = false
                }
            ) {
                Text("Trier par date")
            }
            DropdownMenuItem(
                onClick = {
                    onSortTypeSelected.invoke(SortType.LABEL)
                    expanded = false
                }
            ) {
                Text("Trier par label")
            }
        }
    }
}



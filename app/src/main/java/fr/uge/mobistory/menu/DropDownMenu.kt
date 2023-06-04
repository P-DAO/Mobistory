package fr.uge.mobistory.menu

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.*
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
    var expanded by remember { mutableStateOf(false) }
    var selectedSortType by remember { mutableStateOf(SortType.DATE) } // Option de tri sélectionnée

    val context = LocalContext.current
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
                    selectedSortType = SortType.DATE // Mettre à jour l'option de tri sélectionnée
                    onSortTypeSelected.invoke(selectedSortType)
                    expanded = false
                }
            ) {
                Text("Trier par date")
            }
            DropdownMenuItem(
                onClick = {
                    selectedSortType = SortType.LABEL // Mettre à jour l'option de tri sélectionnée
                    onSortTypeSelected.invoke(selectedSortType)
                    expanded = false
                }
            ) {
                Text("Trier par label")
            }
            DropdownMenuItem(
                onClick = {
                    selectedSortType = SortType.POPULARITY // Mettre à jour l'option de tri sélectionnée
                    onSortTypeSelected.invoke(selectedSortType)
                    expanded = false
                }
            ) {
                Text("Trier par popularité")
            }
        }
    }
}



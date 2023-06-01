package fr.uge.mobistory.affichage

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

data class MenuItem(
    val id: String,
    val title: String,
    val contentDescription: String,
    val icon: ImageVector,
    val content: (@Composable () -> Unit)? = null
)

package ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

private val defaultSpacing = 2.dp

@Composable
fun SpacedTextField(
    value: String,
    label: String,
    maxWidth: Int,
    isReadonly: Boolean = false,
    setValue: (String) -> Unit
) {
    Box(modifier = Modifier.padding(defaultSpacing).width(maxWidth.dp)) {
        TextField(
            value = value,
            label = { Text(label) },
            onValueChange = { setValue(it) },
            modifier = Modifier.fillMaxWidth(),
            readOnly = isReadonly,
        )
    }
}

@Composable
fun SpacedElements(maxWidth: Int, children: @Composable () -> Unit) {
    Box(modifier = Modifier.padding(defaultSpacing).width(maxWidth.dp)) {
        Row {
            children()
        }
    }
}

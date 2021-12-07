package ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

private val defaultSpacing = 2.dp

@Composable
fun SpacedTextField(value: String, label: String, maxWidth: Int, setDescription: (String) -> Unit) {
    Box(modifier = Modifier.padding(defaultSpacing).width(maxWidth.dp)) {
        TextField(
            value = value,
            label = { Text(label) },
            onValueChange = { setDescription(it) },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
fun SpacedButton(label: String, maxWidth: Int, onClick: () -> Unit) {
    Box(modifier = Modifier.padding(defaultSpacing).width(maxWidth.dp)) {
        Button(
            onClick = {
                onClick()
            },
            modifier = Modifier.width(200.dp),
        ) {
            Text(label)
        }
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

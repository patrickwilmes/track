/*
 * Copyright (c) 2022, Patrick Wilmes <patrick.wilmes@bit-lake.com>
 *
 * SPDX-License-Identifier: BSD-2-Clause
 */
package ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.unit.dp

private val defaultSpacing = 2.dp

@Composable
fun spacedTextField(
    value: String,
    label: String,
    maxWidth: Int,
    isReadonly: Boolean = false,
    setValue: (String) -> Unit,
    onValueChange: (String) -> Unit = {},
    onKeyEvent: (KeyEvent) -> Boolean = { false },
) {
    Box(modifier = Modifier.padding(defaultSpacing).width(maxWidth.dp)) {
        TextField(
            singleLine = true,
            value = value,
            label = { Text(label) },
            onValueChange = {
                setValue(it)
                onValueChange(it)
            },
            modifier = Modifier
                .fillMaxWidth()
                .onKeyEvent { onKeyEvent(it) },
            readOnly = isReadonly,
        )
    }
}

@Composable
fun spacedElements(maxWidth: Int, children: @Composable () -> Unit) {
    Box(modifier = Modifier.padding(defaultSpacing).width(maxWidth.dp)) {
        Row {
            children()
        }
    }
}

/*
 * Copyright (c) 2022, Patrick Wilmes <patrick.wilmes@bit-lake.com>
 *
 * SPDX-License-Identifier: BSD-2-Clause
 */
package ui.components

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import time.secondsToFormattedString

@Composable
fun timeLabel(givenSeconds: Long, fontWeight: FontWeight = FontWeight.Normal, fontSize: Int = 1, modifier: Modifier = Modifier.defaultMinSize()) {
    Text(
        givenSeconds.secondsToFormattedString(),
        fontWeight = fontWeight,
        fontSize = fontSize.em,
        modifier = modifier,
    )
}

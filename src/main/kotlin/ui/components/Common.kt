package ui.components

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em

@Composable
fun TimeLabel(givenSeconds: Long, fontWeight: FontWeight = FontWeight.Normal, fontSize: Int = 1, modifier: Modifier = Modifier.defaultMinSize()) {
    val clockHours = givenSeconds / 3600
    val clockMinutes = (givenSeconds % 3600) / 60
    val clockSeconds = givenSeconds % 60
    val formattedString =
        String.format("%02d:%02d:%02d", clockHours, clockMinutes, clockSeconds)
    Text(
        formattedString,
        fontWeight = fontWeight,
        fontSize = fontSize.em,
        modifier = modifier,
    )
}

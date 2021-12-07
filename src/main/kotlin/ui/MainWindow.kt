package ui

import androidx.compose.desktop.DesktopMaterialTheme
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import time.TimeEntry
import time.Timer
import time.asFormattedString
import time.getTimeEntriesForDay
import ui.components.Separator
import ui.components.TimeLabel
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime

@Composable
fun App() {
    DesktopMaterialTheme {
        val timer = remember { Timer() }
        val time = remember { mutableStateOf(Duration.ofMillis(0)) }
        val stateVertical = rememberScrollState(0)
        val (timeEntries, setTimeEntries) = remember { mutableStateOf(getTimeEntriesForDay(LocalDate.now())) }
        Column {
            TimeTrackingBox(timer, time.value.seconds, setTimeEntries)
            Separator()
            Row {
                Column(
                    modifier = Modifier.padding(10.dp).verticalScroll(stateVertical)
                        .fillMaxWidth(0.99f)
                ) {
                    timeEntries
                        .groupBy { it.projectName }
                        .values
                        .forEach {
                            ProjectBar(it)
                        }
                }
                VerticalScrollbar(
                    modifier = Modifier.fillMaxHeight().align(Alignment.CenterVertically),
                    adapter = rememberScrollbarAdapter(stateVertical)
                )
            }
            LaunchedEffect(Unit) {
                while (true) {
                    withFrameMillis {
                        if (timer.isRunning)
                            time.value = timer.tick()
                    }
                }
            }
        }
    }
}

@Composable
private fun TimeTrackingBox(
    timer: Timer,
    timeInSeconds: Long,
    setTimeEntries: (List<TimeEntry>) -> Unit
) {
    Row(modifier = Modifier.padding(10.dp)) {
        TimeTrackingControls(timer, setTimeEntries)
        Column {
            Box(modifier = Modifier.padding(start = 10.dp, top = 8.dp)) {
                TimeLabel(
                    timeInSeconds,
                    fontSize = 2,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.height(DefaultComponentHeight.dp)
                )
            }
        }
    }
}

@Composable
fun ProjectBar(timeEntries: List<TimeEntry>) {
    val (isContentVisible, setContentVisible) = remember { mutableStateOf(false) }
    Column(modifier = Modifier.padding(top = 4.dp)) {
        Row(
            modifier = Modifier.background(color = Color.LightGray).fillMaxWidth()
                .clickable { setContentVisible(!isContentVisible) }.padding(2.dp)
        ) {
            Text("Project: ${timeEntries.first().projectName}")
        }
        if (isContentVisible) {
            Column {
                timeEntries.forEach {
                    TimeEntryItem(it)
                }
            }
        }
        Box(modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)) {
            Separator()
        }
    }
}

@Composable
fun TimeEntryItem(timeEntry: TimeEntry) {
    val padding = Modifier.padding(top = 7.dp, end = 5.dp)

    Row(modifier = Modifier.padding(top = 4.dp)) {
        Column {
            Text(
                "Project: ${timeEntry.projectName}: ${timeEntry.description}",
                fontWeight = FontWeight.Bold,
                modifier = padding
            )
            Box(modifier = Modifier.padding(top = 4.dp)) {
                Text(
                    text = "Start: ${timeEntry.start.asFormattedString()} End: ${timeEntry.end.asFormattedString()}",
                    modifier = padding,
                )
            }
        }
        Button(onClick = {}) {
            Text("Delete")
        }
    }
    Row(modifier = Modifier.padding(top = 4.dp)) {
        Separator()
    }
}

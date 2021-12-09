package ui

import androidx.compose.desktop.DesktopMaterialTheme
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import time.*
import ui.components.Separator
import ui.components.TimeLabel
import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private enum class Direction {
    Left, Right,
}

@Composable
fun App() {
    DesktopMaterialTheme {
        val timer = remember { Timer() }
        val (time, setTime) = remember { mutableStateOf(Duration.ofMillis(0)) }
        val stateVertical = rememberScrollState(0)
        val (timeEntries, setTimeEntries) = remember { mutableStateOf(getTimeEntriesForDay(LocalDate.now())) }
        val (workingDay, setWorkingDay) = remember { mutableStateOf(LocalDate.now()) }

        Column(modifier = Modifier.background(color = Color(0xFFF0F0EB))) {
            TimeTrackingBox(timer, time.seconds, setTimeEntries, setTime)
            Separator()
            Row(
                modifier = Modifier.padding(10.dp).fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            ) {
                val startOfWeek = workingDay.with(DayOfWeek.MONDAY)
                val endOfWeek = workingDay.with(DayOfWeek.FRIDAY)
                val onClick = { e: Direction ->
                    val date = when (e) {
                        Direction.Left -> startOfWeek.minusWeeks(1).with(DayOfWeek.MONDAY)
                        Direction.Right -> startOfWeek.plusWeeks(1).with(DayOfWeek.MONDAY)
                    }
                    setWorkingDay(date)
                    setTimeEntries(getTimeEntriesForDay(date))
                }
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "",
                    modifier = Modifier.clickable {
                        onClick(Direction.Left)
                    })

                // todo - this should be centered
                val textModifier = if (LocalDate.now().isBefore(endOfWeek) && LocalDate.now().isAfter(startOfWeek))
                    Modifier.padding(start = 5.dp, end = 5.dp).background(color = Color(0xFFA8C0CE)).fillMaxWidth(0.9f)
                else
                    Modifier.padding(start = 5.dp, end = 5.dp).fillMaxWidth(0.9f)
                Text(
                    "${startOfWeek.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))} - ${
                        endOfWeek.format(
                            DateTimeFormatter.ofPattern("dd.MM.yyyy")
                        )
                    }",
                    modifier = textModifier,
                )
                Icon(
                    Icons.Default.ArrowForward,
                    contentDescription = "",
                    modifier = Modifier.clickable {
                        onClick(Direction.Right)
                    })
            }
            Row {
                Column(
                    modifier = Modifier.padding(10.dp).verticalScroll(stateVertical)
                        .fillMaxWidth(0.99f)
                ) {
                    timeEntries
                        .groupBy { it.day }
                        .values
                        .forEach {
                            DayBar(it, setTimeEntries, currentWorkingDay = workingDay)
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
                            setTime(timer.tick())
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
    setTimeEntries: (List<TimeEntry>) -> Unit,
    setTime: (Duration) -> Unit
) {
    Row(modifier = Modifier.padding(10.dp)) {
        TimeTrackingControls(timer, setTimeEntries, setTime)
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
fun DayBar(
    timeEntries: List<TimeEntry>,
    setTimeEntries: (List<TimeEntry>) -> Unit,
    currentWorkingDay: LocalDate
) {
    val (isContentVisible, setContentVisible) = remember { mutableStateOf(false) }
    Column(modifier = Modifier.padding(top = 4.dp)) {
        Row(
            modifier = Modifier.background(color = Color(0xFF8B9386)).fillMaxWidth()
                .clickable { setContentVisible(!isContentVisible) }.padding(2.dp)
        ) {
            val totalDuration = timeEntries.sumOf { it.duration.seconds }
            Text("Day: ${timeEntries.first().day.asFormattedDateString()} Total Duration: ${totalDuration.secondsToFormattedString()}")
        }
        if (isContentVisible) {
            Column {
                timeEntries
                    .groupBy { it.projectName }
                    .values
                    .forEach { entries ->
                        ProjectBar(entries, setTimeEntries, currentWorkingDay)
                    }
            }
        }
    }
}

@Composable
fun ProjectBar(
    timeEntries: List<TimeEntry>,
    setTimeEntries: (List<TimeEntry>) -> Unit,
    currentWorkingDay: LocalDate
) {
    val (isContentVisible, setContentVisible) = remember { mutableStateOf(false) }
    Column(modifier = Modifier.padding(top = 4.dp, start = 4.dp)) {
        Row(
            modifier = Modifier.background(color = Color(0xFFA9B1A1)).fillMaxWidth()
                .clickable { setContentVisible(!isContentVisible) }.padding(2.dp)
        ) {
            val totalDuration = timeEntries.sumOf { it.duration.seconds }
            Text("Project: ${timeEntries.first().projectName} Duration: ${totalDuration.secondsToFormattedString()}")
        }
        if (isContentVisible) {
            Column {
                timeEntries.forEach {
                    TimeEntryItem(it, setTimeEntries, currentWorkingDay)
                }
            }
        }
    }
}


@Composable
fun TimeEntryItem(
    timeEntry: TimeEntry,
    setTimeEntries: (List<TimeEntry>) -> Unit,
    currentWorkingDay: LocalDate
) {
    val padding = Modifier.padding(top = 7.dp, end = 5.dp)
    val (durationText, setDurationText) = remember { mutableStateOf(timeEntry.duration.seconds.secondsToFormattedString()) }
    Row(modifier = Modifier.padding(top = 4.dp)) {
        Column(modifier = Modifier.fillMaxWidth(.8f)) {
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
        Column {
            TextField(
                value = durationText,
                onValueChange = {
                    setDurationText(it)
                }
            )
        }

    }
    Row(modifier = Modifier.padding(top = 5.dp)) {
        Button(onClick = {
            val parts = durationText.split(":")
            val hours = parts[0].toLong()
            val minutes = parts[1].toLong()
            val seconds = parts[2].toLong()
            val timeInSeconds = seconds + (minutes * 60) + (hours * 60 * 60)
            setDurationText(timeInSeconds.secondsToFormattedString())
            updateTimeEntry(timeEntry, Duration.ofSeconds(timeInSeconds))
            setTimeEntries(getTimeEntriesForDay(currentWorkingDay))
        }) {
            Text("Update")
        }
        Button(
            onClick = {
                deleteTimeEntryById(timeEntry.id!!)
                setTimeEntries(getTimeEntriesForDay(currentWorkingDay))
            },
            modifier = Modifier.padding(start = 4.dp)
        ) {
            Text("Delete")
        }
    }
    Row(modifier = Modifier.padding(top = 4.dp)) {
        Separator()
    }
}

/*
 * Copyright (c) 2022, Patrick Wilmes <patrick.wilmes@bit-lake.com>
 *
 * SPDX-License-Identifier: BSD-2-Clause
 */
package ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import project.Day
import settings.getSettings
import time.Timer
import tracker.TrackingDataService
import ui.components.separator
import ui.components.timeLabel
import java.time.Duration
import java.time.LocalDate


@Composable
fun app() {
    MaterialTheme {
        val timer = remember { Timer() }
        val (time, setTime) = remember { mutableStateOf(Duration.ofMillis(0)) }
        val (projects, setProjects) = remember {
            mutableStateOf(
                TrackingDataService.getAllProjectsFor(
                    LocalDate.now()
                )
            )
        }
        val (settingsDialogVisible, setSettingsDialogVisible) = remember { mutableStateOf(false) }

        Column(modifier = Modifier.background(color = Color(0xFFF0F0EB))) {
            Column(
                modifier = Modifier.fillMaxWidth().background(color = Color(0xFF7F807A))
                    .padding(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "",
                    modifier = Modifier.clickable {
                        setSettingsDialogVisible(true)
                    }.align(Alignment.End)

                )
                if (settingsDialogVisible) {
                    settingsDialog(setSettingsDialogVisible)
                }
            }
            separator()
            workweekMetaInformation(projects)
            separator()
            timeTrackingBox(timer, time.seconds, setProjects, setTime)
            separator()
            timeEntryList(projects, setProjects)
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
fun workweekMetaInformation(
    days: List<Day>,
) {
    val settings = remember { getSettings() }
    val totalSecondsWorked =
        days.sumOf { it.project.sumOf { project -> project.totalDurationSeconds } }
    Row(modifier = Modifier.padding(10.dp)) {
        Column {
            Box(modifier = Modifier.padding(start = 10.dp, top = 8.dp)) {
                Row {
                    Column {
                        Text("Remaining hours to work this week: ", fontWeight = FontWeight.Bold)
                    }
                    Column {
                        val remaining = settings.hoursPerWeek.minus(Duration.ofSeconds(totalSecondsWorked))
                        with (remaining) {
                            val hours = toHours()
                            val minutes = toMinutesPart().toString().padStart(length = 2, padChar = '0')
                            val seconds = toSecondsPart().toString().padStart(length = 2, padChar = '0')
                            Text("$hours:$minutes:$seconds")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun timeTrackingBox(
    timer: Timer,
    timeInSeconds: Long,
    setTimeEntries: (List<Day>) -> Unit,
    setTime: (Duration) -> Unit
) {
    Row(modifier = Modifier.padding(10.dp)) {
        timeTrackingControls(timer, setTimeEntries, setTime)
        Column {
            Box(modifier = Modifier.padding(start = 10.dp, top = 8.dp)) {
                timeLabel(
                    timeInSeconds,
                    fontSize = 2,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.height(DefaultComponentHeight.dp)
                )
            }
        }
    }
}

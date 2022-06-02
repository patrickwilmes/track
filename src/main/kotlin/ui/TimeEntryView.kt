/*
 * Copyright (c) 2022, Patrick Wilmes <patrick.wilmes@bit-lake.com>
 *
 * SPDX-License-Identifier: BSD-2-Clause
 */
package ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import project.Day
import project.Project
import project.getAllProjectsFor
import time.*
import tracker.TrackingDataService
import ui.components.Separator
import java.time.Duration
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private enum class Direction {
    Left, Right,
}

@Composable
fun TimeEntryList(timeEntries: List<Day>, setTimeEntries: (List<Day>) -> Unit) {
    val stateVertical = rememberScrollState(0)
    val (workingDay, setWorkingDay) = remember { mutableStateOf(LocalDate.now()) }
    Row(
        modifier = Modifier.padding(10.dp).fillMaxWidth()
    ) {
        val week = getWeekForDate(workingDay)
        val onClick = { e: Direction ->
            val date = when (e) {
                Direction.Left -> getFirstDayOfPreviousWeek(week.getFirstDayOfTheWeek())
                Direction.Right -> getFirstDayOfNextWeek(week.getFirstDayOfTheWeek())
            }
            setWorkingDay(date)
            setTimeEntries(TrackingDataService.getAllProjectsFor(date))
        }
        Icon(
            Icons.Default.ArrowBack,
            contentDescription = "",
            modifier = Modifier.clickable {
                onClick(Direction.Left)
            })

        // todo - this should be centered
        val textModifier = if (LocalDate.now().isBefore(week.getLastDayOfTheWeek()) && LocalDate.now().isAfter(week.getFirstDayOfTheWeek()))
            Modifier.padding(start = 5.dp, end = 5.dp).background(color = Color(0xFFA8C0CE)).fillMaxWidth(0.9f)
        else
            Modifier.padding(start = 5.dp, end = 5.dp).fillMaxWidth(0.9f)
        Text(
            "${week.getFirstDayOfTheWeek().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))} - ${
                week.getLastDayOfTheWeek().format(
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
                .forEach {
                    DayBar(it, setTimeEntries, currentWorkingDay = workingDay)
                }
        }
        VerticalScrollbar(
            modifier = Modifier.fillMaxHeight().align(Alignment.CenterVertically),
            adapter = rememberScrollbarAdapter(stateVertical)
        )
    }
}

@Composable
fun DayBar(
    day: Day,
    setTimeEntries: (List<Day>) -> Unit,
    currentWorkingDay: LocalDate
) {
    val (isContentVisible, setContentVisible) = remember { mutableStateOf(false) }
    Column(modifier = Modifier.padding(top = 4.dp)) {
        Row(
            modifier = Modifier.background(color = Color(0xFF8B9386)).fillMaxWidth()
                .clickable { setContentVisible(!isContentVisible) }.padding(2.dp)
        ) {
            Text("Day: ${day.date.asFormattedDateString()} Total Duration: ${day.totalDurationFormatted}")
        }
        if (isContentVisible) {
            Column {
                day.project
                    .forEach { entries ->
                        ProjectBar(entries, setTimeEntries, currentWorkingDay)
                    }
            }
        }
    }
}

@Composable
private fun ProjectBar(
    project: Project,
    setTimeEntries: (List<Day>) -> Unit,
    currentWorkingDay: LocalDate
) {
    val (isContentVisible, setContentVisible) = remember { mutableStateOf(false) }
    Column(modifier = Modifier.padding(top = 4.dp, start = 4.dp)) {
        Row(
            modifier = Modifier.background(color = Color(0xFFA9B1A1)).fillMaxWidth()
                .clickable { setContentVisible(!isContentVisible) }.padding(2.dp)
        ) {
            Text("Project: ${project.name} Duration: ${project.totalDurationTodayFormatted}")
        }
        if (isContentVisible) {
            Column {
                project.timeEntries.forEach {
                    TimeEntryItem(it, setTimeEntries, currentWorkingDay)
                }
            }
        }
    }
}


@Composable
private fun TimeEntryItem(
    timeEntry: TimeEntry,
    setTimeEntries: (List<Day>) -> Unit,
    currentWorkingDay: LocalDate
) {
    val padding = Modifier.padding(top = 7.dp, end = 5.dp)
    val (durationText, setDurationText) = remember { mutableStateOf(timeEntry.duration.seconds.secondsToFormattedString()) }
    val (projectNameText, setProjectNameText) = remember { mutableStateOf(timeEntry.projectName) }
    val (descriptionText, setDescriptionText) = remember { mutableStateOf(timeEntry.description) }
    Row(modifier = Modifier.padding(top = 4.dp)) {
        Column(modifier = Modifier.fillMaxWidth(.8f)) {
            Row {
                Column {
                    Text(
                        "Project:",
                        fontWeight = FontWeight.Bold,
                        modifier = padding
                    )
                    TextField(value = projectNameText, onValueChange = { setProjectNameText(it) })
                }
                Column {
                    Text(
                        "Description:",
                        fontWeight = FontWeight.Bold,
                        modifier = padding
                    )
                    TextField(value = descriptionText, onValueChange = { setDescriptionText(it) })
                }
            }
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
            // todo - this should be done in the backend
            val parts = durationText.split(":")
            val hours = parts[0].toLong()
            val minutes = parts[1].toLong()
            val seconds = parts[2].toLong()
            val timeInSeconds = seconds + (minutes * 60) + (hours * 60 * 60)
            setDurationText(timeInSeconds.secondsToFormattedString())
            val updatedEntry = timeEntry.copy(projectName = projectNameText, description = descriptionText)
            TrackingDataService.updateTimeEntry(updatedEntry, Duration.ofSeconds(timeInSeconds))
            setTimeEntries(getAllProjectsFor(currentWorkingDay))
        }) {
            Text("Update")
        }
        Button(
            onClick = {
                TrackingDataService.deleteTimeEntryById(timeEntry.id!!)
                setTimeEntries(TrackingDataService.getAllProjectsFor(currentWorkingDay))
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

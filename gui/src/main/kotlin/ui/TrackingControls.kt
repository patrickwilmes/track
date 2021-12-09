package ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.unit.dp
import project.Day
import project.getAllProjectsFor
import time.TimeEntry
import time.Timer
import time.getProjectNamesStartingWith
import time.saveTimeEntry
import ui.components.SpacedElements
import ui.components.SpacedTextField
import java.time.Duration
import java.time.LocalDate

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TimeTrackingControls(
    timer: Timer,
    setTimeEntries: (List<Day>) -> Unit,
    setTime: (Duration) -> Unit
) {
    val (description, setDescription) = remember { mutableStateOf("") }
    val (projectName, setProjectName) = remember { mutableStateOf("") }
    val startStopButtonLabel = remember { mutableStateOf("Start") }
    val (textFieldsReadonly, setTextFieldsReadonly) = remember { mutableStateOf(false) }
    val (projectNameSuggestions, setProjectNameSuggestions) = remember { mutableStateOf(emptyList<String>()) }
    val (selectedSuggestionIndex, setSelectedSuggestionIndex) = remember { mutableStateOf(0) }

    Column {
        Row {
            Column {
                SpacedTextField(
                    value = description,
                    label = "Description",
                    maxWidth = 450,
                    isReadonly = textFieldsReadonly,
                    setValue = setDescription
                )
                SpacedTextField(
                    value = projectName,
                    label = "Project Name",
                    maxWidth = 450,
                    isReadonly = textFieldsReadonly,
                    setValue = setProjectName,
                    onValueChange = {
                        setProjectNameSuggestions(getProjectNamesStartingWith(it))
                    },
                    onKeyEvent = {
                        if (it.key == Key.DirectionDown) {
                            if (selectedSuggestionIndex < projectNameSuggestions.size - 1) {
                                setSelectedSuggestionIndex(selectedSuggestionIndex+1)
                            } else {
                                setSelectedSuggestionIndex(0)
                            }
                        } else if (it.key == Key.DirectionUp) {
                            if (selectedSuggestionIndex >= 0 && selectedSuggestionIndex < projectNameSuggestions.size - 1) {
                                setSelectedSuggestionIndex(selectedSuggestionIndex-1)
                            } else {
                                setSelectedSuggestionIndex(0)
                            }
                        }
                        if (it.key == Key.Enter) {
                            setProjectName(projectNameSuggestions[selectedSuggestionIndex])
                        }
                        false
                    }
                )
                if (projectNameSuggestions.isNotEmpty()) {
                    val baseModifier = Modifier.padding(start = 4.dp)
                    Box {
                        Column {
                            projectNameSuggestions.forEachIndexed { index, elem ->
                                if (index == selectedSuggestionIndex) {
                                    Text(
                                        text = elem,
                                        modifier = baseModifier.clickable { setProjectName(elem) }
                                            .background(color = Color.LightGray)
                                    )
                                } else {
                                    Text(
                                        text = elem,
                                        modifier = baseModifier.clickable { setProjectName(elem) }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        Row {
            SpacedElements(450) {
                Box(modifier = Modifier.padding(end = 50.dp)) {
                    Button(
                        onClick = {
                            startStopButtonLabel.value = if (timer.isRunning) {
                                handleTimerStopAction(
                                    timer,
                                    projectName,
                                    description,
                                    setTextFieldsReadonly,
                                    setTimeEntries,
                                    setDescription,
                                    setProjectName,
                                    setTime,
                                    false,
                                )
                                "Start"
                            } else {
                                setTextFieldsReadonly(true)
                                timer.start()
                                "Stop"
                            }
                        },
                        modifier = Modifier.width(200.dp),
                    ) {
                        Text(startStopButtonLabel.value)
                    }
                }
                Button(
                    enabled = timer.isRunning,
                    onClick = {
                        if (timer.isRunning) {
                            handleTimerStopAction(
                                timer,
                                projectName,
                                description,
                                setTextFieldsReadonly,
                                setTimeEntries,
                                setDescription,
                                setProjectName,
                                setTime,
                                true
                            )
                        }
                    },
                    modifier = Modifier.width(200.dp),
                ) {
                    Text("Stop (round)")
                }
            }
        }
    }
}

private fun handleTimerStopAction(
    timer: Timer,
    projectName: String,
    description: String,
    setTextFieldsReadonly: (Boolean) -> Unit,
    setTimeEntries: (List<Day>) -> Unit,
    setDescription: (String) -> Unit,
    setProjectName: (String) -> Unit,
    setTime: (Duration) -> Unit,
    doRound: Boolean,
) {
    timer.stop()
    saveTimeEntry(
        TimeEntry(
            projectName = projectName,
            description = description,
            start = timer.startTime!!,
            end = timer.endTime!!
        ),
        doRound = doRound,
    )
    setTextFieldsReadonly(false)
    setTimeEntries(getAllProjectsFor(LocalDate.now()))
    setDescription("")
    setProjectName("")
    setTime(Duration.ZERO)
    timer.reset()
}

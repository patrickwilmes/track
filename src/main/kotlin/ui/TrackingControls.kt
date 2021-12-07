package ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import time.TimeEntry
import time.Timer
import time.getTimeEntriesForDay
import time.saveTimeEntry
import ui.components.SpacedElements
import ui.components.SpacedTextField
import java.time.Duration
import java.time.LocalDate

@Composable
fun TimeTrackingControls(
    timer: Timer,
    setTimeEntries: (List<TimeEntry>) -> Unit,
    setTime: (Duration) -> Unit
) {
    val (description, setDescription) = remember { mutableStateOf("") }
    val (projectName, setProjectName) = remember { mutableStateOf("") }
    val startStopButtonLabel = remember { mutableStateOf("Start") }
    val stopAndRoundButtonEnabled = remember { mutableStateOf(false) }
    val (textFieldsReadonly, setTextFieldsReadonly) = remember { mutableStateOf(false) }

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
                    setValue = setProjectName
                )
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
                                )
                                "Start"
                            } else {
                                setTextFieldsReadonly(true)
                                timer.start()
                                stopAndRoundButtonEnabled.value = true
                                "Stop"
                            }
                        },
                        modifier = Modifier.width(200.dp),
                    ) {
                        Text(startStopButtonLabel.value)
                    }
                }
                Button(
                    enabled = stopAndRoundButtonEnabled.value,
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
    setTimeEntries: (List<TimeEntry>) -> Unit,
    setDescription: (String) -> Unit,
    setProjectName: (String) -> Unit,
    setTime: (Duration) -> Unit,
) {
    timer.stop()
    saveTimeEntry(
        TimeEntry(
            projectName = projectName,
            description = description,
            start = timer.startTime!!,
            end = timer.endTime!!
        ), doRound = true
    )
    setTextFieldsReadonly(false)
    setTimeEntries(getTimeEntriesForDay(LocalDate.now()))
    setDescription("")
    setProjectName("")
    setTime(Duration.ZERO)
    timer.reset()
}

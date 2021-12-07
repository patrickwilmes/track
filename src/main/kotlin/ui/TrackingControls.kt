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
import java.time.LocalDate

@Composable
fun TimeTrackingControls(
    timer: Timer,
    setTimeEntries: (List<TimeEntry>) -> Unit
) {
    val (description, setDescription) = remember { mutableStateOf("") }
    val (projectName, setProjectName) = remember { mutableStateOf("") }
    val startStopButtonLabel = remember { mutableStateOf("Start") }
    val stopAndRoundButtonEnabled = remember { mutableStateOf(false) }
    Column {
        Row {
            Column {
                SpacedTextField(description, "Description", 450, setDescription)
                SpacedTextField(projectName, "Project Name", 450, setProjectName)
            }
        }
        Row {
            SpacedElements(450) {
                Box(modifier = Modifier.padding(end = 50.dp)) {
                    Button(
                        onClick = {
                            startStopButtonLabel.value = if (timer.isRunning) {
                                timer.stop()
                                stopAndRoundButtonEnabled.value = false
                                saveTimeEntry(
                                    TimeEntry(
                                        projectName = projectName,
                                        description = description,
                                        start = timer.startTime!!,
                                        end = timer.endTime!!
                                    )
                                )
                                setTimeEntries(getTimeEntriesForDay(LocalDate.now()))
                                setDescription("")
                                setProjectName("")
                                timer.reset()
                                "Start"
                            } else {
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
                            timer.stop()
                            saveTimeEntry(
                                TimeEntry(
                                    projectName = projectName,
                                    description = description,
                                    start = timer.startTime!!,
                                    end = timer.endTime!!
                                ), doRound = true
                            )
                            setTimeEntries(getTimeEntriesForDay(LocalDate.now()))
                            setDescription("")
                            setProjectName("")
                            timer.reset()
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

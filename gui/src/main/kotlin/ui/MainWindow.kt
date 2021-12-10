package ui

import androidx.compose.desktop.DesktopMaterialTheme
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import project.Day
import project.getAllProjectsFor
import settings.getSettings
import settings.saveSettings
import time.*
import ui.components.Separator
import ui.components.TimeLabel
import java.time.Duration
import java.time.LocalDate


@Composable
fun App() {
    DesktopMaterialTheme {
        val timer = remember { Timer() }
        val (time, setTime) = remember { mutableStateOf(Duration.ofMillis(0)) }
        val (projects, setProjects) = remember { mutableStateOf(getAllProjectsFor(LocalDate.now())) }
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
                    Dialog(
                        onCloseRequest = { setSettingsDialogVisible(false) },
                        title = "Settings",
                    ) {
                        val settings = remember { mutableStateOf(getSettings()) }
                        val dialogIsDirty = remember { mutableStateOf(false) }
                        val setDirty = { dialogIsDirty.value = true }
                        Column(modifier = Modifier.padding(10.dp).fillMaxHeight()) {
                            Row {
                                Column {
                                    Row {
                                        Text("Common Settings")
                                    }
                                    Row(modifier = Modifier.padding(top = 4.dp)) {
                                        Text("Track on weekends")
                                        Switch(
                                            checked = settings.value.trackTimeOnWeekend,
                                            onCheckedChange = {
                                                settings.value =
                                                    settings.value.copy(trackTimeOnWeekend = it)
                                                setDirty()
                                            })
                                    }
                                    Row(modifier = Modifier.padding(top = 4.dp)) {
                                        Column {
                                            Text("Hours per week")
                                            TextField(
                                                value = settings.value.hoursPerWeek.toString(),
                                                singleLine = true,
                                                onValueChange = {
                                                    settings.value =
                                                        settings.value.copy(hoursPerWeek = it.toInt()) // todo - make all that parsing in some kind of view model
                                                    setDirty()
                                                })
                                        }
                                    }

                                }
                            }
                            Spacer(modifier = Modifier.fillMaxHeight(0.7f))
                            Row {
                                Button(
                                    onClick = {
                                        setSettingsDialogVisible(false)
                                        if (dialogIsDirty.value) {
                                            saveSettings(settings.value)
                                        }
                                    },
                                    modifier = Modifier.padding(start = 4.dp, end = 4.dp)
                                ) {
                                    Text("OK")
                                }
                                Button(
                                    onClick = {
                                        setSettingsDialogVisible(false)
                                    },
                                    modifier = Modifier.padding(start = 4.dp, end = 4.dp)
                                ) {
                                    Text("Cancel")
                                }
                            }
                        }
                    }
                }
            }
            TimeTrackingBox(timer, time.seconds, setProjects, setTime)
            Separator()
            TimeEntryList(projects, setProjects)
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
    setTimeEntries: (List<Day>) -> Unit,
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

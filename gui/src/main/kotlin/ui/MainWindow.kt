package ui

import androidx.compose.desktop.DesktopMaterialTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import project.Day
import time.Timer
import tracker.TrackingDataService
import ui.components.Separator
import ui.components.TimeLabel
import java.time.Duration
import java.time.LocalDate


@Composable
fun App() {
    DesktopMaterialTheme {
        val timer = remember { Timer() }
        val (time, setTime) = remember { mutableStateOf(Duration.ofMillis(0)) }
        val (projects, setProjects) = remember { mutableStateOf(TrackingDataService.getAllProjectsFor(LocalDate.now())) }
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
                    SettingsDialog(setSettingsDialogVisible)
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

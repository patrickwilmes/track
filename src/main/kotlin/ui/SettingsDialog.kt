/*
 * Copyright (c) 2022, Patrick Wilmes <patrick.wilmes@bit-lake.com>
 *
 * SPDX-License-Identifier: BSD-2-Clause
 */
package ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.WindowSize
import androidx.compose.ui.window.rememberDialogState
import settings.getSettings
import settings.saveSettings
import java.time.Duration

@Composable
fun settingsDialog(setSettingsDialogVisible: (Boolean) -> Unit) {
    Dialog(
        onCloseRequest = { setSettingsDialogVisible(false) },
        title = "Settings",
        state = rememberDialogState(size = WindowSize(width = 400.dp, height = 400.dp))
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
                                        settings.value.copy(hoursPerWeek = Duration.ofHours(it.toLong())) // todo - make all that parsing in some kind of view model
                                    setDirty()
                                })
                        }
                    }
                    Row(modifier = Modifier.padding(top = 4.dp)) {
                        Column {
                            val enableRemoteServer = remember { mutableStateOf(false) }
                            Text("Enable Remote Server")
                            Switch(checked = settings.value.storeDataRemote, onCheckedChange = {
                                enableRemoteServer.value = it
                                settings.value = settings.value.copy(storeDataRemote = it)
                                setDirty()
                            })
                            TextField(
                                value = settings.value.remoteServerAddress,
                                enabled = enableRemoteServer.value,
                                singleLine = true,
                                onValueChange = {
                                    settings.value =
                                        settings.value.copy(remoteServerAddress = it)
                                    setDirty()
                                })
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.fillMaxHeight(0.4f))
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

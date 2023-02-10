/*
 * Copyright (c) 2022, Patrick Wilmes <patrick.wilmes@bit-lake.com>
 *
 * SPDX-License-Identifier: BSD-2-Clause
 */
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.*
import db.AppMode
import db.Mode
import db.setupDatabase
import ui.app

fun main(args: Array<String>) = application {
    if (args.any { it == "dev" })
        AppMode = Mode.Dev
    setupDatabase()
    Window(
        onCloseRequest = ::exitApplication,
        state = rememberWindowState(position = WindowPosition.Aligned(Alignment.Center)),
        resizable = false,
        icon = painterResource("track-icon.ico"),
        title = "Track",
    ) {
        app()
    }
}

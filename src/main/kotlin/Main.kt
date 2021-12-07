import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import time.TimeEntry
import time.deleteAll
import time.saveTimeEntry
import ui.App
import java.time.LocalDate
import java.time.LocalDateTime

fun main(args: Array<String>) = application {
    if (args.any { it == "dev" })
        AppMode = Mode.Dev
    setupDatabase()
    setCustomTimeIfRequired(args)
    generateTestDataIfRequired(args)
    Window(
        onCloseRequest = ::exitApplication,
        resizable = false,
        icon = painterResource("track-icon.ico"),
        title = "Track"
    ) {
        App()
    }
}

fun setCustomTimeIfRequired(args: Array<String>) {
    if (args.any { it == "customTime" }) {
        Today = LocalDate.of(2021, 11, 12)
    }
}

private fun generateTestDataIfRequired(args: Array<String>) {
    if (args.any { it == "testData" }) {
        deleteAll()
        (0..10).forEach {
            saveTimeEntry(
                TimeEntry(
                    projectName = "project-1",
                    description = "project-1-description",
                    start = LocalDateTime.of(2021, 11, Today.dayOfMonth, 1, 0, 0),
                    end = LocalDateTime.of(2021, 11, Today.dayOfMonth, 2, 0, 0),
                )
            )
            saveTimeEntry(
                TimeEntry(
                    projectName = "project-2",
                    description = "project-2-description",
                    start = LocalDateTime.of(2021, 11, Today.dayOfMonth, 1, 0, 0),
                    end = LocalDateTime.of(2021, 11, Today.dayOfMonth, 2, 0, 0),
                )
            )
            saveTimeEntry(
                TimeEntry(
                    projectName = "project-1",
                    description = "project-1-description",
                    start = LocalDateTime.of(2021, 11, Today.dayOfMonth, 1, 0, 0),
                    end = LocalDateTime.of(2021, 11, Today.dayOfMonth, 2, 0, 0),
                )
            )
            saveTimeEntry(
                TimeEntry(
                    projectName = "project-2",
                    description = "project-2-description",
                    start = LocalDateTime.of(2021, 11, Today.dayOfMonth, 1, 0, 0),
                    end = LocalDateTime.of(2021, 11, Today.dayOfMonth, 2, 0, 0),
                )
            )
            Today = Today.minusDays(1)
        }
    }
}

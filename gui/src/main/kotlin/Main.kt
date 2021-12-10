import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.*
import db.AppMode
import db.Mode
import db.setupDatabase
import ui.App

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
        App()
    }
}

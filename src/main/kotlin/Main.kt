import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import ui.App

fun main() = application {
    setupDatabase()
    Window(
        onCloseRequest = ::exitApplication,
        resizable = false,
        icon = painterResource("track-icon.ico"),
        title = "Track"
    ) {
        App()
    }
}

package settings

data class Settings(
    val trackTimeOnWeekend: Boolean = false,
    val hoursPerWeek: Int = 40,
    val storeDataRemote: Boolean = false,
    val remoteServerAddress: String = "",
)

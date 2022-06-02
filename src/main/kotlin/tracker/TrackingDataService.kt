package tracker

import project.Day
import settings.Settings
import settings.getSettings
import settings.saveSettings
import time.TimeEntry
import java.time.Duration
import java.time.LocalDate

object TrackingDataService {
    private var settings = getSettings()

    // todo - after this is initially set it will not change during runtime this might be useful but also will require a full sync
    private val dataService = getDataServiceForSettings(settings)

    fun updateSettings(newSettings: Settings) {
        settings = newSettings
        dataService.updateSettings(newSettings)
    }

    fun getAllProjectsFor(dayInWorkingWeek: LocalDate): List<Day> {
        return dataService.getAllProjectsFor(dayInWorkingWeek)
    }

    fun saveTimeEntry(timeEntry: TimeEntry, doRound: Boolean = false) {
        dataService.saveTimeEntry(timeEntry, doRound)
    }

    fun deleteTimeEntryById(id: Int) {
        dataService.deleteTimeEntryById(id)
    }

    fun updateTimeEntry(timeEntry: TimeEntry, newDuration: Duration) {
        dataService.updateTimeEntry(timeEntry, newDuration)
    }

    fun getProjectNamesStartingWith(leading: String): List<String> {
        return dataService.getProjectNamesStartingWith(leading)
    }
}

private fun getDataServiceForSettings(settings: Settings): DataService {
    if (settings.storeDataRemote)
        return RemoteDataService
    return LocalDataService
}

internal interface DataService {
    fun updateSettings(newSettings: Settings)
    fun getAllProjectsFor(dayInWorkingWeek: LocalDate): List<Day>
    fun saveTimeEntry(timeEntry: TimeEntry, doRound: Boolean = false)
    fun deleteTimeEntryById(id: Int)
    fun updateTimeEntry(timeEntry: TimeEntry, newDuration: Duration)
    fun getProjectNamesStartingWith(leading: String): List<String>
}

internal object LocalDataService : DataService {
    override fun updateSettings(newSettings: Settings) {
        saveSettings(newSettings)
    }

    override fun getAllProjectsFor(dayInWorkingWeek: LocalDate): List<Day> {
        return project.getAllProjectsFor(dayInWorkingWeek)
    }

    override fun saveTimeEntry(timeEntry: TimeEntry, doRound: Boolean) {
        time.saveTimeEntry(timeEntry, doRound)
    }

    override fun deleteTimeEntryById(id: Int) {
        time.deleteTimeEntryById(id)
    }

    override fun updateTimeEntry(timeEntry: TimeEntry, newDuration: Duration) {
        time.updateTimeEntry(timeEntry, newDuration)
    }

    override fun getProjectNamesStartingWith(leading: String): List<String> {
        return time.getProjectNamesStartingWith(leading)
    }
}

internal object RemoteDataService : DataService {
    override fun updateSettings(newSettings: Settings) {
        TODO("Not yet implemented")
    }

    override fun getAllProjectsFor(dayInWorkingWeek: LocalDate): List<Day> {
        TODO("Not yet implemented")
    }

    override fun saveTimeEntry(timeEntry: TimeEntry, doRound: Boolean) {
        TODO("Not yet implemented")
    }

    override fun deleteTimeEntryById(id: Int) {
        TODO("Not yet implemented")
    }

    override fun updateTimeEntry(timeEntry: TimeEntry, newDuration: Duration) {
        TODO("Not yet implemented")
    }

    override fun getProjectNamesStartingWith(leading: String): List<String> {
        TODO("Not yet implemented")
    }
}

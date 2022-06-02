package project

import time.getTimeEntriesForDay
import java.time.LocalDate

fun getAllProjectsFor(dayInWorkingWeek: LocalDate): List<Day> =
    getTimeEntriesForDay(dayInWorkingWeek)
        .groupBy { it.day }
        .map { (day, entries) ->
            val byProjectName = entries.groupBy { it.projectName }
            val projects = byProjectName.map { (projectName, entires) -> Project(projectName, entires.toSet()) }
            Day(day!!, projects)
        }

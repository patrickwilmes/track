/*
 * Copyright (c) 2022, Patrick Wilmes <patrick.wilmes@bit-lake.com>
 *
 * SPDX-License-Identifier: BSD-2-Clause
 */
package project

import time.getTimeEntriesForDay
import java.time.LocalDate

fun getAllProjectsFor(dayInWorkingWeek: LocalDate): List<Day> =
    getTimeEntriesForDay(dayInWorkingWeek)
        .groupBy { it.day }
        .map { (day, entries) ->
            val byProjectName = entries.groupBy { it.projectName }
            val projects = byProjectName.map { (projectName, entries) -> Project(projectName, entries.toSet()) }
            Day(day!!, projects)
        }

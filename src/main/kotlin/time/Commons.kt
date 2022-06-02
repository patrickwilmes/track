/*
 * Copyright (c) 2022, Patrick Wilmes <patrick.wilmes@bit-lake.com>
 *
 * SPDX-License-Identifier: BSD-2-Clause
 */
package time

import settings.getSettings
import java.time.DayOfWeek
import java.time.LocalDate

typealias Week = Pair<LocalDate, LocalDate>
fun Week.getFirstDayOfTheWeek() = first
fun Week.getLastDayOfTheWeek() = second

fun getWeekForDate(date: LocalDate): Week {
    val start = date.with(DayOfWeek.MONDAY)
    val end = if (getSettings().trackTimeOnWeekend)
        date.with(DayOfWeek.SUNDAY)
    else
        date.with(DayOfWeek.FRIDAY)
    return Week(start, end)
}

fun getFirstDayOfPreviousWeek(localDate: LocalDate): LocalDate = localDate.minusWeeks(1).with(DayOfWeek.MONDAY)
fun getFirstDayOfNextWeek(localDate: LocalDate): LocalDate = localDate.plusWeeks(1).with(DayOfWeek.MONDAY)

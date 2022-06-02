package time

import settings.getSettings
import java.time.DayOfWeek
import java.time.LocalDate

typealias Week = Pair<LocalDate, LocalDate>
fun Week.getStartDay() = first
fun Week.getEndDay() = second

fun getWeekForDate(date: LocalDate): Week {
    val start = date.with(DayOfWeek.MONDAY)
    val end = if (getSettings().trackTimeOnWeekend)
        date.with(DayOfWeek.SUNDAY)
    else
        date.with(DayOfWeek.FRIDAY)
    return Week(start, end)
}

fun getMondayOfPreviousWeek(localDate: LocalDate): LocalDate = localDate.minusWeeks(1).with(DayOfWeek.MONDAY)
fun getMondayOfNextWeek(localDate: LocalDate): LocalDate = localDate.plusWeeks(1).with(DayOfWeek.MONDAY)

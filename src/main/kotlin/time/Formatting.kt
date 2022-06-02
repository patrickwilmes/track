/*
 * Copyright (c) 2022, Patrick Wilmes <patrick.wilmes@bit-lake.com>
 *
 * SPDX-License-Identifier: BSD-2-Clause
 */
package time

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun LocalDateTime?.asFormattedString(): String = if (this == null) "" else format(DateTimeFormatter.ofPattern("hh:mm:ss"))
fun LocalDateTime?.asFormattedDateString(): String = if (this == null) "" else format(
    DateTimeFormatter.ofPattern("dd.MM.yyyy"))

fun Long.secondsToFormattedString(): String {
    val clockHours = this / 3600
    val clockMinutes = (this % 3600) / 60
    val clockSeconds = this % 60
    return String.format("%02d:%02d:%02d", clockHours, clockMinutes, clockSeconds)
}

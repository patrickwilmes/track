/*
 * Copyright (c) 2022, Patrick Wilmes <patrick.wilmes@bit-lake.com>
 *
 * SPDX-License-Identifier: BSD-2-Clause
 */
package settings

data class Settings(
    val trackTimeOnWeekend: Boolean = false,
    val hoursPerWeek: Int = 40,
    val storeDataRemote: Boolean = false,
    val remoteServerAddress: String = "",
)

package com.bit.lake.trackserver.projects

import kotlinx.serialization.Serializable

@Serializable
data class Project(
    val id: String? = null,
    val name: String,
)

package com.bit.lake.trackserver.projects.db

import org.jetbrains.exposed.sql.Table

object ProjectTable : Table(name = "projects") {
    val id = varchar(name = "id", length = 64)
    val name = text(name = "name")

    override val primaryKey = PrimaryKey(id)
}
package com.example.petmatcher.data.api.organizations

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

// TODO: Pojo classes for more complex fields
@Entity(tableName = "organizations")
data class Organization(
    @PrimaryKey
    @field:Json(name = "id") val id: String,
    @field:Json(name = "name") val name: String?,
    @field:Json(name = "email") val email: String?,
    @field:Json(name = "phone") val phone: String?,
    @field:Json(name = "url") val url: String?,
    @field:Json(name = "website") val website: String?,
    @field:Json(name = "mission_statement") val missionStatement: String?)
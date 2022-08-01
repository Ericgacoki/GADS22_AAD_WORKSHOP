package com.ericg.paging3xml.data.local.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ericg.paging3xml.data.remote.dto.LocationDto
import com.ericg.paging3xml.data.remote.dto.OriginDto
import com.ericg.paging3xml.domain.model.Origin

@Entity(tableName = "characters")
data class CharacterEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val created: String,
    val episode: List<String>,
    val gender: String,
    val image: String,
    @Embedded(prefix = "location")
    val location: LocationDto,
    val name: String,
    @Embedded(prefix = "origin")
    val origin: OriginDto,
    val species: String,
    val status: String,
    val type: String,
    val url: String
)

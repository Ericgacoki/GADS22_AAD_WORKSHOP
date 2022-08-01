package com.ericg.paging3xml.data.mapper

import com.ericg.paging3xml.data.local.entity.CharacterEntity
import com.ericg.paging3xml.data.remote.dto.LocationDto
import com.ericg.paging3xml.data.remote.dto.OriginDto
import com.ericg.paging3xml.domain.model.Character
import com.ericg.paging3xml.domain.model.Location
import com.ericg.paging3xml.domain.model.Origin

internal fun CharacterEntity.toDomain(): Character {
    return Character(
        created = created,
        episode = episode,
        gender = gender,
        id = id,
        image = image,
        location = location.toDomain(),
        name = name,
        origin = origin.toDomain(),
        species = species,
        status = status,
        type = type,
        url = url
    )
}

internal fun Character.toEntity(): CharacterEntity {
    return CharacterEntity(
        created = created,
        episode = episode,
        gender = gender,
        id = id,
        image = image,
        location = location.toDto(),
        name = name,
        origin = origin.toDto(),
        species = species,
        status = status,
        type = type,
        url = url
    )
}

internal fun LocationDto.toDomain(): Location{
    return Location(
        name = locationName,
        url = locationUrl
    )
}

internal fun Location.toDto(): LocationDto{
    return LocationDto(
        locationName = name,
        locationUrl = url
    )
}

internal fun OriginDto.toDomain(): Origin{
    return Origin(
        name = originName,
        url = originUrl
    )
}

internal fun Origin.toDto(): OriginDto{
    return OriginDto(
        originName = name,
        originUrl = url
    )
}

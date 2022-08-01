package com.ericg.paging3xml.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ericg.paging3xml.data.local.dao.CharactersDao
import com.ericg.paging3xml.data.local.dao.RemoteKeyDao
import com.ericg.paging3xml.data.local.entity.CharacterEntity
import com.ericg.paging3xml.data.local.entity.RemoteKey
import com.ericg.paging3xml.data.typeconverter.DataConverter

@TypeConverters(DataConverter::class)
@Database(
    version = 2,
    entities = [CharacterEntity::class, RemoteKey::class],
    exportSchema = true
)
abstract class RickMortyDatabase : RoomDatabase() {
    abstract val charactersDao: CharactersDao
    abstract val remoteKeyDao: RemoteKeyDao
}

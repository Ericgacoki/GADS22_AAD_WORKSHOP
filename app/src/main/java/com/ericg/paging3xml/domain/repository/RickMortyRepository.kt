package com.ericg.paging3xml.domain.repository

import androidx.paging.PagingSource
import com.ericg.paging3xml.data.local.entity.CharacterEntity
import com.ericg.paging3xml.data.pagingdatasource.RickMortyRemoteMediator

interface RickMortyRepository {
    fun getRickMortyDataSource(): PagingSource<Int, CharacterEntity>
    fun getRickMortyRemoteMediator(): RickMortyRemoteMediator
}

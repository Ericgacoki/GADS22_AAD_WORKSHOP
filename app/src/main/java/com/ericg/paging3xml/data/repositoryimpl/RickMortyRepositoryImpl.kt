package com.ericg.paging3xml.data.repositoryimpl

import androidx.paging.PagingSource
import com.ericg.paging3xml.data.local.database.RickMortyDatabase
import com.ericg.paging3xml.data.local.entity.CharacterEntity
import com.ericg.paging3xml.data.pagingdatasource.RickMortyRemoteMediator
import com.ericg.paging3xml.data.remote.ApiService
import com.ericg.paging3xml.domain.repository.RickMortyRepository

class RickMortyRepositoryImpl(
    private val apiService: ApiService,
    private val database: RickMortyDatabase
) :
    RickMortyRepository {

    override fun getRickMortyDataSource(): PagingSource<Int, CharacterEntity> {
        return database.charactersDao.pagingSource()
    }

    override fun getRickMortyRemoteMediator(): RickMortyRemoteMediator {
        return RickMortyRemoteMediator(database, apiService)
    }
}

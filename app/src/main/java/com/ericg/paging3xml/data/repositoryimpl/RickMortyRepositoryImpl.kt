package com.ericg.paging3xml.data.repositoryimpl

import com.ericg.paging3xml.data.pagingdatasource.RickMortyDataSource
import com.ericg.paging3xml.data.remote.ApiService
import com.ericg.paging3xml.domain.repository.RickMortyRepository
import javax.inject.Inject

class RickMortyRepositoryImpl @Inject constructor(private val apiService: ApiService) :
    RickMortyRepository {

    override fun getCharacters(): RickMortyDataSource {
        return RickMortyDataSource(apiService)
    }
}
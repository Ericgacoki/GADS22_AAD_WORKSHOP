package com.ericg.paging3xml.domain.repository

import androidx.paging.PagingData
import com.ericg.paging3xml.data.pagingdatasource.RickMortyDataSource
import com.ericg.paging3xml.data.remote.ApiService
import com.ericg.paging3xml.domain.model.Character
import com.ericg.paging3xml.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface RickMortyRepository  {
    fun getCharacters(): RickMortyDataSource
}
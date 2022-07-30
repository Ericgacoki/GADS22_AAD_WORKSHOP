package com.ericg.paging3xml.presentation.screens.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ericg.paging3xml.data.repositoryimpl.RickMortyRepositoryImpl
import com.ericg.paging3xml.domain.model.Character
import com.ericg.paging3xml.domain.repository.RickMortyRepository
import com.ericg.paging3xml.utils.Constants.NETWORK_PAGE_SIZE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val rickMortyRepository: RickMortyRepository
) : ViewModel() {
    val characters: Flow<PagingData<Character>> = Pager(
        config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
        pagingSourceFactory = { rickMortyRepository.getCharacters() }
    ).flow.cachedIn(viewModelScope)
}

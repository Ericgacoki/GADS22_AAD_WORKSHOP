package com.ericg.paging3xml.presentation.screens.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.ericg.paging3xml.data.local.database.RickMortyDatabase
import com.ericg.paging3xml.data.mapper.toDomain
import com.ericg.paging3xml.domain.model.Character
import com.ericg.paging3xml.domain.repository.RickMortyRepository
import com.ericg.paging3xml.utils.Constants.NETWORK_PAGE_SIZE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val rickMortyRepository: RickMortyRepository,
    private val database: RickMortyDatabase
) : ViewModel() {
    private var _characters: MutableStateFlow<PagingData<Character>?> = MutableStateFlow(null)
    val characters = _characters.asStateFlow()

    @OptIn(ExperimentalPagingApi::class)
    suspend fun getCharacters() {
        Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = rickMortyRepository.getRickMortyRemoteMediator(),
            pagingSourceFactory = { rickMortyRepository.getRickMortyDataSource() }
        ).flow.cachedIn(viewModelScope).collectLatest { data ->
            _characters.value = data.map { it.toDomain() }
        }
    }
}

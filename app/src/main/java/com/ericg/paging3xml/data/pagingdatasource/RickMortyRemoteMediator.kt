package com.ericg.paging3xml.data.pagingdatasource

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.ericg.paging3xml.data.local.database.RickMortyDatabase
import com.ericg.paging3xml.data.local.entity.CharacterEntity
import com.ericg.paging3xml.data.local.entity.RemoteKey
import com.ericg.paging3xml.data.mapper.toEntity
import com.ericg.paging3xml.data.remote.ApiService
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class RickMortyRemoteMediator(
    private val database: RickMortyDatabase,
    private val apiService: ApiService
) : RemoteMediator<Int, CharacterEntity>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType, state: PagingState<Int, CharacterEntity>
    ): MediatorResult {
        val page = when (val pageKeyData = getKeyPageData(loadType, state)) {
            is MediatorResult.Success -> {
                return pageKeyData
            }
            else -> {
                pageKeyData as Int
            }
        }
        try {
            val response = apiService.getCharacters(page)
            val isEndOfList = response.results.isEmpty()

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.charactersDao.deleteAllCharacters()
                    database.remoteKeyDao.deleteAllKeys()
                }
                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (isEndOfList) null else page + 1
                val keys = response.results.map {
                    RemoteKey(id = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                database.remoteKeyDao.insertAll(keys)
                val entities = response.results.map { it.toEntity() }
                database.charactersDao.insertAllCharacters(entities)
            }
            return MediatorResult.Success(endOfPaginationReached = isEndOfList)

        } catch (e: IOException) {
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getKeyPageData(
        loadType: LoadType, state: PagingState<Int, CharacterEntity>
    ): Any {
        return when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToTheCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: 1
            }
            LoadType.APPEND -> {
                val remoteKeys = getLastRemoteKey(state)
                val nextKey = remoteKeys?.nextKey
                return nextKey ?: MediatorResult.Success(endOfPaginationReached = false)
            }
            LoadType.PREPEND -> {
                val remoteKeys = getFirstRemoteKey(state)
                val prevKey = remoteKeys?.prevKey ?: return MediatorResult.Success(
                    endOfPaginationReached = false
                )
                prevKey
            }
        }
    }

    private suspend fun getRemoteKeyClosestToTheCurrentPosition(state: PagingState<Int, CharacterEntity>): RemoteKey? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { repoId ->
                database.remoteKeyDao.remoteKeysCharacterId(repoId)
            }
        }
    }

    private suspend fun getLastRemoteKey(state: PagingState<Int, CharacterEntity>): RemoteKey? {
        return state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { character ->
                database.remoteKeyDao.remoteKeysCharacterId(character.id)
            }
    }

    private suspend fun getFirstRemoteKey(state: PagingState<Int, CharacterEntity>): RemoteKey? {
        return state.pages
            .firstOrNull() { it.data.isNotEmpty() }
            ?.data?.firstOrNull()
            ?.let { character ->
                database.remoteKeyDao.remoteKeysCharacterId(character.id)
            }
    }
}

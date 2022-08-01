package com.ericg.paging3xml.data.pagingdatasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ericg.paging3xml.data.remote.ApiService
import com.ericg.paging3xml.domain.model.Character
import com.ericg.paging3xml.utils.Constants.STARTING_PAGE
import retrofit2.HttpException

/***
 * [NOTE:] Use this Source when you need online-only mode!
 */
class RickMortyDataSource(
    private val apiService: ApiService
) : PagingSource<Int, Character>() {

    override fun getRefreshKey(state: PagingState<Int, Character>): Int? = state.anchorPosition

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Character> {
        return try {
            val page = params.key ?: STARTING_PAGE

            val response = apiService.getCharacters(page)
            val characters = response.results

            LoadResult.Page(
                data = characters,
                prevKey = if (page == STARTING_PAGE) null else page - 1,
                nextKey = if (characters.isEmpty()) null else page + 1
            )
        } catch (e: HttpException) {
            return LoadResult.Error(e)
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }
}

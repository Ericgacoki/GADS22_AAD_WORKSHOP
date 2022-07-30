package com.ericg.paging3xml.data.remote

import com.ericg.paging3xml.data.remote.dto.RickMortyResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("character")
    suspend fun getCharacters(
        @Query("page") page: Int
    ): RickMortyResponseDto
}
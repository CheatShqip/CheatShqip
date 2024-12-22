package com.cheatshqip.adapter.output

import retrofit2.http.GET
import retrofit2.http.Path

interface ShqipRESTService {
    @GET("define/{word}")
    suspend fun define(@Path("word") word: String): RESTSuggestionsResponse
}
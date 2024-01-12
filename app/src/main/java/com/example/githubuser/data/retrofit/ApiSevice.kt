package com.example.githubuser.data.retrofit

import com.example.githubuser.data.response.GithubUsersResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiSevice {
    @GET("search/{name}")
    fun getUser(
        @Path("name") name: String
    ): Call<GithubUsersResponse>
}
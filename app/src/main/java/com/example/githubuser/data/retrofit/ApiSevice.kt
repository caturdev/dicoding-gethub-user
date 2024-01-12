package com.example.githubuser.data.retrofit

import com.example.githubuser.data.response.GithubUsersResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiSevice {
    @GET("search/users")
    fun getUser(@Query("q") name: String): Call<GithubUsersResponse>
}
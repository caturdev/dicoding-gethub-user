package com.example.githubuser.data.retrofit

import com.example.githubuser.data.response.GithubUserDetailResponse
import com.example.githubuser.data.response.GithubUsersResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiSevice {

    // Method untuk mengambil data user berdasarkan querystring (username)
    @GET("search/users")
    fun getUser(@Query("q") name: String): Call<GithubUsersResponse>

    // Method untuk mengambil detail data user
    @GET("users/{username}")
    fun getUserDetail(@Path("username") username: String): Call<GithubUserDetailResponse>

}
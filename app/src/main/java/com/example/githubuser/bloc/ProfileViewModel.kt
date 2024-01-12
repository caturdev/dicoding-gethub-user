package com.example.githubuser.bloc

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuser.data.response.GithubUserDetailResponse
import com.example.githubuser.data.response.ItemsItem
import com.example.githubuser.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel : ViewModel() {

    companion object {
        private const val TAG = "ProfileViewModel"
    }

    private val _user = MutableLiveData<GithubUserDetailResponse>()
    val user: LiveData<GithubUserDetailResponse> = _user

    private val _followers = MutableLiveData<List<ItemsItem>>()
    val followers: LiveData<List<ItemsItem>> = _followers

    private val _following = MutableLiveData<List<ItemsItem>>()
    val following: LiveData<List<ItemsItem>> = _following

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getUserDetail(username: String) {
        // menampilkan loading indicator
        _isLoading.value = true

        val client = ApiConfig.getApiService().getUserDetail(username)
        client.enqueue(object : Callback<GithubUserDetailResponse> {

            override fun onResponse(
                call: Call<GithubUserDetailResponse>,
                response: Response<GithubUserDetailResponse>
            ) {
                // menyembunyikan loading indicator
                _isLoading.value = false

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _user.value = responseBody ?: null
                    }
                } else {
                    Log.e(TAG, "Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GithubUserDetailResponse>, t: Throwable) {
                // menyembunyikan loading indicator
                _isLoading.value = false
            }


        })
    }

    fun getUserFollowers(username: String) {
        // menampilkan loading indicator
        _isLoading.value = true

        val client = ApiConfig.getApiService().getUserFollowers(username)
        client.enqueue(object : Callback<List<ItemsItem>> {

            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                // menyembunyikan loading indicator
                _isLoading.value = false

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _followers.value = responseBody ?: null
                    }
                } else {
                    Log.e(TAG, "Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                // menyembunyikan loading indicator
                _isLoading.value = false
            }

        })

    }

    fun getUserFollowing(username: String) {
        // menampilkan loading indicator
        _isLoading.value = true

        val client = ApiConfig.getApiService().getUserFollowing(username)
        client.enqueue(object : Callback<List<ItemsItem>> {

            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                // menyembunyikan loading indicator
                _isLoading.value = false

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _following.value = responseBody ?: null
                    }
                } else {
                    Log.e(TAG, "Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                // menyembunyikan loading indicator
                _isLoading.value = false
            }

        })

    }
}
package com.example.githubuser.bloc

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuser.data.response.GithubUserDetailResponse
import com.example.githubuser.data.response.GithubUsersResponse
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

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    public fun getUserDetail(username: String) {
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
}
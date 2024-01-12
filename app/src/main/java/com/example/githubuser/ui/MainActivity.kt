package com.example.githubuser.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.githubuser.adapter.GithubUserListAdapter
import com.example.githubuser.data.response.GithubUsersResponse
import com.example.githubuser.data.response.ItemsItem
import com.example.githubuser.data.retrofit.ApiConfig
import com.example.githubuser.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    companion object {
        private const val TAG = "MainActivity"
        private const val USER_ID = "sidiqpermana"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val layoutManager = LinearLayoutManager(this)
        binding.rvUser.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUser.addItemDecoration(itemDecoration)

        getUsers()
    }

    private fun setUserListData(user: List<ItemsItem>) {
        val adapter = GithubUserListAdapter()
        adapter.submitList(user)
        binding.rvUser.adapter = adapter
    }

    private fun getUsers() {
        // menampilkan loading indicator
        showLoading(true)

        val client = ApiConfig.getApiService().getUser(USER_ID)
        client.enqueue(object : Callback<GithubUsersResponse> {

            // response handler untuk response
            override fun onResponse(
                call: Call<GithubUsersResponse>,
                response: Response<GithubUsersResponse>
            ) {

                // menyembunyikan loading indicator
                showLoading(false)

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        setUserListData(responseBody.items)
                    }
                } else {
                    Log.e(TAG, "Error: ${response.message()}")
                }

            }

            // response handler saat terjadi gagal koneksi
            override fun onFailure(call: Call<GithubUsersResponse>, t: Throwable) {

                // menyembunyikan loading indicator
                showLoading(false)

            }

        })
    }

    // function untuk menampilkan atau menyembunyikan loading indicator
    private fun showLoading(b: Boolean) {
        if (b) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

}
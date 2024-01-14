package com.example.githubuser.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.R
import com.example.githubuser.adapter.GithubUserListAdapter
import com.example.githubuser.bloc.MainViewModel
import com.example.githubuser.data.response.ItemsItem
import com.example.githubuser.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    private val searchResultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == SearchActivity.RESULT_CODE && result.data != null) {
            val resultValue = result.data?.getStringExtra(SearchActivity.EXTRA_SELECTED_VALUE)
            mainViewModel.getUsers(resultValue ?: "")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        mainViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[MainViewModel::class.java]

        // -------------------------------
        // Block untuk menangani list view
        // -------------------------------
        val layoutManager = LinearLayoutManager(this)
        binding.rvUser.layoutManager = layoutManager

        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUser.addItemDecoration(itemDecoration)

        mainViewModel.users.observe(this) { user -> setUserListData(user) }

        // -----
        // Listening is loading data
        //
        // block ini melakukan listening data isLoading
        // untuk menentukan apakah loading indicator ditampilkan atau tidak
        // -----
        mainViewModel.isLoading.observe(this) { isLoading -> showLoading(isLoading) }

        // -------------------------------
        // Block untuk lottie animation
        // -------------------------------
        val githubLoading = binding.githubLoading
        githubLoading.setAnimationFromUrl("https://lottie.host/f4aa2a91-160f-40bf-927a-85ca4d9f1074/HesvD4FI65.json")

        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.search_menu -> {
                    val intent = Intent(this@MainActivity, SearchActivity::class.java)
                    searchResultLauncher.launch(intent)
                    true
                }

                else -> false
            }
        }

    }

    /**
     * Set User List Data
     * ------------------
     * Function untuk menampilkan list data dengan menggunakan Recycler View
     * dengan menggunakan data list items user
     *
     *
     *
     * @param [List<ItemsItem>] - data yang akan dilakukan mapping
     * @return [Unit]
     */
    private fun setUserListData(user: List<ItemsItem>): Unit {
        val adapter = GithubUserListAdapter()
        adapter.submitList(user)
        binding.rvUser.adapter = adapter
    }

    /**
     * Show Loading
     * ------------
     * Function untuk menentukan apakah loading indicator akan ditampilkan atau tidak
     * loading indicator berada di file XML dengan ID github_loading
     *
     *
     *
     * @param [Boolean] - ketentuan apakah loading akan ditampilkan atau tidak
     * @return [Unit]
     */
    private fun showLoading(b: Boolean): Unit = if (b) {
        binding.githubLoading.visibility = View.VISIBLE
    } else {
        binding.githubLoading.visibility = View.GONE
    }

}
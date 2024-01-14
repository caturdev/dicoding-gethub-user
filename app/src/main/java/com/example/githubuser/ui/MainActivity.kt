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

        binding.emptyPlaceholder.visibility = View.GONE
        binding.emptyPlaceholderDesc.visibility = View.GONE
        binding.emptyPlaceholderDescSub.visibility = View.GONE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        // -----
        // Init Lottie Files
        //
        // block ini untuk melakukan init lottie files
        // untuk keperluan loading indicator
        // -----
        val githubLoading = binding.githubLoading
        githubLoading.setAnimationFromUrl("https://lottie.host/f4aa2a91-160f-40bf-927a-85ca4d9f1074/HesvD4FI65.json")

        // -----
        // Init Lottie Files
        //
        // block ini untuk melakukan init lottie files
        // untuk keperluan placeholder saat pertama user belum melakukan pencarian github user
        // -----
        val emptyPlaceholder = binding.emptyPlaceholder
        emptyPlaceholder.setAnimationFromUrl("https://lottie.host/17faed6a-c2bf-4130-8f6b-3702d625576b/LEJ9NRrh3p.json")

        // -----
        // Load View Model
        //
        // Section di bawah malakukan init view model
        // -----
        mainViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[MainViewModel::class.java]

        // -----
        // Init Layout Manager
        //
        // Section di bawah melakukan init layout manager
        // layout manager untuk display recycler view
        // -----
        val layoutManager = LinearLayoutManager(this)
        binding.rvUser.layoutManager = layoutManager

        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUser.addItemDecoration(itemDecoration)

        // -----
        // Listening users data
        //
        // block ini melakukan listening data users
        // untuk menentukan melakukan render ulang saat data users berubah
        // -----
        mainViewModel.users.observe(this) { user -> setUserListData(user) }

        // -----
        // Listening is loading data
        //
        // block ini melakukan listening data isLoading
        // untuk menentukan apakah loading indicator ditampilkan atau tidak
        // -----
        mainViewModel.isLoading.observe(this) { isLoading -> showLoading(isLoading) }

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

        binding.fab.setOnClickListener { view: View ->
            val intent = Intent(this@MainActivity, SearchActivity::class.java)
            searchResultLauncher.launch(intent)
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
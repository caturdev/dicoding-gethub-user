package com.example.githubuser.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.R
import com.example.githubuser.adapter.GithubUserListAdapter
import com.example.githubuser.bloc.ProfileViewModel
import com.example.githubuser.data.response.ItemsItem
import com.example.githubuser.databinding.FragmentFollowBinding

class FollowFragment : Fragment() {

    private lateinit var binding: FragmentFollowBinding

    companion object {
        const val ARG_SECTION_NUMBER = "section_number"
        const val GITHUB_USERNAME = "github_username"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_follow, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentFollowBinding.bind(view)
        super.onViewCreated(binding.root, savedInstanceState)

        val username = arguments?.getString(GITHUB_USERNAME)
        val position = arguments?.getInt(ARG_SECTION_NUMBER)

        // -----
        // Init Lottie Files
        //
        // block ini untuk melakukan init lottie files
        // untuk keperluan loading indicator
        // -----
        val githubLoading = binding.githubLoading
        githubLoading.setAnimationFromUrl("https://lottie.host/f4aa2a91-160f-40bf-927a-85ca4d9f1074/HesvD4FI65.json")

        // -----
        // Load View Model
        //
        // Section di bawah malakukan init view model
        // -----
        val profileViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[ProfileViewModel::class.java]

        // -----
        // Init Layout Manager
        //
        // Section di bawah melakukan init layout manager
        // layout manager untuk display recycler view
        // -----
        val layoutManager = LinearLayoutManager(activity)
        binding.rvFollow.layoutManager = layoutManager

        val itemDecoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)
        binding.rvFollow.addItemDecoration(itemDecoration)

        // menentukan tindakan apabila section sekarang adalah followers (index 1)
        if (position == 1) {
            profileViewModel.getUserFollowers(username ?: "")
            Log.d("XX001", "get data followers")
        }

        // menentukan tindakan apabila section sekarang adalah following (index 2)
        if (position == 2) {
            profileViewModel.getUserFollowing(username ?: "")
            Log.d("XX001", "get data following")
        }

        profileViewModel.followers.observe(viewLifecycleOwner) { users: List<ItemsItem> ->
            setUserFollow(users)
        }
        profileViewModel.following.observe(viewLifecycleOwner) { users: List<ItemsItem> ->
            setUserFollow(users)
        }

        // -----
        // Listening is loading data
        //
        // block ini melakukan listening data isLoading
        // untuk menentukan apakah loading indicator ditampilkan atau tidak
        // -----
        profileViewModel.isLoading.observe(viewLifecycleOwner) { isLoading -> showLoading(isLoading) }

    }

    /**
     * Set User Follow
     * ---------------
     * Function untuk menampilkan list data follower atau following dengan menggunakan Recycler View
     * dengan menggunakan data list items user
     *
     *
     *
     * @param [List<ItemsItem>] - data yang akan dilakukan mapping
     * @return [Unit]
     */
    private fun setUserFollow(users: List<ItemsItem>): Unit {
        val adapter = GithubUserListAdapter()
        adapter.submitList(users)
        binding.rvFollow.adapter = adapter
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
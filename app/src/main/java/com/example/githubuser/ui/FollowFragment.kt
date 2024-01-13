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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FollowFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
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

        val profileViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[ProfileViewModel::class.java]

        // -------------------------------
        // Block untuk menangani list view
        // -------------------------------
        val layoutManager = LinearLayoutManager(activity)
        binding.rvFollow.layoutManager = layoutManager

        val itemDecoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)
        binding.rvFollow.addItemDecoration(itemDecoration)

        setUserFollow(listOf(ItemsItem(login = "sidiqpermana")))
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

    }

    private fun setUserFollow(users: List<ItemsItem>) {
        val adapter = GithubUserListAdapter()
        adapter.submitList(users)
        binding.rvFollow.adapter = adapter
    }
    
}
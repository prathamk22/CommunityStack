package com.example.communitystack.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.communitystack.databinding.FragmentHomeBinding
import com.example.communitystack.ui.view_question.ViewQuestionActivity
import com.example.communitystack.util.PostUserModel
import com.example.communitystack.util.State
import com.example.communitystack.util.createViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val vm by lazy {
        createViewModel<HomeViewModel>()
    }

    private val binding get() = _binding!!

    val homePageAdapter = HomePageAdapter(object : HomeItemOnClicks{
        override fun onItemClick(item: PostUserModel) {
            startActivity(ViewQuestionActivity.instance(requireContext(), item.posts?.id))
        }

        override fun onLikeClicked(item: PostUserModel, add: Boolean) {
            vm.likePost(item.posts?.id?:"", add)
        }

        override fun onCommentClick(item: PostUserModel) {
            startActivity(ViewQuestionActivity.instance(requireContext(), item.posts?.id))
        }

    })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = homePageAdapter
        }
        vm.allPosts.observe(viewLifecycleOwner){
            when(it) {
                is State.Loading ->{
                    loadingBar.isVisible = true
                }
                is State.Success ->{
                    loadingBar.isVisible = false
                    homePageAdapter.submitList(it.data)
                }
                is State.Failed ->{
                    loadingBar.isVisible = false
                    Snackbar.make(loadingBar, it.message, Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        vm.getAllPosts()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
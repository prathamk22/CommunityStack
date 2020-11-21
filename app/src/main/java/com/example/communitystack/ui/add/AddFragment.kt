package com.example.communitystack.ui.add

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.communitystack.R
import com.example.communitystack.util.*
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_add.*

class AddFragment : Fragment() {

    val vm by lazy {
        createViewModel<AddViewModel>()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addBtn.setOnClickListener {
            if (!title.text.isNullOrBlank() && !description.text.isNullOrBlank() && vm.tagsArray.isNotEmpty()){
                vm.addPost(Posts(
                    user = PreferenceManager.instance.userUID,
                    id = randomId(),
                    title = title.text.toString(),
                    description = description.text.toString(),
                    tags = vm.tagsArray
                ))
            }else{
                Snackbar.make(addBtn, "Add all fields properly", Snackbar.LENGTH_LONG).show()
            }
        }

        vm.addPost.observe(viewLifecycleOwner){
            when(it){
                is State.Failed ->{
                    addBtn.isEnabled = true
                    Snackbar.make(addTag, it.message, Snackbar.LENGTH_LONG).show()
                }
                is State.Success ->{
                    title.text.clear()
                    description.text.clear()
                    tags.text.clear()
                    Snackbar.make(addTag, "Post Added SuccessFully", Snackbar.LENGTH_LONG).show()
                    addBtn.isEnabled = true
                }
                is State.Loading ->{
                    addBtn.isEnabled = false
                }
            }
        }

        addTag.setOnClickListener {
            if (!tags.text.isNullOrBlank()){
                if (!vm.tagsArray.contains(tags.text.toString())){
                    vm.tagsArray.add(tags.text.toString())
                    var tags = ""
                    vm.tagsArray.forEach {
                        tags += "$it,"
                    }
                    allTags.text = tags
                }
            }else{
                Snackbar.make(addBtn, "Enter proper tag", Snackbar.LENGTH_LONG).show()
            }
        }
    }
}
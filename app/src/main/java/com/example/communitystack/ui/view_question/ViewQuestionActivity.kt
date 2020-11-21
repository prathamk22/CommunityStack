package com.example.communitystack.ui.view_question

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.communitystack.R
import com.example.communitystack.util.State
import com.example.communitystack.util.createViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_view_question.*

class ViewQuestionActivity : AppCompatActivity() {

    companion object {
        fun instance(context: Context, postId: String?): Intent {
            val intent = Intent(context, ViewQuestionActivity::class.java)
            intent.putExtra("id", postId)
            return intent
        }
    }

    val vm by lazy {
        createViewModel<ViewQuestionViewModel>()
    }

    val postId by lazy {
        intent?.getStringExtra("id")
    }

    val answerAdapter = AnswerAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_question)

        vm.postId = postId?:""
        vm.getPost(vm.postId)

        answerRv.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = answerAdapter
        }

        send.setOnClickListener {
            if(answerEt.isEnabled && !answerEt.text.isNullOrBlank()){
                vm.postAns(answerEt.text.toString())
            }
        }

        vm.sendAnsLiveData.observe(this){
            when (it) {
                is State.Loading -> {
                    loadingQuestion.isVisible = true
                    answerEt.isEnabled = false
                }
                is State.Failed -> {
                    loadingQuestion.isVisible = false
                    answerEt.isEnabled = true
                    Snackbar.make(description, it.message, Snackbar.LENGTH_LONG).show()
                }
                is State.Success -> {
                    answerEt.isEnabled = true
                    loadingQuestion.isVisible = false
                    answerEt.text.clear()
                    Snackbar.make(description, "Answer Added Successfully", Snackbar.LENGTH_LONG).show()
                }
            }
        }

        vm.ansLiveData.observe(this){
            answerAdapter.submitList(it)
        }

        vm.postLiveData.observe(this) {
            when (it) {
                is State.Loading -> {
                    loadingQuestion.isVisible = true
                }
                is State.Failed -> {
                    loadingQuestion.isVisible = false
                    Snackbar.make(description, it.message, Snackbar.LENGTH_LONG).show()
                }
                is State.Success -> {
                    loadingQuestion.isVisible = false
                    setTitle(it.data.posts?.title)
                    with(it.data){
                        titleTv.text = posts?.title
                        description.text = posts?.description
                        likeTv.text = posts?.likes.toString()
                        commentTv.text = posts?.answerCount.toString()
                    }
                }
            }
        }
    }


}

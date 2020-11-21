package com.example.communitystack.ui.view_question

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.communitystack.R
import com.example.communitystack.util.AnswerUserModel
import kotlinx.android.synthetic.main.answer_item.view.*

class AnswerAdapter : ListAdapter<AnswerUserModel, AnswerViewHolder>(object : DiffUtil.ItemCallback<AnswerUserModel>(){
    override fun areItemsTheSame(oldItem: AnswerUserModel, newItem: AnswerUserModel): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: AnswerUserModel, newItem: AnswerUserModel): Boolean {
        return oldItem.answer?.id == newItem.answer?.id
    }

}) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswerViewHolder {
        return AnswerViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.answer_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: AnswerViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class AnswerViewHolder(view: View) : RecyclerView.ViewHolder(view){

    fun bind(item: AnswerUserModel) = with(itemView) {
        name.text = item.user?.name
        answerTv.text = item.answer?.answer
    }

}
package com.example.communitystack.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.communitystack.R
import com.example.communitystack.util.PostUserModel
import kotlinx.android.synthetic.main.home_page_item.view.*

class HomePageAdapter(private val onClick: HomeItemOnClicks) :
    ListAdapter<PostUserModel, HomePageViewHolder>(object : DiffUtil.ItemCallback<PostUserModel>() {
        override fun areItemsTheSame(oldItem: PostUserModel, newItem: PostUserModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: PostUserModel, newItem: PostUserModel): Boolean {
            return oldItem.posts?.id == newItem.posts?.id
        }

    }) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomePageViewHolder {
        return HomePageViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.home_page_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: HomePageViewHolder, position: Int) {
        holder.bind(getItem(position), onClick = onClick)
    }

}

class HomePageViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bind(item: PostUserModel, onClick: HomeItemOnClicks) = with(itemView) {
        setOnClickListener {
            onClick.onItemClick(item)
        }

        likeTv.setOnClickListener {
            likeTv.text = (likeTv.text.toString().toInt() + 1).toString()
            onClick.onLikeClicked(item, true)
        }

        commentTv.setOnClickListener {
            onClick.onCommentClick(item)
        }

        name.text = item.user?.name
        description.text = item.posts?.description
        title.text = item.posts?.title
        likeTv.text = item.posts?.likes.toString()
        commentTv.text = item.posts?.answerCount.toString()
    }

}

interface HomeItemOnClicks {
    fun onItemClick(item: PostUserModel)
    fun onLikeClicked(item: PostUserModel, add: Boolean)
    fun onCommentClick(item: PostUserModel)
}
package com.example.android.politicalpreparedness.election.adapter

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.network.models.Election

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Election>?) {
    val adapter = recyclerView.adapter as ElectionListAdapter
    adapter.submitList(data)
}

@BindingAdapter("isFollowingElection")
fun bindSavedStateToTextView(textView: TextView, isFollowingElection: Boolean) {
    when (isFollowingElection) {
        true -> textView.setText(R.string.unfollow_election)
        false -> textView.setText(R.string.follow_election)
    }
}


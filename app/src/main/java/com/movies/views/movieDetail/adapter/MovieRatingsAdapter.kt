package com.movies.views.movieDetail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.movies.R
import com.movies.databinding.ItemRatingsBinding
import com.movies.views.movieDetail.model.MovieRatingsModel

class MovieRatingsAdapter(
    private val mValues: List<MovieRatingsModel>,
) : RecyclerView.Adapter<MovieRatingsAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return mValues.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.mBinding.tvReview.text = mValues[position].source
        holder.mBinding.tvRatings.text = mValues[position].value
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = if (LayoutInflater.from(parent.context) == null) LayoutInflater.from(parent.context) else LayoutInflater.from(
            parent.context
        )
        val binding: ItemRatingsBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_ratings, parent, false)

        return ViewHolder(binding)
    }

    inner class ViewHolder(private val binding: ItemRatingsBinding) : RecyclerView.ViewHolder(binding.root) {
        var mBinding = binding
    }

}
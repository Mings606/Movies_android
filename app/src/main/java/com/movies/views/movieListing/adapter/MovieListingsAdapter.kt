package com.movies.views.movieListing.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.movies.R
import com.movies.databinding.ItemMovieBinding
import com.movies.views.movieListing.model.MovieListingsModel

class MovieListingsAdapter(
    private val context: Context,
    private val mValues: List<MovieListingsModel>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<MovieListingsAdapter.ViewHolder>() {

    interface OnItemClickListener {

        fun onItemClick(item: MovieListingsModel)
    }

    override fun getItemCount(): Int {
        return mValues.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.mBinding.tvTitle.text = mValues[position].title
        Glide.with(context).load(mValues[position].poster).into(holder.mBinding.imgMovie)

        holder.mBinding.root.setOnClickListener {
            buttonOneClick(it)
            listener.onItemClick(mValues[position])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater =
            if (LayoutInflater.from(parent.context) == null) LayoutInflater.from(parent.context) else LayoutInflater.from(
                parent.context
            )
        val binding: ItemMovieBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.item_movie, parent, false)

        return ViewHolder(binding)
    }

    inner class ViewHolder(private val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var mBinding = binding
    }

    /**  Button Single Click */
    private fun buttonOneClick(v: View) {
        v.isClickable = false
        v.postDelayed({ v.isClickable = true }, 800)
    }

}
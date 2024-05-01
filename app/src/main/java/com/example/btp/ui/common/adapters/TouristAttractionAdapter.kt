package com.example.btp.ui.common.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.btp.databinding.ItemDestinationExploreBinding
import com.example.btp.model.Location

class TouristAttractionAdapter:
    ListAdapter<Location, TouristAttractionAdapter.DestinationViewHolder>(REPO_COMPARATOR) {
    inner class DestinationViewHolder(private val binding: ItemDestinationExploreBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(location: Location) {
            with(binding) {
                destinationNameTextView.text = location.touristSpot
                Glide.with(root.context).load(location.imageUrl).into(destinationImageView)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DestinationViewHolder {
        val binding =
            ItemDestinationExploreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DestinationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DestinationViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<Location>() {
            override fun areItemsTheSame(oldItem: Location, newItem: Location): Boolean =
                oldItem.touristSpot == newItem.touristSpot

            override fun areContentsTheSame(oldItem: Location, newItem: Location): Boolean =
                oldItem == newItem
        }
    }
}
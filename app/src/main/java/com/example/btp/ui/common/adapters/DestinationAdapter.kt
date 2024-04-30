package com.example.btp.ui.common.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.btp.databinding.ItemDestinationBinding
import com.example.btp.model.Location

class DestinationAdapter(
    val clickListener: (List<Location>) -> Unit
) :
    ListAdapter<Location, DestinationAdapter.DestinationViewHolder>(REPO_COMPARATOR) {

    private val selectedDestinations = mutableListOf<Location>()

    inner class DestinationViewHolder(private val binding: ItemDestinationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(location: Location) {
            with(binding) {
                destinationNameTextView.text = location.touristSpot
                Glide.with(root.context).load(location.imageUrl).into(destinationImageView)
                destinationItemCard.setOnClickListener {
                    if (selectedDestinations.contains(location)) {
                        selectedDestinations.remove(location)
                    } else {
                        selectedDestinations.add(location)
                    }
                    clickListener(selectedDestinations)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DestinationViewHolder {
        val binding =
            ItemDestinationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

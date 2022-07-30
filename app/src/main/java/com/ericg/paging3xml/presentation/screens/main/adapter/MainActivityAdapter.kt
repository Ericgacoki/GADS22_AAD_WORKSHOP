package com.ericg.paging3xml.presentation.screens.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.ericg.paging3xml.R
import com.ericg.paging3xml.databinding.RickMortyItemBinding
import com.ericg.paging3xml.domain.model.Character
import java.util.*


class MainActivityAdapter :
    PagingDataAdapter<Character, MainActivityAdapter.MainViewHolder>(DIFF_CALLBACK) {

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Character>() {
            override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean =
                oldItem == newItem
        }
    }

    inner class MainViewHolder(val binding: RickMortyItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(
            RickMortyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val item = getItem(position)

        holder.binding.apply {
            image.load(item!!.image) {
                placeholder(R.drawable.ic_launcher_foreground)
                crossfade(750)
                if (item.status == "Dead") {
                    transformations(
                        // GrayScaleTransformation() fuck this 🥴
                    )
                }
                build()
            }
            tvName.text = addEllipses(item.name, 16)
            tvStatus.text = item.status
            tvLocation.text = addEllipses(item.location.name, 7)
            tvSpecies.text = addEllipses(item.species, 5)
            tvGender.text = item.gender.first().toString().uppercase(Locale.ROOT)
            tvAppearedOn.text = checkLength(item.episode.size)
        }
    }

    private fun checkLength(size: Int?): String {
        return when (size) {
            0 -> "$size Episodes"
            in 2..Int.MAX_VALUE -> "$size Episodes"
            1 -> "$size Episode"
            else -> "0 Episodes"
        }
    }

    private fun addEllipses(string: String, maxLength: Int): String {
        return when {
            string.contains("earth", true) -> "Earth"
            string.length > maxLength -> string.substring(0, maxLength) + "..."
            else -> string
        }
    }
}

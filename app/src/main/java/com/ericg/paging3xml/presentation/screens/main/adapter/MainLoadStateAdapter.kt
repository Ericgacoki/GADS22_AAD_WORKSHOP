package com.ericg.paging3xml.presentation.screens.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ericg.paging3xml.databinding.LoadStateViewBinding

class MainLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<MainLoadStateAdapter.LoadStateViewHolder>() {

    inner class LoadStateViewHolder(
        val binding: LoadStateViewBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun setRetry() {
            binding.also {
                it.btnRetry.setOnClickListener {
                    retry()
                }
            }
        }
    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.binding.apply {
            progress.isVisible = loadState is LoadState.Loading
            tvErrorMsg.isVisible = loadState is LoadState.Error
            btnRetry.isVisible = loadState is LoadState.Error

            if (loadState is LoadState.Error) {
                tvErrorMsg.text = loadState.error.localizedMessage
            }
        }
        holder.setRetry()
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        return LoadStateViewHolder(
            binding = LoadStateViewBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }
}

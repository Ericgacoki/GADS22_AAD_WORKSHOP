package com.ericg.paging3xml.presentation.screens.main.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import com.ericg.paging3xml.databinding.ActivityMainBinding
import com.ericg.paging3xml.presentation.screens.main.adapter.MainActivityAdapter
import com.ericg.paging3xml.presentation.screens.main.adapter.MainLoadStateAdapter
import com.ericg.paging3xml.presentation.screens.main.viewmodel.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainActivityViewModel by viewModels<MainActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launchWhenCreated {
            mainActivityViewModel.getCharacters()
        }
        bindData(true)
    }

    private fun bindData(isMediator: Boolean) {

        val adapter = MainActivityAdapter()

        binding.mainRecyclerView.adapter = adapter.withLoadStateFooter(
            footer = MainLoadStateAdapter(retry = { adapter.retry() })
        )
        binding.mainRecyclerView.addItemDecoration(
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        )

        adapter.addLoadStateListener { loadState ->
            val refreshState = if (isMediator){
                loadState.mediator?.refresh
            } else {
                loadState.source.refresh
            }

            binding.mainRecyclerView.isVisible = refreshState is LoadState.NotLoading
            binding.mainLoading.isVisible = refreshState is LoadState.Loading
            binding.mainRetry.isVisible = refreshState is LoadState.Error
        }

        binding.mainRetry.setOnClickListener{
            adapter.retry()
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainActivityViewModel.characters.collectLatest {
                    it?.let { adapter.submitData(it) }
                }
            }
        }
    }
}

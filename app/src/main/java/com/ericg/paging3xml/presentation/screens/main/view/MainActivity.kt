package com.ericg.paging3xml.presentation.screens.main.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bindData()
    }

    private fun bindData() {
        val mainActivityViewModel by viewModels<MainActivityViewModel>()
        val adapter = MainActivityAdapter()

        binding.mainRecyclerView.adapter = adapter.withLoadStateFooter(
            footer = MainLoadStateAdapter()
        )
        binding.mainRecyclerView.addItemDecoration(
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        )

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainActivityViewModel.characters.collectLatest {
                    adapter.submitData(it)
                }
            }
        }
    }
}

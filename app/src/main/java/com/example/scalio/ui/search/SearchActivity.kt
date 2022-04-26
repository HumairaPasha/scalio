package com.example.scalio.ui.search

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scalio.databinding.ActivitySearchBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding

    private val viewModel: SearchViewModel by viewModels()

    lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        setContentView(binding.root)
        setupLayout()
        setupObservers()
    }

    private fun setupLayout() {
        adapter = UserAdapter()
        binding.usersListView.layoutManager = LinearLayoutManager(this)
        binding.usersListView.adapter = adapter
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.users.observe(this@SearchActivity, Observer {
                adapter.submitData(lifecycle, it)
            })
        }

        lifecycleScope.launch {
            adapter.loadStateFlow.distinctUntilChanged().collectLatest {
                if (it.refresh is LoadState.NotLoading) {
                    val isListEmpty = it.refresh is LoadState.NotLoading && adapter.itemCount == 0
                    binding.usersListView.isVisible = !isListEmpty
                    binding.errorMessage.isVisible =
                        isListEmpty && binding.loginField.text.isNotEmpty()

                    val errorState = it.source.append as? LoadState.Error
                        ?: it.source.prepend as? LoadState.Error
                        ?: it.append as? LoadState.Error
                        ?: it.prepend as? LoadState.Error
                    errorState?.let {
                        Toast.makeText(
                            this@SearchActivity,
                            " Aa-oh ${it.error}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }


    }
}




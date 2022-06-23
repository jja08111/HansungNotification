package com.foundy.presentation.favorite

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.foundy.presentation.R
import com.foundy.presentation.databinding.FragmentFavoriteBinding
import com.foundy.presentation.notice.NoticeAdapter

class FavoriteFragment : Fragment(R.layout.fragment_favorite) {

    private val viewModel: FavoriteViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentFavoriteBinding.bind(view)

        binding.apply {
            recyclerView.adapter = NoticeAdapter()
            recyclerView.layoutManager = LinearLayoutManager(context)
        }

        viewModel.favoriteList.observe(viewLifecycleOwner) {
            (binding.recyclerView.adapter as NoticeAdapter).addAll(it)
        }
    }
}
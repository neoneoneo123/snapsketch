package com.project.snapsketch.presentation.ui.home

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.project.snapsketch.R
import com.project.snapsketch.databinding.FragmentHomeBinding
import com.project.snapsketch.domain.entity.ImageEntity
import com.project.snapsketch.presentation.ui.loading.LoadingDialog
import com.project.snapsketch.presentation.ui.selecting.SelectingFragment
import com.project.snapsketch.presentation.shared.DefaultEvent
import com.project.snapsketch.presentation.utils.ToastMaker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var loadingDialog: LoadingDialog? = null

    private val viewmodel: HomeViewModel by viewModels()
    private val homeAdapter: HomeAdapter by lazy {
        HomeAdapter(object : HomeAdapter.HomeItemListener {
            override fun onItemClicked(item: ImageEntity) {
                goColoring(item)
            }

            override fun onItemMenuClicked(item: ImageEntity) {
                deleteItem(item)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUi()
        setupListener()
        setupObserver()
    }

    private fun setupUi() = with(binding) {
        rvHomeList.adapter = homeAdapter
    }

    private fun setupListener() = with(binding) {
        btnHomeAlbum.setOnClickListener {
            goAlbum()
        }
    }

    private fun setupObserver() {
        lifecycleScope.launch {
            viewmodel.getImagesEvent.flowWithLifecycle(lifecycle).collectLatest { event ->
                when (event) {
                    is DefaultEvent.Failure -> ToastMaker.make(requireContext(), event.msg)
                    DefaultEvent.Success -> {}
                }
            }
        }

        lifecycleScope.launch {
            viewmodel.getImages.flowWithLifecycle(lifecycle).collectLatest { images ->
                homeAdapter.submitList(images)
            }
        }

        lifecycleScope.launch {
            viewmodel.deleteImageEvent.flowWithLifecycle(lifecycle).collectLatest { event ->
                when (event) {
                    is DefaultEvent.Failure -> ToastMaker.make(requireContext(), event.msg)
                    DefaultEvent.Success -> {}
                }
            }
        }

        lifecycleScope.launch {
            viewmodel.uiState.flowWithLifecycle(lifecycle).collectLatest { state ->
                if (state.isLoading) {
                    loadingDialog = LoadingDialog()
                    loadingDialog?.show(parentFragmentManager, null)
                } else {
                    loadingDialog?.dismiss()
                    loadingDialog = null
                }
            }
        }
    }

    private fun deleteItem(item: ImageEntity) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage(R.string.home_msg_delete_image)
        builder.setPositiveButton(R.string.common_yes) { _, _ ->
            viewmodel.deleteImage(item)
        }
        builder.setNegativeButton(R.string.common_no) { _, _ -> }
        builder.show()
    }

    private fun goAlbum() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.cl_main, SelectingFragment())
            .addToBackStack(null)
            .commit()
    }

    private fun goColoring(item: ImageEntity) {
        ToastMaker.make(requireContext(), "Image clicked")
        //TODO: 색칠 화면으로 이동
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
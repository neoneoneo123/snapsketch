package com.project.snapsketch.presentation.ui.home

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.project.snapsketch.R
import com.project.snapsketch.databinding.FragmentHomeBinding
import com.project.snapsketch.presentation.model.ImageModel
import com.project.snapsketch.presentation.util.FileUtils
import com.project.snapsketch.presentation.util.ToastMaker
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewmodel: HomeViewModel by viewModels()
    private val imageListAdapter: ImageListAdapter by lazy {
        ImageListAdapter(object : ImageListAdapter.ImageItemListener {
            override fun onItemClicked(item: ImageModel) {
                imageOpen(item)
            }
        })
    }

    private fun imageOpen(item: ImageModel) {
        ToastMaker.make(requireContext(), "Image clicked")
        //TODO: 색칠 화면으로 이동
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

        setupOpenAlbum()
        setupImageList()
    }

    private fun setupOpenAlbum() {
        val getContent =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                uri?.let {
                    goDetecting(uri)
                } ?: run {
                    ToastMaker.make(requireContext(), R.string.home_no_image)
                }
            }

        binding.btnHomeAlbum.setOnClickListener {
            getContent.launch("image/*")
        }
    }

    private fun goDetecting(uri: Uri) {
        val action = HomeFragmentDirections.actionHomeFragmentToDetectingFragment(uri.toString())
        findNavController().navigate(action)
    }

    private fun setupImageList() {
        val images = FileUtils.getImagesFromDirectory(requireContext()).reversed()
        imageListAdapter.submitList(images)
        binding.rvHomeList.adapter = imageListAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
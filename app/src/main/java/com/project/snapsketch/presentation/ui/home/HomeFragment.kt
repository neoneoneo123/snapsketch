package com.project.snapsketch.presentation.ui.home

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.project.snapsketch.databinding.FragmentHomeBinding
import com.project.snapsketch.presentation.model.ImageModel
import com.project.snapsketch.presentation.util.FileUtils
import com.project.snapsketch.presentation.util.FileUtils.convertUri
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewmodel: HomeViewModel by viewModels()
    private val imageListAdapter: ImageListAdapter by lazy {
        ImageListAdapter(object : ImageListAdapter.ImageItemListener {
            override fun onItemClicked(item: ImageModel) {
                openImage(item)
            }
        })
    }

    private fun openImage(item: ImageModel) {
        Log.d("HomeFragment", "openImage : $item")
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
                    convertUri(requireContext(), uri)
                    setupImageList()
                } ?: run {
                    Toast.makeText(requireContext(), "No image selected", Toast.LENGTH_SHORT).show()
                }
            }

        binding.btnHomeAlbum.setOnClickListener {
            getContent.launch("image/*")
        }
    }

    private fun setupImageList() {
        val images = FileUtils.getImagesFromDirectory(requireContext()).reversed()
        imageListAdapter.submitList(images)

        binding.rvHomeList.adapter = imageListAdapter
    }
}
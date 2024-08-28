package com.project.snapsketch.presentation.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.project.snapsketch.R
import com.project.snapsketch.data.local.FileUtils
import com.project.snapsketch.databinding.FragmentHomeBinding
import com.project.snapsketch.presentation.model.ImageModel
import com.project.snapsketch.presentation.ui.album.AlbumFragment
import com.project.snapsketch.presentation.utils.ToastMaker
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val imageListAdapter: ImageListAdapter by lazy {
        ImageListAdapter(object : ImageListAdapter.ImageItemListener {
            override fun onItemClicked(item: ImageModel) {
                goColoring(item)
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
    }

    private fun setupUi() {
        val images = FileUtils.getImagesFromDirectory(requireContext()).reversed()
        imageListAdapter.submitList(images)
        binding.rvHomeList.adapter = imageListAdapter
    }

    private fun setupListener() {
        binding.btnHomeAlbum.setOnClickListener {
            goAlbum()
        }
    }

    private fun goAlbum() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.cl_home_main, AlbumFragment())
            .addToBackStack(null)
            .commit()
    }

    private fun goColoring(item: ImageModel) {
        ToastMaker.make(requireContext(), "Image clicked")
        //TODO: 색칠 화면으로 이동
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
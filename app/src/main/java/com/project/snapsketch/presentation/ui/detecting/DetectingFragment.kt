package com.project.snapsketch.presentation.ui.detecting

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.project.snapsketch.databinding.FragmentDetectingBinding
import com.project.snapsketch.presentation.util.FileUtils
import dagger.hilt.android.AndroidEntryPoint
import org.opencv.android.OpenCVLoader

@AndroidEntryPoint
class DetectingFragment : Fragment() {
    private var _binding: FragmentDetectingBinding? = null
    private val binding get() = _binding!!

    private lateinit var originalImageUri: Uri
    private var th1 = 50.0
    private var th2 = 150.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!OpenCVLoader.initDebug()) {
            throw RuntimeException("OpenCV library initialization failed.")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetectingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getDataFromHome()
        setupListener()
    }

    private fun getDataFromHome() {
        val args: DetectingFragmentArgs by navArgs()
        originalImageUri = Uri.parse(args.argsUriString)
        updateImage()
    }

    private fun setupListener() = with(binding) {
        sdTh1Detecting.addOnChangeListener { _, value, _ ->
            th1 = value.toDouble()
            updateImage()
        }

        sdTh2Detecting.addOnChangeListener { _, value, _ ->
            th2 = value.toDouble()
            updateImage()
        }

        btnDetectingOut.setOnClickListener {
            val edgeBitmap = FileUtils.convertImage(requireContext(), originalImageUri, th1, th2)
            val savedFile = FileUtils.saveImageToDirectory(requireContext(), edgeBitmap)

            savedFile?.let {
                parentFragmentManager.popBackStack()
            }
        }
    }

    private fun updateImage() {
        val edgeBitmap = FileUtils.convertImage(requireContext(), originalImageUri, th1, th2)
        binding.ivItemDetecting.setImageBitmap(edgeBitmap)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
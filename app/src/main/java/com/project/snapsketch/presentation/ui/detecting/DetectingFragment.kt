package com.project.snapsketch.presentation.ui.detecting

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.project.snapsketch.databinding.FragmentDetectingBinding
import com.project.snapsketch.presentation.utils.Constants.BUNDLE_KEY_URI
import com.project.snapsketch.presentation.utils.Constants.CAMERA_PUT_NAME
import com.project.snapsketch.presentation.utils.Constants.REQUEST_KEY_FOR_DETECTING
import com.project.snapsketch.presentation.ui.loading.LoadingDialog
import com.project.snapsketch.presentation.shared.DefaultEvent
import com.project.snapsketch.presentation.utils.ToastMaker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.opencv.android.OpenCVLoader

@AndroidEntryPoint
class DetectingFragment : Fragment() {
    private var _binding: FragmentDetectingBinding? = null
    private val binding get() = _binding!!
    private var loadingDialog: LoadingDialog? = null

    private val viewmodel: DetectingViewModel by viewModels()

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

        getData()
        setupUi()
        setupListener()
        setupObserver()
    }

    private fun getData() {
        setFragmentResultListener(REQUEST_KEY_FOR_DETECTING) { _, bundle ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                val uri = bundle.getParcelable(BUNDLE_KEY_URI, Uri::class.java)

                if (uri != null) {
                    originalImageUri = uri
                }
            } else {
                val uri = bundle.getParcelable(BUNDLE_KEY_URI) as Uri?

                if (uri != null) {
                    originalImageUri = uri
                }
            }
        }

        setFragmentResultListener(CAMERA_PUT_NAME) { _, bundle ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                val uri = bundle.getParcelable(BUNDLE_KEY_URI, Uri::class.java)

                if (uri != null) {
                    originalImageUri = uri
                }
            } else {
                val uri = bundle.getParcelable(BUNDLE_KEY_URI) as Uri?

                if (uri != null) {
                    originalImageUri = uri
                }
            }
        }
    }

    private fun setupUi() = with(binding) {
        ivDetected.setImageURI(originalImageUri)
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

        ivDetectingNext.setOnClickListener {
            viewmodel.saveImage(originalImageUri, requireContext())
        }
    }

    private fun setupObserver() {
        lifecycleScope.launch {
            viewmodel.getDetectedImageEvent.flowWithLifecycle(lifecycle).collectLatest { event ->
                when (event) {
                    is DefaultEvent.Failure -> ToastMaker.make(requireContext(), event.msg)
                    DefaultEvent.Success -> {}
                }
            }
        }

        lifecycleScope.launch {
            viewmodel.getDetectedImage.flowWithLifecycle(lifecycle).collectLatest { image ->
                binding.ivDetected.setImageBitmap(image)
            }
        }

        lifecycleScope.launch {
            viewmodel.saveImageEvent.flowWithLifecycle(lifecycle).collectLatest { event ->
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

    private fun updateImage() {
        viewmodel.detectingImage(originalImageUri, th1, th2, requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
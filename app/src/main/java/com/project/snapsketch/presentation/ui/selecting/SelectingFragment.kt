package com.project.snapsketch.presentation.ui.selecting

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import com.project.snapsketch.R
import com.project.snapsketch.databinding.FragmentSelectingBinding
import com.project.snapsketch.domain.entity.ImageEntity
import com.project.snapsketch.presentation.utils.Constants.BUNDLE_KEY_URI
import com.project.snapsketch.presentation.utils.Constants.CAMERA_PUT_NAME
import com.project.snapsketch.presentation.utils.Constants.REQUEST_KEY_FOR_DETECTING
import com.project.snapsketch.presentation.ui.camera.CameraActivity
import com.project.snapsketch.presentation.ui.detecting.DetectingFragment
import com.project.snapsketch.presentation.ui.main.MainActivity
import com.project.snapsketch.presentation.utils.ToastMaker
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectingFragment : Fragment() {
    private var _binding: FragmentSelectingBinding? = null
    private val binding get() = _binding!!

    private val selectingAdapter by lazy {
        SelectingAdapter(object : SelectingAdapter.SelectingItemListener {
            override fun onItemClicked(item: ImageEntity) {
                if (item.uri != null) {
                    goDetecting(item.uri)
                } else {
                    ToastMaker.make(requireContext(), R.string.selecting_msg_fail_get_images)
                }
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUi()
        setupListener()
    }

    private fun setupUi() = with(binding) {
        rvSelecting.adapter = selectingAdapter
    }

    private fun setupListener() = with(binding) {
        ivSelectingBack.setOnClickListener {
            goBackHome()
        }

        ivSelectingCamera.setOnClickListener {
            goCamera()
        }

        ivSelectingAlbum.setOnClickListener {
            val getContent =
                registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                    uri?.let {
                        goDetecting(uri)
                    } ?: run {
                        ToastMaker.make(requireContext(), R.string.selecting_msg_no_image)
                    }
                }

            getContent.launch("image/*")
        }
    }

    private fun goBackHome() {
        parentFragmentManager.popBackStack()
    }

    private fun goCamera() {
        val startCameraForResult = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val imageUri = result.data?.getParcelableExtra<Uri>(CAMERA_PUT_NAME)
                if (imageUri != null) {
                    (activity as? MainActivity)?.goDetecting(imageUri)
                }
            }
        }

        val intent = Intent(requireContext(), CameraActivity::class.java)
        startCameraForResult.launch(intent)
    }

    private fun goDetecting(uri: Uri) {
        setFragmentResult(REQUEST_KEY_FOR_DETECTING, bundleOf(BUNDLE_KEY_URI to uri))

        parentFragmentManager.beginTransaction()
            .replace(R.id.cl_main, DetectingFragment())
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
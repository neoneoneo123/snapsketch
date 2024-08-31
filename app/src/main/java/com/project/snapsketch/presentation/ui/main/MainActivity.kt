package com.project.snapsketch.presentation.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.project.snapsketch.R
import com.project.snapsketch.databinding.ActivityMainBinding
import com.project.snapsketch.presentation.utils.Constants.CAMERA_PUT_NAME
import com.project.snapsketch.presentation.ui.detecting.DetectingFragment
import com.project.snapsketch.presentation.ui.home.HomeFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupUi()
        goHome()
    }

    private fun setupUi() {
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.clMain) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun goHome() {
        val homeFragment = HomeFragment()

        supportFragmentManager.beginTransaction()
            .replace(R.id.cl_main, homeFragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val uri = intent.getParcelableExtra(CAMERA_PUT_NAME, Uri::class.java)

            if (uri != null) {
                goDetecting(uri)
            }
        } else {
            val uri = intent.getParcelableExtra<Uri>(CAMERA_PUT_NAME)

            if (uri != null) {
                goDetecting(uri)
            }
        }
    }

    fun goDetecting(imageUri: Uri) {
        val detectingFragment = DetectingFragment().apply {
            arguments = Bundle().apply {
                putParcelable(CAMERA_PUT_NAME, imageUri)
            }
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.cl_main, detectingFragment)
            .addToBackStack(null)
            .commit()
    }
}
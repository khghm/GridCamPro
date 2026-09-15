package com.gridcam.pro.camera

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Toast
import androidx.camera.view.PreviewView
import androidx.fragment.app.Fragment
import com.gridcam.pro.R
import com.gridcam.pro.settings.AspectRatio
import com.gridcam.pro.settings.SettingsManager
import com.gridcam.pro.ui.GridOverlayView

class CameraFragment : Fragment() {
    
    private var _view: View? = null
    private val view get() = _view!!
    
    private lateinit var cameraHelper: CameraHelper
    private lateinit var settingsManager: SettingsManager
    
    private lateinit var previewView: PreviewView
    private lateinit var gridOverlay: GridOverlayView
    private lateinit var aspectRatioSpinner: Spinner
    private lateinit var btnGrid: ImageButton
    private lateinit var btnFlash: ImageButton
    private lateinit var btnCapture: ImageButton
    private lateinit var btnSwitchCamera: ImageButton
    private lateinit var btnGallery: ImageButton
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _view = inflater.inflate(R.layout.fragment_camera, container, false)
        return view
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        initViews()
        setupSettings()
        setupListeners()
        startCamera()
    }
    
    private fun initViews() {
        previewView = view.findViewById(R.id.previewView)
        gridOverlay = view.findViewById(R.id.gridOverlay)
        aspectRatioSpinner = view.findViewById(R.id.aspectRatioSpinner)
        btnGrid = view.findViewById(R.id.btnGrid)
        btnFlash = view.findViewById(R.id.btnFlash)
        btnCapture = view.findViewById(R.id.btnCapture)
        btnSwitchCamera = view.findViewById(R.id.btnSwitchCamera)
        btnGallery = view.findViewById(R.id.btnGallery)
    }
    
    private fun setupSettings() {
        settingsManager = SettingsManager()
        
        // Setup aspect ratio spinner
        val aspectRatios = AspectRatio.values()
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            aspectRatios.map { it.label }
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        aspectRatioSpinner.adapter = adapter
        
        // Set default to REELS
        val defaultPosition = aspectRatios.indexOfFirst { it == settingsManager.aspectRatio }
        if (defaultPosition >= 0) {
            aspectRatioSpinner.setSelection(defaultPosition)
        }
    }
    
    private fun setupListeners() {
        // Grid toggle
        btnGrid.setOnClickListener {
            val isVisible = settingsManager.toggleGrid()
            gridOverlay.isVisibleGrid = isVisible
        }
        
        // Flash toggle
        btnFlash.setOnClickListener {
            val isEnabled = cameraHelper.toggleFlash()
            settingsManager.flashEnabled = isEnabled
            updateFlashIcon(isEnabled)
        }
        
        // Capture button (photo or video)
        btnCapture.setOnClickListener {
            if (cameraHelper.isRecordingVideo()) {
                stopVideoRecording()
            } else {
                startPhotoOrVideoCapture()
            }
        }
        
        // Switch camera
        btnSwitchCamera.setOnClickListener {
            cameraHelper.switchCamera()
        }
        
        // Gallery (placeholder)
        btnGallery.setOnClickListener {
            Toast.makeText(requireContext(), "گالری به زودی", Toast.LENGTH_SHORT).show()
        }
        
        // Aspect ratio change
        aspectRatioSpinner.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: android.widget.AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedRatio = AspectRatio.values()[position]
                settingsManager.setAspectRatio(selectedRatio)
                cameraHelper.setAspectRatio(selectedRatio)
            }
            
            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {
                // Do nothing
            }
        }
    }
    
    private fun startPhotoOrVideoCapture() {
        // For now, just capture photo
        // Can be extended to support long-press for video
        cameraHelper.capturePhoto(
            onSuccess = { message ->
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            },
            onError = { error ->
                Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show()
            }
        )
    }
    
    private fun startVideoRecording() {
        btnCapture.setImageResource(android.R.drawable.ic_media_pause)
        
        cameraHelper.startVideoRecording(
            onStart = {
                // Recording started
            },
            onStop = { path ->
                btnCapture.setImageResource(android.R.drawable.ic_menu_camera)
                if (path != null) {
                    Toast.makeText(requireContext(), "ویدیو ذخیره شد", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }
    
    private fun stopVideoRecording() {
        cameraHelper.stopVideoRecording()
        btnCapture.setImageResource(android.R.drawable.ic_menu_camera)
    }
    
    private fun updateFlashIcon(isEnabled: Boolean) {
        // Update flash button icon based on state
        btnFlash.alpha = if (isEnabled) 1.0f else 0.5f
    }
    
    private fun startCamera() {
        cameraHelper = CameraHelper(
            activity = requireActivity(),
            lifecycleOwner = viewLifecycleOwner,
            previewView = previewView
        )
        cameraHelper.startCamera()
    }
    
    override fun onResume() {
        super.onResume()
        // Ensure grid is visible
        gridOverlay.isVisibleGrid = settingsManager.gridVisible
    }
    
    override fun onPause() {
        super.onPause()
        // Stop recording if active
        if (cameraHelper.isRecordingVideo()) {
            stopVideoRecording()
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        cameraHelper.release()
        _view = null
    }
}

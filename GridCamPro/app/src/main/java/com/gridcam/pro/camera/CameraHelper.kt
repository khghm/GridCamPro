package com.gridcam.pro.camera

import android.annotation.SuppressLint
import android.content.ContentValues
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.OrientationEventListener
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import com.gridcam.pro.settings.AspectRatio
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraHelper(
    private val activity: FragmentActivity,
    private val lifecycleOwner: LifecycleOwner,
    private val previewView: androidx.camera.view.PreviewView
) {
    
    private var cameraProvider: ProcessCameraProvider? = null
    private var preview: Preview? = null
    private var imageCapture: ImageCapture? = null
    private var videoCapture: Recording? = null
    private var camera: Camera? = null
    
    private val cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()
    private val handler = Handler(Looper.getMainLooper())
    
    private var isRecording = false
    private var currentAspectRatio = AspectRatio.REELS
    
    private val orientationEventListener: OrientationEventListener by lazy {
        object : OrientationEventListener(activity.applicationContext) {
            override fun onOrientationChanged(orientation: Int) {
                if (orientation == ORIENTATION_UNKNOWN) return
                // Update rotation for captures
            }
        }
    }
    
    @SuppressLint("UnsafeOptInUsageError")
    fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(activity)
        
        cameraProviderFuture.addListener({
            try {
                cameraProvider = cameraProviderFuture.get()
                bindCameraUseCases()
            } catch (e: Exception) {
                Log.e(TAG, "Camera initialization failed", e)
            }
        }, ContextCompat.getMainExecutor(activity))
    }
    
    @SuppressLint("UnsafeOptInUsageError")
    private fun bindCameraUseCases() {
        val cameraProvider = cameraProvider ?: return
        
        // Preview use case
        preview = Preview.Builder()
            .build()
            .also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }
        
        // Image Capture use case
        imageCapture = ImageCapture.Builder()
            .setTargetAspectRatio(getTargetAspectRatio(currentAspectRatio))
            .build()
        
        // Video Capture use case
        val videoCaptureBuilder = VideoCapture.withOutput(
            FileOutputOptions.Builder(
                FileOutputOptions.Builder(
                    ContentValues().apply {
                        put(MediaStore.Video.Media.DISPLAY_NAME, "VID_" + System.currentTimeMillis())
                    }
                ).build()
            ).builder()
        )
        
        // Unbind all use cases before rebinding
        cameraProvider.unbindAll()
        
        try {
            // Select back camera as default
            camera = cameraProvider.bindToLifecycle(
                lifecycleOwner,
                CameraSelector.DEFAULT_BACK_CAMERA,
                preview,
                imageCapture
            )
        } catch (e: Exception) {
            Log.e(TAG, "Camera binding failed", e)
        }
    }
    
    @SuppressLint("UnsafeOptInUsageError")
    fun switchCamera() {
        val cameraProvider = cameraProvider ?: return
        val newCameraSelector = if (camera?.cameraInfo?.lensFacing == CameraSelector.LENS_FACING_BACK) {
            CameraSelector.DEFAULT_FRONT_CAMERA
        } else {
            CameraSelector.DEFAULT_BACK_CAMERA
        }
        
        cameraProvider.unbindAll()
        
        try {
            camera = cameraProvider.bindToLifecycle(
                lifecycleOwner,
                newCameraSelector,
                preview,
                imageCapture
            )
        } catch (e: Exception) {
            Log.e(TAG, "Camera switch failed", e)
        }
    }
    
    @SuppressLint("UnsafeOptInUsageError")
    fun capturePhoto(onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        val imageCapture = imageCapture ?: return
        
        val photoFile = File(
            activity.getExternalFilesDir(null),
            SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.US).format(Date()) + ".jpg"
        )
        
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(activity),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    onSuccess("عکس ذخیره شد: ${photoFile.name}")
                }
                
                override fun onError(exception: ImageCaptureException) {
                    onError("خطا در عکس‌برداری: ${exception.message}")
                }
            }
        )
    }
    
    @SuppressLint("UnsafeOptInUsageError")
    fun startVideoRecording(onStart: () -> Unit, onStop: (String?) -> Unit) {
        if (isRecording) return
        
        val videoCapture = VideoCapture.withOutput(
            FileOutputOptions.Builder(
                File(
                    activity.getExternalFilesDir(null),
                    "VID_" + SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.US).format(Date()) + ".mp4"
                )
            ).build()
        )
        
        cameraProvider?.unbind(imageCapture)
        
        try {
            camera = cameraProvider?.bindToLifecycle(
                lifecycleOwner,
                if (camera?.cameraInfo?.lensFacing == CameraSelector.LENS_FACING_BACK) 
                    CameraSelector.DEFAULT_BACK_CAMERA 
                else 
                    CameraSelector.DEFAULT_FRONT_CAMERA,
                preview,
                videoCapture
            )
            
            val recording = videoCapture.output
                .prepareRecording(activity, ContentValues().apply {
                    put(MediaStore.Video.Media.DISPLAY_NAME, "VID_" + System.currentTimeMillis())
                    put(MediaStore.Video.Media.MIME_TYPE, "video/mp4")
                })
                .start(ContextCompat.getMainExecutor(activity)) { event ->
                    when (event) {
                        is VideoRecordEvent.Start -> {
                            isRecording = true
                            onStart()
                        }
                        is VideoRecordEvent.Finish -> {
                            isRecording = false
                            onStop(event.outputResults.outputUri.toString())
                            // Rebind image capture after video
                            bindCameraUseCases()
                        }
                        is VideoRecordEvent.Error -> {
                            isRecording = false
                            onStop("خطا: ${event.error}")
                            bindCameraUseCases()
                        }
                    }
                }
            
            this.videoCapture = recording
        } catch (e: Exception) {
            Log.e(TAG, "Video recording failed", e)
            onStop("خطا در شروع فیلم‌برداری")
        }
    }
    
    fun stopVideoRecording() {
        videoCapture?.stop()
        videoCapture = null
    }
    
    fun setAspectRatio(ratio: AspectRatio) {
        currentAspectRatio = ratio
        // Rebind with new aspect ratio
        bindCameraUseCases()
    }
    
    fun toggleFlash(): Boolean {
        val camera = camera ?: return false
        val hasFlash = camera.cameraInfo.hasFlashUnit()
        
        if (!hasFlash) return false
        
        val newFlashMode = if (camera.cameraInfo.flashState == FlashMode.ON) {
            FlashMode.OFF
        } else {
            FlashMode.ON
        }
        
        camera.cameraControl.enableTorch(newFlashMode == FlashMode.ON)
        return newFlashMode == FlashMode.ON
    }
    
    fun isRecordingVideo(): Boolean = isRecording
    
    private fun getTargetAspectRatio(ratio: AspectRatio): Int {
        return when (ratio) {
            AspectRatio.SQUARE -> AspectRatio.RATIO_1_1
            AspectRatio.REELS -> AspectRatio.RATIO_9_16
            AspectRatio.PORTRAIT -> AspectRatio.RATIO_4_5
            AspectRatio.LANDSCAPE -> AspectRatio.RATIO_16_9
        }
    }
    
    fun release() {
        cameraProvider?.unbindAll()
        cameraExecutor.shutdown()
        orientationEventListener.disable()
    }
    
    companion object {
        private const val TAG = "CameraHelper"
    }
}

package com.gridcam.pro.settings

/**
 * Aspect ratio options for capture
 */
enum class AspectRatio(val value: Float, val label: String) {
    SQUARE(1f, "۱:۱ پست"),
    REELS(9f/16f, "۹:۱۶ ریلز/استوری"),
    PORTRAIT(4f/5f, "۴:۵ پست عمودی"),
    LANDSCAPE(16f/9f, "۱۶:۹ افقی")
}

/**
 * Camera settings manager
 */
class SettingsManager {
    
    var aspectRatio: AspectRatio = AspectRatio.REELS
        private set
    
    var flashEnabled: Boolean = false
    var timerSeconds: Int = 0
    var gridVisible: Boolean = true
    
    fun setAspectRatio(ratio: AspectRatio) {
        aspectRatio = ratio
    }
    
    fun toggleFlash(): Boolean {
        flashEnabled = !flashEnabled
        return flashEnabled
    }
    
    fun setTimer(seconds: Int) {
        timerSeconds = seconds
    }
    
    fun toggleGrid(): Boolean {
        gridVisible = !gridVisible
        return gridVisible
    }
}

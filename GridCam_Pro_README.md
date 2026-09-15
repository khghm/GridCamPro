# GridCam Pro - Android Camera App

A simple Android camera app with grid overlays for perfect framing of photos and videos for Telegram posts, reels, and stories.

## Features
- Photo and video capture
- Grid overlay visible before and during recording (not in final output)
- Aspect ratios: 1:1 (Post), 9:16 (Reels/Stories), 4:5, 16:9
- Simple settings: flash, timer, resolution
- Built with Kotlin and CameraX

## Quick Start

### Prerequisites
- Android Studio Arctic Fox or later
- Android SDK 21+
- Kotlin 1.6+

### Project Structure
```
app/
├── src/main/
│   ├── java/com/gridcam/pro/
│   │   ├── MainActivity.kt
│   │   ├── camera/
│   │   │   ├── CameraHelper.kt
│   │   │   └── CameraFragment.kt
│   │   ├── ui/
│   │   │   └── GridOverlayView.kt
│   │   └── settings/
│   │       └── SettingsManager.kt
│   ├── res/
│   │   ├── layout/activity_main.xml
│   │   ├── layout/fragment_camera.xml
│   │   └── values/strings.xml
│   └── AndroidManifest.xml
├── build.gradle
└── settings.gradle
```

### Setup Instructions

1. Create new Android project with Empty Activity
2. Add dependencies to build.gradle:
```gradle
dependencies {
    implementation 'androidx.core:core-ktx:1.12.0'
    implementation 'androidx.appcompat:appcompat:1.6.1'
    implementation 'com.google.android.material:material:1.11.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
    implementation 'androidx.camera:camera-core:1.3.0'
    implementation 'androidx.camera:camera-camera2:1.3.0'
    implementation 'androidx.camera:camera-lifecycle:1.3.0'
    implementation 'androidx.camera:camera-video:1.3.0'
    implementation 'androidx.lifecycle:lifecycle-runtime-ktx:2.7.0'
}
```

3. Add permissions to AndroidManifest.xml:
```xml
<uses-permission android:name="android.permission.CAMERA" />
<uses-permission android:name="android.permission.RECORD_AUDIO" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" 
    android:maxSdkVersion="28" />
```

4. Implement MainActivity with CameraFragment
5. Create GridOverlayView for drawing grid lines
6. Implement CameraHelper using CameraX API
7. Add settings UI for aspect ratio and other options

## Key Implementation Details

### Grid Overlay
- Custom View that draws 3x3 grid lines
- Visible during preview and recording
- Not included in final photo/video output
- Toggle visibility option

### Aspect Ratios
- 1:1 for square posts
- 9:16 for reels/stories  
- 4:5 for portrait posts
- 16:9 for landscape

### Camera Functions
- Capture photo with current aspect ratio
- Record video with selected dimensions
- Switch between front/back cameras
- Flash control
- Timer functionality

## Build & Run
1. Open project in Android Studio
2. Sync Gradle files
3. Connect Android device or start emulator
4. Run app

## Next Steps
- Implement photo capture
- Implement video recording
- Add settings screen
- Test on multiple devices
- Optimize performance

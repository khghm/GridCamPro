# سند طراحی اپلیکیشن دوربین اندروید با گرید راهنما

## 📱 مشخصات کلی پروژه

### نام پروژه: GridCam Pro
### هدف: ساخت اپلیکیشن دوربین ساده با قابلیت نمایش گرید در حین عکسبرداری و فیلمبرداری
### پلتفرم: Android
### روش توسعه: اقتصاد فنی با استفاده از کدهای آماده و کتابخانه‌های موجود

---

## 🎯 ویژگی‌های اصلی

### 1. قابلیت‌های عکسبرداری
- عکسبرداری با نسبت‌های ابعاد مختلف:
  - **پست مربعی (1:1)** - 1080x1080 پیکسل
  - **پست عمودی (4:5)** - 1080x1350 پیکسل  
  - **استوری/ریلز (9:16)** - 1080x1920 پیکسل
  - **استاندارد (3:4)** - 1080x1440 پیکسل

### 2. قابلیت‌های فیلمبرداری
- فیلمبرداری با نسبت‌های ابعاد مختلف:
  - **ریلز/استوری (9:16)** - 1080x1920، حداکثر 60 ثانیه
  - **پست مربعی (1:1)** - 1080x1080، حداکثر 60 ثانیه
  - **پست عمودی (4:5)** - 1080x1350، حداکثر 60 ثانیه
  - **استاندارد (3:4)** - 1080x1440، حداکثر 60 ثانیه

### 3. سیستم گرید هوشمند
- **نمایش گرید قبل از عکسبرداری/فیلمبرداری**
- **نمایش گرید در حین عکسبرداری/فیلمبرداری**
- **عدم نمایش گرید در خروجی نهایی**
- انواع گرید:
  - قانون یک‌سوم (Rule of Thirds)
  - گرید متقارن
  - گرید طلایی (Golden Ratio)
  - خطوط افقی و عمودی راهنما

---

## 🏗️ معماری فنی

### تکنولوژی‌های پیشنهادی

#### گزینه 1: Kotlin + CameraX (توصیه شده ⭐)
```
زبان: Kotlin
کتابخانه دوربین: Android Jetpack CameraX
رابط کاربری: XML Layouts یا Jetpack Compose
حداقل API: 21 (Android 5.0)
هدف API: 34 (Android 14)
```

**مزایا:**
- پشتیبانی رسمی گوگل
- کدهای آماده زیاد
- مستندات کامل
- سازگاری با دستگاه‌های مختلف
- توسعه سریع

#### گزینه 2: Flutter + camera_plugin
```
فریم‌ورک: Flutter
زبان: Dart
پلاگین دوربین: camera ^0.10.5
رابط کاربری: Flutter Widgets
```

**مزایا:**
- کدنویسی یکباره برای اندروید و iOS
- توسعه بسیار سریع
- ویجت‌های آماده زیاد

---

## 📐 ساختار پروژه

### دایرکتوری‌بندی پیشنهادی (CameraX)

```
app/
├── src/
│   ├── main/
│   │   ├── java/com/gridcam/pro/
│   │   │   ├── MainActivity.kt
│   │   │   ├── camera/
│   │   │   │   ├── CameraManager.kt
│   │   │   │   ├── CameraPreview.kt
│   │   │   │   └── GridOverlay.kt
│   │   │   ├── ui/
│   │   │   │   ├── PreviewActivity.kt
│   │   │   │   ├── SettingsActivity.kt
│   │   │   │   └── components/
│   │   │   │       ├── GridTypeSelector.kt
│   │   │   │       ├── AspectRatioSelector.kt
│   │   │   │       └── CaptureButton.kt
│   │   │   ├── model/
│   │   │   │   ├── GridType.kt
│   │   │   │   └── AspectRatio.kt
│   │   │   └── utils/
│   │   │       ├── MediaStoreHelper.kt
│   │   │       └── PermissionHelper.kt
│   │   ├── res/
│   │   │   ├── layout/
│   │   │   │   ├── activity_main.xml
│   │   │   │   └── view_grid_overlay.xml
│   │   │   ├── drawable/
│   │   │   ├── values/
│   │   │   │   ├── strings.xml
│   │   │   │   ├── colors.xml
│   │   │   │   └── dimens.xml
│   │   │   └── menu/
│   │   └── AndroidManifest.xml
│   └── test/
├── build.gradle.kts
└── settings.gradle.kts
```

---

## 🔧 کامپوننت‌های کلیدی

### 1. CameraManager.kt
```kotlin
// مدیریت دوربین با CameraX
// - راه‌اندازی دوربین
// - تنظیم نسبت ابعاد
// - کنترل زوم
// - مدیریت فلش
```

### 2. GridOverlay.kt
```kotlin
// Overlay سفارشی برای نمایش گرید
// - رسم خطوط گرید بر اساس نوع انتخاب شده
// - شفافیت قابل تنظیم
// - رنگ قابل تغییر
// - عدم تاثیر روی خروجی نهایی
```

### 3. AspectRatioSelector.kt
```kotlin
// انتخابگر نسبت ابعاد
// - 1:1 (پست مربعی)
// - 4:5 (پست عمودی)
// - 9:16 (ریلز/استوری)
// - 3:4 (استاندارد)
```

### 4. MediaStoreHelper.kt
```kotlin
// ذخیره‌سازی عکس و ویدیو
// - مدیریت مجوزها
// - ذخیره در گالری
// - متادیتای فایل
```

---

## 🎨 رابط کاربری

### صفحه اصلی (Camera Preview)
```
┌─────────────────────────┐
│ [Settings]  [Grid] [🔄]│ ← نوار بالا
│                         │
│                         │
│    ┌───────────────┐    │
│    │               │    │
│    │   Camera      │    │
│    │   Preview     │    │
│    │   + Grid      │    │
│    │   Overlay     │    │
│    │               │    │
│    └───────────────┘    │
│                         │
│  [📸]  [●REC]  [🎬]    │ ← دکمه‌های پایین
│                         │
│ [1:1][4:5][9:16][3:4]  │ ← انتخاب نسبت
└─────────────────────────┘
```

### منوی تنظیمات گرید
- نوع گرید (قانون یک‌سوم، متقارن، طلایی)
- رنگ خطوط (سفید، سیاه، زرد)
- ضخامت خطوط
- شفافیت گرید
- نمایش/مخفی کردن گرید

---

## 📊 جریان کار (Workflow)

### 1. راه‌اندازی اولیه
```
1. بررسی مجوزهای دوربین و ذخیره‌سازی
2. راه‌اندازی CameraX
3. تنظیم نسبت ابعاد پیش‌فرض (9:16)
4. نمایش گرید پیش‌فرض (قانون یک‌سوم)
```

### 2. عکسبرداری
```
1. کاربر نسبت ابعاد را انتخاب می‌کند
2. کاربر نوع گرید را انتخاب می‌کند
3. کاربر سوژه را در گرید تنظیم می‌کند
4. دکمه عکسبرداری فشرده می‌شود
5. عکس با نسبت انتخاب شده ذخیره می‌شود
6. گرید در عکس نهایی وجود ندارد
```

### 3. فیلمبرداری
```
1. کاربر نسبت ابعاد را انتخاب می‌کند
2. کاربر نوع گرید را انتخاب می‌کند
3. دکمه فیلمبرداری فشرده می‌شود
4. گرید در حین فیلمبرداری نمایش داده می‌شود
5. کاربر می‌تواند سوژه را در گرید تنظیم کند
6. پس از پایان، ویدیو بدون گرید ذخیره می‌شود
7. تایمر حداکثر 60 ثانیه
```

---

## 🛠️ پیاده‌سازی سریع با کدهای آماده

### منابع کد آماده پیشنهادی

#### 1. CameraX Sample Code (رسمی گوگل)
- Repository: https://github.com/android/camera-samples
- نمونه کدهای کامل عکسبرداری و فیلمبرداری
- لایسنس: Apache 2.0

#### 2. Grid Overlay Implementation
```kotlin
// کد نمونه GridOverlay
class GridOverlay @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {
    
    private val paint = Paint().apply {
        color = Color.WHITE
        strokeWidth = 2f
        style = Paint.Style.STROKE
        alpha = 180
    }
    
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        
        val width = width.toFloat()
        val height = height.toFloat()
        
        // قانون یک‌سوم
        canvas.drawLine(width / 3, 0f, width / 3, height, paint)
        canvas.drawLine(width * 2 / 3, 0f, width * 2 / 3, height, paint)
        canvas.drawLine(0f, height / 3, width, height / 3, paint)
        canvas.drawLine(0f, height * 2 / 3, width, height * 2 / 3, paint)
    }
}
```

#### 3. Aspect Ratio Calculator
```kotlin
object AspectRatioUtil {
    data class Ratio(val width: Int, val height: Int)
    
    val SQUARE = Ratio(1080, 1080)      // 1:1
    val PORTRAIT = Ratio(1080, 1350)    // 4:5
    val STORY = Ratio(1080, 1920)       // 9:16
    val STANDARD = Ratio(1080, 1440)    // 3:4
    
    fun getCropRect(previewWidth: Int, previewHeight: Int, ratio: Ratio): Rect {
        // محاسبه مستطیل برش بر اساس نسبت انتخاب شده
    }
}
```

---

## 📦 وابستگی‌های پروژه (build.gradle)

```kotlin
dependencies {
    // CameraX
    implementation "androidx.camera:camera-core:1.3.1"
    implementation "androidx.camera:camera-camera2:1.3.1"
    implementation "androidx.camera:camera-lifecycle:1.3.1"
    implementation "androidx.camera:camera-video:1.3.1"
    implementation "androidx.camera:camera-view:1.3.1"
    
    // Lifecycle
    implementation "androidx.lifecycle:lifecycle-runtime-ktx:2.7.0"
    
    // Coroutines
    implementation "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3"
    
    // Material Design
    implementation "com.google.android.material:material:1.11.0"
    
    // ConstraintLayout
    implementation "androidx.constraintlayout:constraintlayout:2.1.4"
}
```

---

## ⚡ نقشه راه توسعه سریع

### هفته 1: راه‌اندازی پایه
- [ ] ایجاد پروژه Android با Kotlin
- [ ] تنظیم CameraX
- [ ] پیاده‌سازی پیش‌نمایش دوربین
- [ ] اضافه کردن مجوزها

### هفته 2: سیستم گرید
- [ ] ایجاد GridOverlay سفارشی
- [ ] پیاده‌سازی انواع گرید
- [ ] اضافه کردن تنظیمات گرید
- [ ] تست روی دستگاه‌های مختلف

### هفته 3: عکسبرداری
- [ ] پیاده‌گیری عکسبرداری با CameraX
- [ ] اضافه کردن انتخاب نسبت ابعاد
- [ ] ذخیره عکس با کیفیت مناسب
- [ ] تست عملکرد

### هفته 4: فیلمبرداری
- [ ] پیاده‌سازی فیلمبرداری با CameraX Video
- [ ] نمایش گرید در حین فیلمبرداری
- [ ] محدودیت زمانی 60 ثانیه
- [ ] ذخیره ویدیو با کیفیت مناسب

### هفته 5: بهینه‌سازی
- [ ] رفع باگ‌ها
- [ ] بهینه‌سازی عملکرد
- [ ] تست نهایی
- [ ] آماده‌سازی برای انتشار

---

## 🎯 معیارهای موفقیت

### функциональность
- ✅ عکسبرداری با 4 نسبت ابعاد مختلف
- ✅ فیلمبرداری با 4 نسبت ابعاد مختلف
- ✅ نمایش گرید قبل و در حین ضبط
- ✅ عدم نمایش گرید در خروجی نهایی
- ✅ حداکثر 60 ثانیه فیلمبرداری

### Performance
- ✅ زمان راه‌اندازی دوربین < 2 ثانیه
- ✅ نرخ فریم پایدار 30 FPS
- ✅ مصرف باتری بهینه
- ✅ حافظه موقت کمتر از 100MB

### User Experience
- ✅ رابط کاربری ساده و intuitive
- ✅ تغییر سریع بین حالت‌ها
- ✅ بازخورد بصری مناسب
- ✅ بدون تاخیر در ضبط

---

## 💰 برآورد هزینه و زمان

### روش توسعه با CameraX (توصیه شده)
- **زمان توسعه:** 4-5 هفته
- **هزینه تقریبی:** 50-80 ساعت کاری
- **منابع مورد نیاز:** 1 توسعه‌دهنده Android
- **هزینه زیرساخت:** رایگان (استفاده از ابزارهای رایگان گوگل)

### روش توسعه با Flutter
- **زمان توسعه:** 3-4 هفته
- **هزینه تقریبی:** 40-60 ساعت کاری
- **منابع مورد نیاز:** 1 توسعه‌دهنده Flutter
- **هزینه زیرساخت:** رایگان

---

## 🚀 نکات کلیدی برای موفقیت

1. **استفاده از CameraX**: بهترین کتابخانه رسمی با پشتیبانی عالی
2. **کدهای آماده گوگل**: استفاده از sample codeهای رسمی
3. **تست روی دستگاه‌های واقعی**: اهمیت بالای تست روی سخت‌افزار مختلف
4. **بهینه‌سازی زودهنگام**: توجه به performance از ابتدا
5. **رابط کاربری ساده**: تمرکز بر سادگی و کارایی

---

## 📞 منابع کمکی

### مستندات رسمی
- CameraX Documentation: https://developer.android.com/training/camerax
- Android Developer Guide: https://developer.android.com/guide

### نمونه کدها
- GitHub Camera Samples: https://github.com/android/camera-samples
- Android Kotlin Samples: https://github.com/android/kotlin-samples

### انجمن‌ها
- Stack Overflow: تگ‌های android-camera, camerax
- Reddit: r/androiddev
- Discord: Android Developers Community

---

## ✨ نتیجه‌گیری

این پروژه با استفاده از CameraX و کدهای آماده گوگل، به سرعت و با کمترین هزینه قابل توسعه است. تمرکز بر سادگی، کارایی و تجربه کاربری عالی، کلید موفقیت این اپلیکیشن خواهد بود.

**زمان تخمینی کل:** 4-5 هفته
**هزینه تخمینی:** 50-80 ساعت کاری
**سطح دشواری:** متوسط (با استفاده از کدهای آماده)

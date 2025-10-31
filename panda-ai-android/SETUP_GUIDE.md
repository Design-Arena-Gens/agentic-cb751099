# 🚀 Quick Setup Guide - Panda AI by Max

## Step-by-Step Instructions

### 1️⃣ Download Android Studio
If you don't have Android Studio installed:
- Download from: https://developer.android.com/studio
- Install and follow the setup wizard
- Install Android SDK (API 24 or higher)

### 2️⃣ Open the Project
1. Launch Android Studio
2. Click **"Open an Existing Project"**
3. Navigate to the `panda-ai-android` folder
4. Click **"OK"**
5. Wait for Gradle to sync (this may take a few minutes)

### 3️⃣ Get Your OpenAI API Key
1. Visit: https://platform.openai.com/api-keys
2. Sign up or log in to your OpenAI account
3. Click **"Create new secret key"**
4. Copy the key (starts with `sk-proj-...` or `sk-...`)
5. Save it securely - you'll need it next!

### 4️⃣ Add Your API Key
1. In Android Studio, open: `app/src/main/java/com/max/pandaai/AIService.kt`
2. Find line 18 (around the top of the file):
   ```kotlin
   private const val API_KEY = "sk-YOUR-OPENAI-API-KEY-HERE"
   ```
3. Replace the placeholder with your actual key:
   ```kotlin
   private const val API_KEY = "sk-proj-abc123xyz..." // Your real key
   ```
4. Save the file (Ctrl+S or Cmd+S)

### 5️⃣ Build the Project
1. Click **"Build"** in the menu bar
2. Select **"Make Project"** (or press Ctrl+F9 / Cmd+F9)
3. Wait for the build to complete
4. Check the "Build" tab at the bottom - should say "BUILD SUCCESSFUL"

### 6️⃣ Run on a Device

#### Option A: Real Android Device
1. Enable Developer Options on your phone:
   - Go to Settings → About Phone
   - Tap "Build Number" 7 times
   - Go back to Settings → Developer Options
   - Enable "USB Debugging"
2. Connect phone to computer via USB
3. Accept "Allow USB Debugging" prompt on phone
4. In Android Studio, select your device from dropdown
5. Click the green ▶️ "Run" button

#### Option B: Android Emulator
1. Click **"Device Manager"** in Android Studio (phone icon)
2. Click **"Create Device"**
3. Select a phone (e.g., Pixel 6)
4. Select a system image (e.g., API 34)
5. Click **"Finish"** and wait for it to download
6. Start the emulator
7. Click the green ▶️ "Run" button

### 7️⃣ First Launch
1. App will show splash screen with Panda emoji 🐼
2. Grant microphone permission when prompted
3. You'll see a welcome message from Panda AI
4. Tap the microphone button and speak, or type a message!

## 🎤 Try These Commands

Once the app is running, try saying:

- **"Open YouTube"** - Opens YouTube app
- **"Search for cute cats"** - Google search
- **"What time is it?"** - Shows current time
- **"Open camera"** - Opens camera app
- **"Tell me a joke"** - AI responds with humor
- **"What's the weather?"** - AI provides info

## ⚙️ App Settings

Tap the menu (⋮) in top right:
- **Clear Chat** - Delete all messages
- **Settings** - Customize the app
  - Toggle Text-to-Speech
  - Change theme (Light/Dark)
  - Modify assistant name
  - View privacy policy

## 🔧 Common Issues & Fixes

### "Speech recognition not available"
- **Fix**: Install Google app from Play Store
- The device needs Google voice services

### "API Error: 401"
- **Fix**: Your API key is invalid or expired
- Check you copied the entire key correctly
- Verify your OpenAI account is active

### "No internet connection"
- **Fix**: Enable WiFi or mobile data
- AI features require internet

### App crashes on launch
- **Fix**: Invalidate Caches
  - File → Invalidate Caches → Invalidate and Restart
- Clean and rebuild:
  - Build → Clean Project
  - Build → Rebuild Project

### TTS not speaking
- **Fix**: Check device volume
- Go to Settings in app and enable TTS
- Check Android TTS settings

## 📱 Testing Checklist

Before deploying, test these:

- ✅ Voice input works (mic button)
- ✅ Text input works (keyboard)
- ✅ AI responds correctly
- ✅ TTS reads responses aloud
- ✅ Commands work (open apps, search)
- ✅ Settings can be changed
- ✅ Chat history persists after closing app
- ✅ Dark mode works
- ✅ Permissions are requested properly
- ✅ No crashes or ANR errors

## 🏗️ Building Release APK

When ready to share your app:

1. **Build → Generate Signed Bundle/APK**
2. Select **"APK"**
3. Create new keystore:
   - Key store path: `panda-ai-keystore.jks`
   - Password: [Choose a strong password]
   - Alias: `panda-ai`
   - Validity: 25 years
4. Click **"Next"** → **"release"** → **"Finish"**
5. Find APK in: `app/release/app-release.apk`

## 📤 Sharing Your APK

To install on other devices:
1. Copy the APK file to the device
2. Open it on the device
3. Enable "Install from Unknown Sources" if prompted
4. Install and run!

## 🎯 Next Steps

- **Customize**: Change colors, add features
- **Enhance**: Add more voice commands
- **Deploy**: Publish to Google Play Store
- **Share**: Let others try your assistant!

## 💡 Tips

- Keep your API key secret - never share it
- Test on multiple devices if possible
- Read the full README.md for advanced features
- Check OpenAI API usage limits

## 🆘 Need Help?

If you encounter issues:
1. Check the "Troubleshooting" section in README.md
2. Review Android Studio's "Logcat" for error messages
3. Verify all dependencies in `build.gradle` are up to date
4. Google the error message - Stack Overflow helps!

---

**You're all set! Enjoy your Panda AI assistant! 🐼✨**

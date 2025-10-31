# 🐼 Panda AI by Max - Personal Voice Assistant

A complete, production-ready Android voice assistant app with integrated AI chat and voice command control.

## ✨ Features

### Voice Assistant System
- **Voice Recognition**: Tap-to-speak functionality using Android's built-in speech recognition
- **AI Chat**: Powered by OpenAI GPT for natural conversations
- **Text-to-Speech**: AI responses are read aloud automatically
- **Chat History**: Persistent storage using Room database

### Smart Command Processing
- Open apps: YouTube, WhatsApp, Instagram, Facebook, Twitter, Gmail, Spotify, Netflix, Maps
- Google Search: "Search for cats"
- YouTube Search: "Play Bohemian Rhapsody on YouTube"
- Phone calls: "Call mom"
- Send SMS: "Send message to John"
- Open camera, settings, browser
- System information: current time, date
- Set alarms and calendar events

### Modern UI/UX
- Material Design 3 theme
- Dark/Light mode support
- Chat-style interface with user and AI message bubbles
- Floating mic button with pulsating animation when listening
- Loading indicators for AI processing

### Settings & Customization
- Toggle Text-to-Speech on/off
- Change theme (Light/Dark/System)
- Customize assistant name
- Privacy policy viewer
- About screen

### Privacy & Security
- All permissions requested at runtime with clear explanations
- No background recording or spying
- Chat history stored locally only
- User-controlled actions - no automatic operations
- Google Play Store compliant

## 🚀 Getting Started

### Prerequisites
- Android Studio Arctic Fox or newer
- Android SDK 24+ (Android 7.0+)
- OpenAI API key (for AI responses)

### Installation Steps

1. **Clone/Download the project**
   ```bash
   # The project is in the panda-ai-android folder
   ```

2. **Open in Android Studio**
   - Open Android Studio
   - Select "Open an Existing Project"
   - Navigate to the `panda-ai-android` folder
   - Wait for Gradle sync to complete

3. **Configure AI API Key**
   - Open `app/src/main/java/com/max/pandaai/AIService.kt`
   - Find line 18: `private const val API_KEY = "sk-YOUR-OPENAI-API-KEY-HERE"`
   - Replace with your actual OpenAI API key from: https://platform.openai.com/api-keys

   ```kotlin
   private const val API_KEY = "sk-proj-your-actual-api-key-here"
   ```

4. **Build the Project**
   - Click "Build" → "Make Project" (Ctrl+F9 / Cmd+F9)
   - Fix any errors if they appear

5. **Run on Device/Emulator**
   - Connect Android device or start emulator
   - Click "Run" → "Run 'app'" (Shift+F10 / Ctrl+R)
   - Grant permissions when prompted

## 📱 App Structure

```
panda-ai-android/
├── app/
│   ├── src/main/
│   │   ├── java/com/max/pandaai/
│   │   │   ├── MainActivity.kt          # Main chat screen
│   │   │   ├── SplashActivity.kt        # Splash screen with animation
│   │   │   ├── SettingsActivity.kt      # Settings screen
│   │   │   ├── PrivacyPolicyActivity.kt # Privacy policy
│   │   │   ├── AIService.kt             # OpenAI API integration
│   │   │   ├── IntentHandler.kt         # Command processing & app opening
│   │   │   ├── adapter/
│   │   │   │   └── ChatAdapter.kt       # RecyclerView adapter for messages
│   │   │   ├── database/
│   │   │   │   ├── ChatDao.kt           # Database operations
│   │   │   │   └── ChatDatabase.kt      # Room database
│   │   │   ├── model/
│   │   │   │   └── ChatMessage.kt       # Message data model
│   │   │   └── viewmodel/
│   │   │       ├── ChatViewModel.kt     # Business logic
│   │   │       └── ChatViewModelFactory.kt
│   │   ├── res/
│   │   │   ├── layout/                  # All XML layouts
│   │   │   ├── drawable/                # Icons and backgrounds
│   │   │   ├── values/                  # Strings, colors, themes
│   │   │   ├── anim/                    # Animations
│   │   │   ├── menu/                    # Menu items
│   │   │   └── xml/                     # Config files
│   │   └── AndroidManifest.xml          # App configuration & permissions
│   └── build.gradle                     # App dependencies
├── build.gradle                         # Project config
└── settings.gradle
```

## 🎨 Key Components

### MainActivity.kt
- Handles voice input via Android's RecognizerIntent
- Manages TextToSpeech for reading AI responses
- Displays chat messages in RecyclerView
- Requests and manages runtime permissions

### AIService.kt
- Communicates with OpenAI GPT API
- Formats requests and parses responses
- Handles API errors gracefully
- **⚠️ IMPORTANT: Add your API key here!**

### IntentHandler.kt
- Processes voice/text commands
- Opens Android apps using package names
- Handles system intents (calls, SMS, camera, etc.)
- Provides user-friendly responses

### ChatViewModel.kt
- Coordinates between UI, database, and services
- Manages chat message flow
- Handles loading states

## 🔑 Permissions

The app requests these permissions at runtime:

| Permission | Purpose | When Requested |
|------------|---------|----------------|
| `RECORD_AUDIO` | Voice commands | When mic button tapped |
| `INTERNET` | AI API calls | At startup |
| `CALL_PHONE` | Make calls | When user asks to call |
| `SEND_SMS` | Send messages | When user asks to text |
| `READ/WRITE_CALENDAR` | Create events | When user creates event |

All permissions include clear rationale dialogs explaining why they're needed.

## 🛠️ Customization

### Change AI Model
In `AIService.kt`, line 20:
```kotlin
private const val MODEL = "gpt-4" // or "gpt-3.5-turbo"
```

### Add New Commands
In `IntentHandler.kt`, add new patterns in `processCommand()`:
```kotlin
lowerCommand.contains("open tiktok") -> openApp("com.zhiliaoapp.musically", "TikTok")
```

### Modify UI Colors
Edit `res/values/colors.xml`:
```xml
<color name="panda_primary">#YOUR_COLOR_HERE</color>
```

### Change Assistant Personality
Edit system prompt in `AIService.kt`, line 72-80.

## 📲 Building for Release

### Create Signed APK

1. **Generate Keystore**
   ```bash
   keytool -genkey -v -keystore panda-ai-keystore.jks -keyalg RSA -keysize 2048 -validity 10000 -alias panda-ai
   ```

2. **Configure Signing in Android Studio**
   - Build → Generate Signed Bundle/APK
   - Select "APK"
   - Create/select keystore
   - Build release APK

3. **Test APK**
   - Install on test device
   - Test all features and permissions
   - Verify no crashes

### Prepare for Google Play Store

1. **Update Version**
   In `app/build.gradle`:
   ```gradle
   versionCode 1
   versionName "1.0"
   ```

2. **Create Assets**
   - App icon (512x512 PNG)
   - Feature graphic (1024x500)
   - Screenshots (phone & tablet)
   - App description

3. **Privacy Policy**
   - The app includes a built-in privacy policy
   - Host a copy online for Play Store requirements
   - Link: [Your Privacy Policy URL]

4. **Play Store Listing**
   - Title: "Panda AI by Max - Voice Assistant"
   - Category: Productivity
   - Content Rating: Everyone
   - Describe all permissions clearly

## 🐛 Troubleshooting

### Speech Recognition Not Working
- Check device has Google app installed
- Enable microphone permission
- Try restarting device

### AI Not Responding
- Verify API key is correct in `AIService.kt`
- Check internet connection
- Confirm OpenAI API account has credits

### App Crashes on Launch
- Check Gradle sync completed successfully
- Verify minimum SDK is 24+
- Clear app data and reinstall

### TTS Not Speaking
- Check device TTS settings
- Enable TTS in app settings
- Verify device volume is not muted

## 📄 License

© 2024 Panda AI by Max. All rights reserved.

This is a personal project. Modify and use as needed.

## 🤝 Contributing

To improve Panda AI:
1. Add new voice commands in `IntentHandler.kt`
2. Enhance AI personality in `AIService.kt`
3. Improve UI/UX in layout files
4. Add new features with proper documentation

## 📞 Support

For questions or issues:
- Email: pandaai.support@example.com
- GitHub Issues: [Your Repository URL]

## 🎯 Future Enhancements

Potential features to add:
- [ ] Multi-language support
- [ ] Custom wake word ("Hey Panda")
- [ ] Voice profiles for multiple users
- [ ] Smart home integration
- [ ] Reminders and notifications
- [ ] Weather information
- [ ] News briefings
- [ ] Music playback control
- [ ] Contact management
- [ ] Cloud sync for chat history

## ⚠️ Important Notes

1. **API Key Security**: Never commit your API key to public repositories
2. **Permissions**: Only request permissions when needed
3. **Testing**: Test on multiple devices and Android versions
4. **Privacy**: Respect user data - no unauthorized data collection
5. **Play Store**: Follow all Google Play policies

---

**Made with ❤️ by Max**

*Panda AI - Your friendly voice assistant, always ready to help!* 🐼

# Deep Links Implementation Guide

## Overview
This guide explains how to use and test deeplinks in the Singularity app, specifically for the AboutScreen.

## Supported Deep Links

### AboutScreen
- **HTTPS URL**: `https://yourdomain.com/about`
- **Custom Scheme**: `singularityapp://about`

### SecuritySettingScreen
- **HTTPS URL**: `https://yourdomain.com/security/setting`
- **Custom Scheme**: `singularityapp://security/setting`

### HelpAndSupportScreen
- **HTTPS URL**: `https://yourdomain.com/help`
- **Custom Scheme**: `singularityapp://help`

### AccountSettingScreen
- **HTTPS URL**: `https://yourdomain.com/account/setting`
- **Custom Scheme**: `singularityapp://account/setting`

### NotificationSettingScreen
- **HTTPS URL**: `https://yourdomain.com/notification/setting`
- **Custom Scheme**: `singularityapp://notification/setting`

## Testing Deep Links

### Android Testing with ADB
```bash
# Test HTTPS deeplinks
adb shell am start -W -a android.intent.action.VIEW -d "https://yourdomain.com/about" com.singularityuniverse.singularity.android
adb shell am start -W -a android.intent.action.VIEW -d "https://yourdomain.com/security/setting" com.singularityuniverse.singularity.android
adb shell am start -W -a android.intent.action.VIEW -d "https://yourdomain.com/help" com.singularityuniverse.singularity.android
adb shell am start -W -a android.intent.action.VIEW -d "https://yourdomain.com/account/setting" com.singularityuniverse.singularity.android
adb shell am start -W -a android.intent.action.VIEW -d "https://yourdomain.com/notification/setting" com.singularityuniverse.singularity.android

# Test custom scheme deeplinks
adb shell am start -W -a android.intent.action.VIEW -d "singularityapp://about" com.singularityuniverse.singularity.android
adb shell am start -W -a android.intent.action.VIEW -d "singularityapp://security/setting" com.singularityuniverse.singularity.android
adb shell am start -W -a android.intent.action.VIEW -d "singularityapp://help" com.singularityuniverse.singularity.android
adb shell am start -W -a android.intent.action.VIEW -d "singularityapp://account/setting" com.singularityuniverse.singularity.android
adb shell am start -W -a android.intent.action.VIEW -d "singularityapp://notification/setting" com.singularityuniverse.singularity.android
```

### iOS Testing
For iOS, you can test deeplinks using:
1. Safari browser with the URL
2. Simulator with device -> device -> open URL
3. Xcode scheme arguments

## Implementation Details

### 1. AndroidManifest.xml
- Added intent filters for both HTTPS and custom scheme
- Set `android:autoVerify="true"` for .App Links
- Added `android:launchMode="singleTop"` to prevent multiple instances

### 2. Navigation Setup
- Added `navDeepLink` to the AboutScreen composable
- Defined deeplink patterns in Destination.kt
- Created DeepLinkHandler for programmatic navigation

### 3. MainActivity
- Handles incoming intents with deeplink data
- Supports both cold start and warm start scenarios
- Passes deeplink URL to .App composable

## Adding More Deep Links

To add deeplinks for other screens:

1. **Define the deeplink URLs in Destination.kt**:
```kotlin
const val YourScreenDeepLink = "https://yourdomain.com/yourscreen"
const val YourScreenCustomDeepLink = "singularityapp://yourscreen"
```

2. **Add to MainNavigation.kt**:
```kotlin
composable(
    route = YourScreenDestination,
    deepLinks = listOf(
        navDeepLink { uriPattern = YourScreenDeepLink },
        navDeepLink { uriPattern = YourScreenCustomDeepLink }
    )
) {
    YourScreen()
}
```

3. **Update DeepLinkHandler**:
```kotlin
fun handleDeepLink(url: String): Boolean {
    return when {
        url.contains("/about") -> {
            navigateToAbout()
            true
        }
        url.contains("/security/setting") -> {
            navigateToSecuritySetting()
            true
        }
        url.contains("/help") -> {
            navigateToHelpAndSupport()
            true
        }
        url.contains("/account/setting") -> {
            navigateToAccountSetting()
            true
        }
        url.contains("/notification/setting") -> {
            navigateToNotificationSetting()
            true
        }
        url.contains("/yourscreen") -> {
            navigateToYourScreen()
            true
        }
        else -> false
    }
}
```

## Best Practices

1. **URL Structure**: Use consistent, meaningful URL patterns
2. **Validation**: Always validate deeplink parameters
3. **Fallback**: Provide fallback navigation if deeplink fails
4. **Testing**: Test both cold start and warm start scenarios
5. **Analytics**: Track deeplink usage for insights

## Troubleshooting

### Common Issues:
1. **Deeplink not working**: Check AndroidManifest.xml intent filters
2. **Wrong destination**: Verify URL patterns match exactly
3. **.App not opening**: Ensure the package name is correct in ADB commands
4. **Multiple instances**: Use `launchSingleTop = true` in navigation

### Debugging:
- Use `adb logcat` to see intent handling logs
- Check if the intent filter is correctly configured
- Verify the URL pattern matches the incoming intent data

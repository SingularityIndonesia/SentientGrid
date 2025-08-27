# Account UI Components

This directory contains reusable UI components extracted from the AccountPane. These components follow Material Design 3 principles and are designed to be used across the application.

## Components Overview

### 1. SearchBar.kt
A reusable search input component with:
- Placeholder text support
- Search icon
- Clear button (when text is present)
- Keyboard handling
- Custom styling

**Usage:**
```kotlin
SearchBar(
    query = searchQuery,
    onQueryChange = { query -> /* handle query change */ },
    placeholder = "Search settings...",
    modifier = Modifier.fillMaxWidth()
)
```

### 2. UserProfileCard.kt
A card component displaying user profile information:
- Profile image (with fallback to initials)
- User name and email
- Click handler
- Consistent styling

**Usage:**
```kotlin
UserProfileCard(
    userProfile = UserProfile(
        name = "John Doe",
        email = "john.doe@example.com"
    ),
    onClick = { /* handle profile click */ },
    modifier = Modifier.fillMaxWidth()
)
```

### 3. StorageUsageCard.kt
A card showing storage usage information:
- Storage used vs total storage
- Progress bar visualization
- Manage storage action
- Customizable title

**Usage:**
```kotlin
StorageUsageCard(
    storageUsed = "8.7 GB",
    totalStorage = "15 GB",
    progress = 0.58f,
    onManageClick = { /* handle manage click */ },
    modifier = Modifier.fillMaxWidth()
)
```

### 4. AccountMenuItemCard.kt
A reusable menu item component:
- Icon with circular background
- Title and optional subtitle
- Search text highlighting
- Navigation arrow
- Click handler

**Usage:**
```kotlin
AccountMenuItemCard(
    menuItem = AccountMenuItem(
        title = "Settings",
        subtitle = ".App preferences",
        iconRes = Res.drawable.ic_settings,
        onClick = { /* handle click */ }
    ),
    searchQuery = "sett", // Optional for highlighting
    modifier = Modifier.fillMaxWidth()
)
```

### 5. NoResultsFound.kt
A component for displaying "no results" state:
- Search icon
- Customizable title and description
- Consistent styling

**Usage:**
```kotlin
NoResultsFound(
    searchQuery = "example",
    title = "No results found",
    description = "Try different keywords",
    modifier = Modifier.fillMaxWidth()
)
```

### 6. SearchResultsHeader.kt
A header component for search results:
- Results count
- Customizable title
- Consistent styling

**Usage:**
```kotlin
SearchResultsHeader(
    resultsCount = 5,
    title = "Search Results",
    modifier = Modifier.fillMaxWidth()
)
```

## Data Classes

### UserProfile
```kotlin
data class UserProfile(
    val name: String,
    val email: String,
    val profileImageUrl: String? = null,
    val storageUsed: String = "2.3 GB",
    val totalStorage: String = "15 GB",
    val storageProgress: Float = 0.15f
)
```

### AccountMenuItem
```kotlin
data class AccountMenuItem(
    val title: String,
    val subtitle: String? = null,
    val iconRes: DrawableResource,
    val onClick: () -> Unit = {},
    val showDivider: Boolean = false
)
```

## Design Principles

1. **Consistency**: All components follow Material Design 3 guidelines
2. **Reusability**: Components are designed to be used in multiple contexts
3. **Customization**: Modifiers and parameters allow for flexible styling
4. **Accessibility**: Proper content descriptions and semantic markup
5. **Performance**: Efficient composition and state management

## Benefits of Extraction

1. **Maintainability**: Changes to component behavior only need to be made in one place
2. **Testability**: Components can be tested individually
3. **Reusability**: Components can be used across different screens
4. **Consistency**: Ensures uniform UI across the application
5. **Development Speed**: Developers can quickly compose new screens using existing components

## Usage Tips

- Always provide meaningful `contentDescription` for icons
- Use consistent spacing and padding patterns
- Leverage the component's built-in theming support
- Consider adding your own Preview composables for testing
- Use the `AccountComponents.kt` file for convenient imports

@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package utils

expect object MediaPicker {
    /**
     * Launches a media picker to select images and videos
     * @param onMediaSelected Callback invoked when media is selected with the list of URIs
     * @param onCancelled Callback invoked when the picker is cancelled
     */
    fun launchMediaPicker(
        onMediaSelected: (List<String>) -> Unit,
        onCancelled: () -> Unit = {}
    )

    /**
     * Checks if media picker is available on the current platform
     * @return true if media picker is available, false otherwise
     */
    fun isMediaPickerAvailable(): Boolean
}

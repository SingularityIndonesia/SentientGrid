package utils

actual object MediaPicker {
    actual fun launchMediaPicker(
        onMediaSelected: (List<String>) -> Unit,
        onCancelled: () -> Unit
    ) {
    }

    actual fun isMediaPickerAvailable(): Boolean {
        TODO("Not yet implemented")
    }
}
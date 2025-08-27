package utils


expect fun notifyMediaScanner(
    uri: String,
    onScanCompleted: (path: String) -> Unit = { _ -> }
)
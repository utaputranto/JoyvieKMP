package com.utaputranto.joyviekmp.core.platform

import android.app.AlertDialog
import android.widget.Toast

actual fun showToast(message: String) {
    // Show toast on the UI thread to ensure it runs correctly
    val activity = PlatformContextProvider.currentActivity
    if (activity != null && !activity.isFinishing && !activity.isDestroyed) {
        activity.runOnUiThread {
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
        }
    } else {
        // Fallback to application context if no active activity (e.g. background tasks/services)
        Toast.makeText(PlatformContextProvider.context, message, Toast.LENGTH_SHORT).show()
    }
}

actual fun showAlert(
    title: String,
    message: String,
    confirmButtonText: String,
    onConfirm: () -> Unit,
    dismissButtonText: String?,
    onDismiss: (() -> Unit)?,
) {
    val activity = PlatformContextProvider.currentActivity
    if (activity != null && !activity.isFinishing && !activity.isDestroyed) {
        activity.runOnUiThread {
            val builder =
                AlertDialog.Builder(activity)
                    .setTitle(title)
                    .setMessage(message)
                    .setCancelable(false)
                    .setPositiveButton(confirmButtonText) { dialog, _ ->
                        onConfirm()
                        dialog.dismiss()
                    }

            if (dismissButtonText != null) {
                builder.setNegativeButton(dismissButtonText) { dialog, _ ->
                    onDismiss?.invoke()
                    dialog.dismiss()
                }
            }

            builder.show()
        }
    } else {
        AppLogger.w("PlatformNotifier", "Cannot show alert dialog: No active activity context.")
    }
}

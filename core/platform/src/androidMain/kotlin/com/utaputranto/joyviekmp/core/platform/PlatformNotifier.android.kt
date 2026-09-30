package com.utaputranto.joyviekmp.core.platform

import android.app.AlertDialog
import android.content.Context
import android.widget.Toast
import org.koin.java.KoinJavaComponent.getKoin

actual fun showToast(message: String) {
    val context: Context = getKoin().get()
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

actual fun showAlert(
    title: String,
    message: String,
    confirmButtonText: String,
    onConfirm: () -> Unit,
    dismissButtonText: String?,
    onDismiss: (() -> Unit)?,
) {
    val context: Context = getKoin().get()
    val builder =
        AlertDialog.Builder(context)
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

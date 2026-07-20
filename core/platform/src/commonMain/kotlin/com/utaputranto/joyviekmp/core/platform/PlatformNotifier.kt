package com.utaputranto.joyviekmp.core.platform

expect fun showToast(message: String)

expect fun showAlert(
    title: String,
    message: String,
    confirmButtonText: String = "OK",
    onConfirm: () -> Unit = {},
    dismissButtonText: String? = null,
    onDismiss: (() -> Unit)? = null,
)

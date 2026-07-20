package com.utaputranto.joyviekmp.core.platform

import platform.Foundation.NSTimer
import platform.UIKit.UIAlertAction
import platform.UIKit.UIAlertActionStyleCancel
import platform.UIKit.UIAlertActionStyleDefault
import platform.UIKit.UIAlertController
import platform.UIKit.UIAlertControllerStyleAlert
import platform.UIKit.UIApplication
import platform.UIKit.UIViewController
import platform.UIKit.UIWindow

private fun getTopViewController(root: UIViewController?): UIViewController? {
    val presented = root?.presentedViewController
    if (presented != null) {
        return getTopViewController(presented)
    }
    return root
}

private fun getViewController(): UIViewController? {
    val keyWindow =
        UIApplication.sharedApplication.keyWindow
            ?: (UIApplication.sharedApplication.windows.firstOrNull() as? UIWindow)
    return getTopViewController(keyWindow?.rootViewController)
}

actual fun showToast(message: String) {
    val alert =
        UIAlertController.alertControllerWithTitle(
            title = null,
            message = message,
            preferredStyle = UIAlertControllerStyleAlert,
        )

    val viewController = getViewController()
    if (viewController != null) {
        viewController.presentViewController(alert, animated = true, completion = {
            NSTimer.scheduledTimerWithTimeInterval(
                interval = 2.0,
                repeats = false,
                block = { timer: NSTimer? ->
                    alert.dismissViewControllerAnimated(true, completion = null)
                },
            )
        })
    } else {
        AppLogger.w("PlatformNotifier", "Cannot show toast: No active view controller found.")
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
    val alert =
        UIAlertController.alertControllerWithTitle(
            title = title,
            message = message,
            preferredStyle = UIAlertControllerStyleAlert,
        )

    val confirmAction =
        UIAlertAction.actionWithTitle(
            title = confirmButtonText,
            style = UIAlertActionStyleDefault,
            handler = { _ ->
                onConfirm()
            },
        )
    alert.addAction(confirmAction)

    if (dismissButtonText != null) {
        val dismissAction =
            UIAlertAction.actionWithTitle(
                title = dismissButtonText,
                style = UIAlertActionStyleCancel,
                handler = { _ ->
                    onDismiss?.invoke()
                },
            )
        alert.addAction(dismissAction)
    }

    val viewController = getViewController()
    if (viewController != null) {
        viewController.presentViewController(alert, animated = true, completion = null)
    } else {
        AppLogger.w("PlatformNotifier", "Cannot show alert: No active view controller found.")
    }
}

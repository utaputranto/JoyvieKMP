package com.utaputranto.joyviekmp

import androidx.compose.ui.window.ComposeUIViewController
import com.utaputranto.joyviekmp.di.initKoin
import platform.UIKit.UIViewController

@Suppress("FunctionName")
fun MainViewController(): UIViewController {
    initKoin()
    return ComposeUIViewController { MainApp() }
}

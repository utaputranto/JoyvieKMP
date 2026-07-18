package com.utaputranto.joyviekmp

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.utaputranto.joyviekmp.di.appModules
import org.koin.compose.KoinApplication

@Preview
@Composable
fun MainAppPreview() {
    // Previews render outside the app, so Application.onCreate never runs;
    // provide a local Koin context for koinViewModel/koinInject to resolve.
    KoinApplication(application = { modules(appModules) }) {
        MainApp()
    }
}

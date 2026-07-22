import SwiftUI

/// Main entry point for the iOS application.
///
/// Bootstraps the SwiftUI scene hierarchy and hosts the root [ContentView]
/// which bridges the Compose Multiplatform UI into iOS.
@main
struct iOSApp: App {
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
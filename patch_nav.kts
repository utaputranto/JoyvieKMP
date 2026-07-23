import java.io.File

val file = File("feature/onboarding/presentation/src/commonMain/kotlin/com/utaputranto/joyviekmp/feature/onboarding/presentation/navigation/OnboardingNavigation.kt")
var text = file.readText()

text = text.replace("import com.utaputranto.joyviekmp.feature.onboarding.presentation.WelcomeMainScreen", "import com.utaputranto.joyviekmp.feature.onboarding.presentation.WelcomeMainScreen\nimport com.utaputranto.joyviekmp.core.platform.showToast")

text = text.replace("            WelcomeMainScreen(", """            LaunchedEffect(viewModel.toastEvent) {
                viewModel.toastEvent.collect { message ->
                    showToast(message)
                }
            }

            WelcomeMainScreen(""")

file.writeText(text)

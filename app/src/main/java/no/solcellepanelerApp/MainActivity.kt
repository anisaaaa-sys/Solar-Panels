package no.solcellepanelerApp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
<<<<<<< HEAD
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalDensity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import no.solcellepanelerApp.ui.font.FontScaleViewModel
import no.solcellepanelerApp.ui.language.LanguageUtils
import no.solcellepanelerApp.ui.navigation.Nav
import no.solcellepanelerApp.ui.onboarding.OnboardingScreen
import no.solcellepanelerApp.ui.onboarding.OnboardingUtils
=======
import androidx.compose.ui.platform.LocalDensity
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import no.solcellepanelerApp.ui.font.FontScaleViewModel
import no.solcellepanelerApp.ui.language.LanguageUtils
import no.solcellepanelerApp.ui.navigation.Nav
>>>>>>> 0eec2f562a6c5679733228427e18bb9ed3baa46b
import no.solcellepanelerApp.ui.theme.SolcellepanelerAppTheme


class MainActivity : ComponentActivity() {
<<<<<<< HEAD

    private val onboardingUtils by lazy { OnboardingUtils(this) }

=======
>>>>>>> 0eec2f562a6c5679733228427e18bb9ed3baa46b
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val languageCode = LanguageUtils.getSavedLanguage(this) ?: "en"
        LanguageUtils.setLanguage(this, languageCode)
<<<<<<< HEAD
        installSplashScreen()

        enableEdgeToEdge()
        setContent {
            SolcellepanelerAppTheme {
                if (onboardingUtils.isOnboardingCompleted()) {
                    App()
                } else {
                    ShowOnboardingScreen()
                }


            }
        }
    }


    @Composable
    fun App() {
        val navController = rememberNavController()
        val fontScaleViewModel: FontScaleViewModel = viewModel()

        val systemFontScale = LocalDensity.current.fontScale
        val effectiveFontScale = systemFontScale * fontScaleViewModel.fontScale.floatValue

        SolcellepanelerAppTheme(
            fontScale = effectiveFontScale
        ) {
            Nav(navController = navController, fontScaleViewModel = fontScaleViewModel)
        }
    }

    @Composable
    fun ShowOnboardingScreen() {
        val scope = rememberCoroutineScope()
        OnboardingScreen {
            onboardingUtils.setOnboardingCompleted()
            scope.launch {
                setContent {
                    App()
                }
            }
        }
    }


}


=======
        enableEdgeToEdge()
        setContent {
            SolcellepanelerAppTheme {
                App()
            }
        }
    }
}

@Composable
fun App() {
    val navController = rememberNavController()
    val fontScaleViewModel: FontScaleViewModel = viewModel()

    val systemFontScale = LocalDensity.current.fontScale
    val effectiveFontScale = systemFontScale * fontScaleViewModel.fontScale.floatValue

    SolcellepanelerAppTheme(
        fontScale = effectiveFontScale
    ) {
        Nav(navController = navController, fontScaleViewModel = fontScaleViewModel)
    }
}
>>>>>>> 0eec2f562a6c5679733228427e18bb9ed3baa46b

package no.solcellepaneller.ui.infoscreen


import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import no.solcellepaneller.R
import no.solcellepaneller.ui.navigation.AppearanceBottomSheet
import no.solcellepaneller.ui.navigation.BottomBar
import no.solcellepaneller.ui.navigation.TopBar
import no.solcellepaneller.ui.font.FontScaleViewModel
import no.solcellepaneller.ui.navigation.HelpBottomSheet
import no.solcellepaneller.ui.reusables.ExpandInfoSection
import no.solcellepaneller.ui.theme.SolcellepanellerTheme


@Composable
fun InfoScreen(navController: NavController, fontScaleViewModel: FontScaleViewModel
) {
    SolcellepanellerTheme {
        var showHelp by remember { mutableStateOf(false) }
        var showAppearance by remember { mutableStateOf(false) }

        Scaffold(
            topBar = {
                var backClicked by remember { mutableStateOf(false) }
                TopBar(
                    navController = navController,
                    text = stringResource(R.string.info_title),
                    onBackClick = { backClicked = true },
                    modifier = Modifier,
                    backClick = !backClicked
                )
            },
            bottomBar = {
                BottomBar(
                    onHelpClicked = { showHelp = true },
                    onAppearanceClicked = { showAppearance = true },
                    navController = navController
                )
            }
        ) { padding ->
        LazyColumn (
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {

            item {

                ExpandInfoSection(
                    title = stringResource(id = R.string.intro_title),
                    content = stringResource(id = R.string.intro_content)
                )

            }

            item {
                ExpandInfoSection(
                    title = stringResource(id = R.string.price_title),
                    content = stringResource(id = R.string.price_content)
                )
            }
            item {

                ExpandInfoSection(
                    title = stringResource(id = R.string.pros_and_cons_title),
                    content = stringResource(id = R.string.pros_and_cons_content)
                )

            }
            item {

                ExpandInfoSection(
                    title = stringResource(id = R.string.money_saved_title),
                    content = stringResource(id = R.string.money_saved_content)
                )

            }
            item {

                ExpandInfoSection(
                    title = stringResource(id = R.string.cabin_title),
                    content = stringResource(id = R.string.cabin_content)
                )

                }
            }

            HelpBottomSheet(
                visible = showHelp,
                onDismiss ={ showHelp = false },
            )
AppearanceBottomSheet(
                visible = showAppearance,
                onDismiss = { showAppearance = false },
                fontScaleViewModel = fontScaleViewModel
            )
        }
    }
}
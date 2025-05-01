package no.solcellepaneller.ui.home

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import no.solcellepaneller.R
import no.solcellepaneller.data.homedata.ElectricityPriceRepository
import no.solcellepaneller.data.location.LocationService
import no.solcellepaneller.model.electricity.Region
import no.solcellepaneller.ui.electricity.*
import no.solcellepaneller.ui.font.FontScaleViewModel
import no.solcellepaneller.ui.handling.LoadingScreen
import no.solcellepaneller.ui.navigation.*
import no.solcellepaneller.ui.reusables.MyNavCard

@Composable
fun HomeScreen(
    navController: NavController,
    fontScaleViewModel: FontScaleViewModel
) {
    var showHelp by remember { mutableStateOf(false) }
    var showAppearance by remember { mutableStateOf(false) }
    val isLoading by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val activity = context as? Activity
    var selectedRegion by remember { mutableStateOf(Region.BERGEN) }

    // Get user location and map to region
    LaunchedEffect(Unit) {
        activity?.let { it ->
            val locationService = LocationService(it)
            try {
                val location = locationService.getCurrentLocation()
                location?.let {
                    selectedRegion = mapLocationToRegion(it)
                }
            } catch (e: Exception) {
                Log.e("HomeScreen", "Error getting location", e)
            }
        }
    }

    // ViewModel to load price data
    val repository = ElectricityPriceRepository(priceArea = selectedRegion.regionCode)
    val viewModel: PriceScreenViewModel = viewModel(
        factory = PriceViewModelFactory(repository, selectedRegion.regionCode),
        key = selectedRegion.regionCode
    )
    val priceUiState by viewModel.priceUiState.collectAsStateWithLifecycle()

    if (isLoading) {
        LoadingScreen()
        return
    }

    Scaffold(
        topBar = {
            TopBar(
                navController = navController,
                text = "IKON og APPNAVN",
                backClick = false,
                height = 150.dp
            )
        },
        bottomBar = {
            BottomBar(
                onHelpClicked = { showHelp = true },
                onAppearanceClicked = { showAppearance = true },
                navController = navController
            )
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            MyNavCard(
                text = stringResource(id = R.string.install_panels),
                route = "map",
                navController = navController,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                style = "Large"
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(1.dp)
            ) {
                MyNavCard(
                    text = stringResource(id = R.string.saved_locations),
                    route = "saved_locations",
                    navController = navController,
                    modifier = Modifier
                        .weight(1f)
                        .height(400.dp),
                    style = "Large"
                )

                MyNavCard(
                    text = stringResource(id = R.string.prices),
                    route = "prices",
                    navController = navController,
                    modifier = Modifier
                        .weight(1f)
                        .height(400.dp),
                    style = "Large"
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Show PriceCard
            when (priceUiState) {
                is PriceUiState.Loading -> {
                    Text("Laster strømpriser...", style = MaterialTheme.typography.bodyLarge)
                }

                is PriceUiState.Error -> {
                    Text(
                        "Kunne ikke hente strømpriser",
                        color = MaterialTheme.colorScheme.error
                    )
                }

                is PriceUiState.Success -> {
                    val prices = (priceUiState as PriceUiState.Success).prices
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        elevation = CardDefaults.cardElevation(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                "Strømpriser i ${selectedRegion.displayName}",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            PriceCard(prices = prices)
                        }
                    }
                }
            }

            HelpBottomSheet(
                visible = showHelp,
                onDismiss = { showHelp = false }
            )

            AppearanceBottomSheet(
                visible = showAppearance,
                onDismiss = { showAppearance = false },
                fontScaleViewModel = fontScaleViewModel
            )
        }
    }
}

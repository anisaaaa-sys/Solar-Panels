package no.solcellepanelerApp.ui.home

<<<<<<< HEAD
import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
=======
import android.location.Location
import android.util.Log
>>>>>>> 0eec2f562a6c5679733228427e18bb9ed3baa46b
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
<<<<<<< HEAD
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
=======
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
>>>>>>> 0eec2f562a6c5679733228427e18bb9ed3baa46b
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
<<<<<<< HEAD
import androidx.compose.runtime.mutableIntStateOf
=======
>>>>>>> 0eec2f562a6c5679733228427e18bb9ed3baa46b
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
<<<<<<< HEAD
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
=======
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
>>>>>>> 0eec2f562a6c5679733228427e18bb9ed3baa46b
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import no.solcellepanelerApp.MainActivity
import no.solcellepanelerApp.R
import no.solcellepanelerApp.data.homedata.ElectricityPriceRepository
import no.solcellepanelerApp.model.electricity.Region
import no.solcellepanelerApp.ui.electricity.HomePriceCard
import no.solcellepanelerApp.ui.electricity.PriceScreenViewModel
import no.solcellepanelerApp.ui.electricity.PriceUiState
import no.solcellepanelerApp.ui.electricity.PriceViewModelFactory
import no.solcellepanelerApp.ui.font.FontScaleViewModel
import no.solcellepanelerApp.ui.handling.ErrorScreen
import no.solcellepanelerApp.ui.handling.LoadingScreen
import no.solcellepanelerApp.ui.navigation.AppearanceBottomSheet
import no.solcellepanelerApp.ui.navigation.BottomBar
import no.solcellepanelerApp.ui.navigation.HelpBottomSheet
<<<<<<< HEAD
import no.solcellepanelerApp.ui.result.WeatherViewModel
import no.solcellepanelerApp.ui.reusables.MyDisplayCard
import no.solcellepanelerApp.ui.reusables.MyNavCard
import no.solcellepanelerApp.ui.theme.ThemeMode
import no.solcellepanelerApp.ui.theme.ThemeState
import no.solcellepanelerApp.util.fetchCoordinates
import no.solcellepanelerApp.util.mapLocationToRegion
import java.time.LocalTime
import java.time.ZonedDateTime


@SuppressLint("DefaultLocale")
=======
import no.solcellepanelerApp.ui.navigation.TopBar
import no.solcellepanelerApp.ui.result.WeatherViewModel
import no.solcellepanelerApp.ui.reusables.MyNavCard
import no.solcellepanelerApp.util.RequestLocationPermission
import no.solcellepanelerApp.util.fetchCoordinates
import java.time.ZonedDateTime

>>>>>>> 0eec2f562a6c5679733228427e18bb9ed3baa46b
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    fontScaleViewModel: FontScaleViewModel,
    weatherViewModel: WeatherViewModel,
) {
    var context = LocalContext.current
    val activity = (context as? MainActivity)
    val radiationArray by weatherViewModel.frostDataRim.collectAsState()

    var showHelp by remember { mutableStateOf(false) }
    var showAppearance by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    var selectedRegion by rememberSaveable { mutableStateOf<Region?>(null) }
    var currentLocation by remember { mutableStateOf<Location?>(null) }
    var locationPermissionGranted by remember { mutableStateOf(false) }
<<<<<<< HEAD
    Log.d("HomeScreen", "currentLocation: $currentLocation")
    var dataFetched by remember { mutableStateOf(false) }

    val currentHour by remember { mutableIntStateOf(ZonedDateTime.now().minusHours(2).hour) }
=======
    var dataFetched by remember { mutableStateOf(false) }

    var currentHour by remember { mutableStateOf(ZonedDateTime.now().minusHours(2).hour) }
>>>>>>> 0eec2f562a6c5679733228427e18bb9ed3baa46b
    //var currentHourValue by remember { mutableStateOf(radiationArray[currentHour]) }
    // The value for the current hour
    Log.e("HomeScreen", "currentHour: $currentHour")
    Log.e("HomeScreen", "radiationArray: ${radiationArray.joinToString(", ")}")
    var currentHourValueny by remember { mutableStateOf<Double?>(null) }
    Log.e("HomeScreen", "currentHourValueny: $currentHourValueny")
<<<<<<< HEAD
    LaunchedEffect(currentHour, radiationArray) {
=======
    LaunchedEffect(currentHour,radiationArray) {
>>>>>>> 0eec2f562a6c5679733228427e18bb9ed3baa46b
        radiationArray.let {
            if (it.isNotEmpty()) {
                it[currentHour]?.let { currentHourValue ->
                    Log.e("HomeScreen", "currentHourValue: $currentHourValue")
                    currentHourValueny = currentHourValue / 1000.0
                } ?: run {
                    Log.e("HomeScreen", "currentHourValue is null.")
                }
            } else {
                Log.e("HomeScreen", "radiationArray is empty.")
            }
        }
    }

    if (isLoading) {
        LoadingScreen()
        return
    }

<<<<<<< HEAD
//    //Request location permission and fetch region
//    RequestLocationPermission { region ->
//        selectedRegion = region
//        locationPermissionGranted = true
//
//    }
//
//    LaunchedEffect(locationPermissionGranted) {
//        if (locationPermissionGranted && activity != null) {
//            val location = fetchCoordinates(context, activity)
//            currentLocation = location
//
//        }
//    }

    LaunchedEffect(Unit) {
        val permissionGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (permissionGranted && activity != null) {
            val location = fetchCoordinates(context, activity)
            currentLocation = location
            selectedRegion = location?.let { mapLocationToRegion(it) } ?: Region.OSLO
        } else {
            selectedRegion = Region.OSLO
        }
    }


    Log.d("HomeScreen", "currentLocation: $currentLocation")
    if (currentLocation != null && !dataFetched) {
        Log.e(
            "HomeScreen",
            "currentLocation is now not null and is: ${currentLocation!!.latitude}, ${currentLocation!!.longitude}"
        )
        weatherViewModel.fetchRimData(
            currentLocation!!.latitude,
            currentLocation!!.longitude,
            "mean(surface_downwelling_shortwave_flux_in_air PT1H)"
=======
    //Request location permission and fetch region
    RequestLocationPermission { region ->
        selectedRegion = region
        locationPermissionGranted = true

    }

        LaunchedEffect(locationPermissionGranted) {
            if(locationPermissionGranted && activity!=null){
            val location= fetchCoordinates(context,activity)
            currentLocation = location

        }}

    Log.d("HomeScreen", "currentLocation: $currentLocation")
    if(currentLocation!=null && !dataFetched){
        Log.e("HomeScreen", "currentLocation is now not null and is: ${currentLocation!!.latitude}, ${currentLocation!!.longitude}")
        weatherViewModel.fetchRimData(
            currentLocation!!.latitude,currentLocation!!.longitude,"mean(surface_downwelling_shortwave_flux_in_air PT1H)"
>>>>>>> 0eec2f562a6c5679733228427e18bb9ed3baa46b
        )
        dataFetched = true
        Log.d("HomeScreen", "radiationArray: $radiationArray")
    }

<<<<<<< HEAD
    val isDark = when (ThemeState.themeMode) {
        ThemeMode.DARK -> true
        ThemeMode.LIGHT -> false
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
    }


    Scaffold(
        topBar = {
            Surface(
                modifier = Modifier
                    .padding(top = 35.dp),
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth()
//                        .background(Color.Red)
                    ,
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(
                            id = if (isDark) R.drawable.logo_topbar_dark else R.drawable.logo_topbar_light
                        ),
                        contentDescription = "",
                        modifier = Modifier
                            .height(100.dp)
                    )
                }
            }
=======
    Scaffold(
        topBar = {
            TopBar(
                navController = navController,
                text = "*IKON og APPNAVN*",
                backClick = false,
            )
>>>>>>> 0eec2f562a6c5679733228427e18bb9ed3baa46b
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
<<<<<<< HEAD
//                .background(Color.Blue)
            ,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            MyNavCard(
                text = stringResource(id = R.string.install_panels_title),
                desc = stringResource(id = R.string.install_panels_desc),
=======
                .padding(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            MyNavCard(
                text = stringResource(id = R.string.install_panels),
>>>>>>> 0eec2f562a6c5679733228427e18bb9ed3baa46b
                route = "map",
                navController = navController,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
<<<<<<< HEAD
                style = MaterialTheme.typography.displaySmall,
//                content = { PanelAnimation() },
                color = MaterialTheme.colorScheme.tertiary
//                color = MaterialTheme.colorScheme.primary
//                color = MaterialTheme.colorScheme.secondary
=======
                style = MaterialTheme.typography.displayLarge,
                content = { PanelAnimation() },
                color = MaterialTheme.colorScheme.tertiary
>>>>>>> 0eec2f562a6c5679733228427e18bb9ed3baa46b
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(1.dp)
            ) {
<<<<<<< HEAD

                MyDisplayCard(
                    modifier = Modifier
                        .weight(1f)
                        .height(400.dp),
                    style = MaterialTheme.typography.displaySmall,
                    content = {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .border(
                                    3.dp,
                                    MaterialTheme.colorScheme.primary,
                                    RoundedCornerShape(15.dp)
                                )
//                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .padding(start = 10.dp, end = 10.dp, top = 20.dp),

                            horizontalAlignment = Alignment.CenterHorizontally // Center text horizontally
                        ) {
                            val timenow = LocalTime.now().hour
                            Text(
                                "LIVE ENERGY $timenow:00 ",
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.secondary
//                                color = MaterialTheme.colorScheme.tertiary Oransje fargen. bare å fjerne kommentaren her hvis dere vil bruke oransj d
                            )
                            Spacer(modifier = Modifier.height(10.dp))

                            if (currentHourValueny != null) {
                                Text(
                                    text = currentHourValueny?.let {
                                        String.format(
                                            "%.4f",
                                            it
                                        ) + " kW/m²"
                                    } ?: "No data",
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.ExtraLight,
                                    color = MaterialTheme.colorScheme.primary
                                )

                                SunAnimation(currentHourValueny ?: 0.0)
                            } else {
                                LoadingScreen()
                            }

                        }
                    },
                    color = MaterialTheme.colorScheme.primary
                )

                MyNavCard(
=======
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(400.dp)
                        .align(Alignment.CenterVertically)
                        .padding(16.dp) // Add padding around the card
                         // Add shadow for a nice elevation effect
                    , // Rounded corners // Elevation for a more card-like appearance
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp), // Padding inside the card to separate text from edges
                         // Center text vertically
                        horizontalAlignment = Alignment.CenterHorizontally // Center text horizontally
                    ) {

                        Text("LIVE ENERGY:",style = MaterialTheme.typography.displaySmall, // Use a larger, bolder text style
                            color = MaterialTheme.colorScheme.onSurface)
                        Text(
                            text = "${currentHourValueny  ?: "No data"} Kwh", // Optional fallback for null
                            style = MaterialTheme.typography.displaySmall, // Use a larger, bolder text style
                            color = MaterialTheme.colorScheme.primary // Use an appropriate text color
                        )
                        LightningAnimation()

                    }
                }


                MyNavCard(
                    //text = stringResource(id = R.string.prices),
>>>>>>> 0eec2f562a6c5679733228427e18bb9ed3baa46b
                    route = "prices",
                    navController = navController,
                    modifier = Modifier
                        .weight(1f)
                        .height(400.dp),
<<<<<<< HEAD
                    style = MaterialTheme.typography.headlineSmall,
                    content = {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(start = 10.dp, end = 10.dp, top = 20.dp),
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Text(
                                    text = stringResource(R.string.live_prices),
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.secondary
//                                color = MaterialTheme.colorScheme.tertiary Oransje fargen. bare å fjerne kommentaren her hvis dere vil bruke oransj d
                                )
                                Spacer(modifier = Modifier.height(10.dp))


=======
                    style = MaterialTheme.typography.displaySmall,
                    //content = { LightningAnimation() },
                    content = {
                        Column(modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Top
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Sjekk strømprisene!",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(10.dp))

                                //Spacer(modifier = Modifier.height(10.dp))
>>>>>>> 0eec2f562a6c5679733228427e18bb9ed3baa46b
                                // Show loading screen until the region is selected
                                if (selectedRegion == null) {
                                    LoadingScreen()
                                } else {
                                    val repository =
                                        ElectricityPriceRepository(priceArea = selectedRegion!!.regionCode)

                                    val viewModel: PriceScreenViewModel = viewModel(
                                        factory = PriceViewModelFactory(
                                            repository,
                                            selectedRegion!!.regionCode
                                        ),
                                        key = selectedRegion!!.regionCode
                                    )

                                    val priceUiState by viewModel.priceUiState.collectAsStateWithLifecycle()

                                    when (priceUiState) {
                                        is PriceUiState.Loading -> LoadingScreen()
                                        is PriceUiState.Error -> ErrorScreen()
                                        is PriceUiState.Success -> {
<<<<<<< HEAD
                                            val prices =
                                                (priceUiState as PriceUiState.Success).prices
=======
                                            val prices = (priceUiState as PriceUiState.Success).prices
>>>>>>> 0eec2f562a6c5679733228427e18bb9ed3baa46b
                                            Column {
                                                HomePriceCard(prices, selectedRegion)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    },
                    color = MaterialTheme.colorScheme.primary
                )
<<<<<<< HEAD

            }

            HelpBottomSheet(
                navController = navController,
=======
            }

            HelpBottomSheet(
>>>>>>> 0eec2f562a6c5679733228427e18bb9ed3baa46b
                visible = showHelp,
                onDismiss = { showHelp = false },
            )
            AppearanceBottomSheet(
                visible = showAppearance,
                onDismiss = { showAppearance = false },
                fontScaleViewModel = fontScaleViewModel
            )

        }
    }
}

@Composable
fun PanelAnimation() {
    val animationFile = "solarPanel_anim.json"

    val composition by rememberLottieComposition(
        LottieCompositionSpec.Asset(animationFile)
    )

    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever,
    )

    Box(
        modifier = Modifier
            .height(100.dp)
    ) {
        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier
                .width(130.dp)
                .aspectRatio(400.dp / 1000.dp),
        )
    }
}

@Composable
<<<<<<< HEAD
fun ElectricityTowers() {

    val isDark = when (ThemeState.themeMode) {
        ThemeMode.DARK -> true
        ThemeMode.LIGHT -> false
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
    }


    val animationFile =
        if (isDark) "electricity_tower_dark.json" else "electricity_tower_light.json"
=======
fun LightningAnimation() {
    val animationFile = "lightningBolt_anim.json"
>>>>>>> 0eec2f562a6c5679733228427e18bb9ed3baa46b

    val composition by rememberLottieComposition(
        LottieCompositionSpec.Asset(animationFile)
    )

    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever,
    )

    Box(
        modifier = Modifier
<<<<<<< HEAD
//            .background(Color.Blue)
        ,
        contentAlignment = Alignment.Center
=======
//        .background(color = Color.Blue)
            .height(500.dp)
>>>>>>> 0eec2f562a6c5679733228427e18bb9ed3baa46b
    ) {
        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier
<<<<<<< HEAD
                .width(150.dp)
                .aspectRatio(400.dp / 400.dp),
//                .background(Color.Red)
=======
                .size(150.dp)

>>>>>>> 0eec2f562a6c5679733228427e18bb9ed3baa46b
        )
    }
}

<<<<<<< HEAD
@Composable
fun SunAnimation(value: Double) {
    val animationFile = when {
        value < 0.03 -> "solar_verylow.json"
        value in 0.03..0.1 -> "solar_low.json"
        value in 0.1..0.3 -> "solar_half.json"
        value > 0.3 -> "solar_full.json"
        else -> "solar_verylow.json" // Default animation
    }

    // Force new composition when value changes
    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.Asset(animationFile),
    )

    // Reset animation state when value changes
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        // Add a key to restart animation when value changes
    )

    LottieAnimation(
        composition = composition,
        progress = { progress },
        modifier = Modifier
            .height(450.dp)
            .fillMaxWidth()
            .graphicsLayer(
                scaleX = 1.8f,
                scaleY = 1.8f
            )
    )
    Log.d("SunAnimation", "Animating with value: $value")
}

=======
>>>>>>> 0eec2f562a6c5679733228427e18bb9ed3baa46b

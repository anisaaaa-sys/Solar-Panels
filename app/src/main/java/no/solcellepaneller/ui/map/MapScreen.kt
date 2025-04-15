package no.solcellepaneller.ui.map

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.Polygon
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.coroutines.launch
import no.solcellepaneller.R
import no.solcellepaneller.ui.font.FontScaleViewModel
import no.solcellepaneller.ui.navigation.AdditionalInputBottomSheet
import no.solcellepaneller.ui.navigation.AppearanceBottomSheet
import no.solcellepaneller.ui.navigation.BottomBar
import no.solcellepaneller.ui.navigation.HelpBottomSheet
import no.solcellepaneller.ui.navigation.TopBar
import no.solcellepaneller.ui.result.WeatherViewModel

@Composable
fun MapScreen(
    viewModel: MapScreenViewModel,
    navController: NavController,
    fontScaleViewModel: FontScaleViewModel,
    weatherViewModel: WeatherViewModel,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val trigger by viewModel.snackbarMessageTrigger
    var lastShownTrigger by remember { mutableIntStateOf(0) }
    var showHelp by remember { mutableStateOf(false) }
    var showAppearance by remember { mutableStateOf(false) }

    LaunchedEffect(trigger) {
        if (trigger > lastShownTrigger) {
            snackbarHostState.showSnackbar("Address not found, try again")
            lastShownTrigger = trigger
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopBar(
                navController = navController,
                text = stringResource(id = R.string.map_title)
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
        DisplayScreen(
            viewModel = viewModel,
            navController = navController,
            weatherViewModel = weatherViewModel,
            snackbarHostState = snackbarHostState,
            modifier = Modifier.padding(contentPadding)
        )
    }

    HelpBottomSheet(visible = showHelp, onDismiss = { showHelp = false })
    AppearanceBottomSheet(
        visible = showAppearance,
        onDismiss = { showAppearance = false },
        fontScaleViewModel = fontScaleViewModel
    )
}

@Composable
fun InputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    colors: TextFieldColors,
    viewModel: MapScreenViewModel,
    address: String,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        colors = colors,
        singleLine = true,
        modifier = Modifier
            .width(300.dp)
            .padding(bottom = 10.dp, start = 10.dp),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
            onDone = {
                viewModel.fetchCoordinates(address)
            }
        ),
    )
}

@Composable
private fun DisplayScreen(
    viewModel: MapScreenViewModel,
    navController: NavController,
    weatherViewModel: WeatherViewModel,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedCoordinates by remember { mutableStateOf<LatLng?>(null) }
    val markerState = rememberMarkerState(position = selectedCoordinates ?: LatLng(0.0, 0.0))
    val coroutineScope = rememberCoroutineScope()
    var address by remember { mutableStateOf("") }
    val coordinates by viewModel.coordinates.observeAsState()
    val polygonPoints = viewModel.polygondata
    var isPolygonVisible by remember { mutableStateOf(false) }
    var drawingEnabled by remember { mutableStateOf(false) }
    var showBottomSheet by remember { mutableStateOf(false) }
    var showMissingLocationDialog by remember { mutableStateOf(false) }

    // Location permission state
    var locationPermissionGranted by remember { mutableStateOf(false) }
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        locationPermissionGranted = isGranted
        if (!isGranted) {
            coroutineScope.launch {
                snackbarHostState.showSnackbar("Location permission required")
            }
        }
    }

    // Camera and map state
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(59.9436145, 10.7182883), 18f)
    }
    val mapUiSettings = remember { MapUiSettings() }

    // Check initial permission
    LaunchedEffect(Unit) {
        locationPermissionGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    Box(modifier = modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(0f),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(
                mapType = MapType.HYBRID,
                isMyLocationEnabled = locationPermissionGranted
            ),
            uiSettings = mapUiSettings.copy(
                scrollGesturesEnabled = !drawingEnabled,
                zoomGesturesEnabled = !drawingEnabled
            ),
            onMapClick = { latLng ->
                if (drawingEnabled) {
                    viewModel.addPoint(latLng)
                } else {
                    selectedCoordinates = latLng
                    viewModel.selectLocation(latLng.latitude, latLng.longitude)
                    coroutineScope.launch {
                        cameraPositionState.animate(
                            CameraUpdateFactory.newLatLngZoom(latLng, cameraPositionState.position.zoom)
                        )
                    }
                }
            }
        ) {
            // Map markers and polygons
            if (!drawingEnabled) {
                selectedCoordinates?.let {
                    markerState.position = it
                    Marker(
                        state = markerState,
                        title = stringResource(id = R.string.selected_position),
                        snippet = "Lat: ${it.latitude}, Lng: ${it.longitude}"
                    )
                }
            } else {
                polygonPoints.forEachIndexed { i, point ->
                    Marker(
                        state = rememberMarkerState(position = point),
                        title = "${stringResource(id = R.string.point)} ${i + 1}"
                    )
                }
                if (isPolygonVisible) {
                    Polygon(
                        points = polygonPoints,
                        fillColor = Green.copy(0.3f),
                        strokeColor = Green,
                        strokeWidth = 5f
                    )
                }
            }
        }

        LaunchedEffect(coordinates) {
            coordinates?.let { (lat, lon) ->
                val newLatLng = LatLng(lat, lon)
                selectedCoordinates = newLatLng
                cameraPositionState.animate(
                    CameraUpdateFactory.newCameraPosition(
                        CameraPosition.fromLatLngZoom(newLatLng, 18f)
                    )
                )
            }
        }

        // Current Location Button
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 110.dp)
        ) {
            Button(
                onClick = {
                    if (locationPermissionGranted) {
                        getCurrentLocation(context = context, onSuccess = { latLng ->
                                selectedCoordinates = latLng
                                viewModel.selectLocation(latLng.latitude, latLng.longitude)
                                coroutineScope.launch {
                                    cameraPositionState.animate(
                                        CameraUpdateFactory.newCameraPosition(
                                            CameraPosition.fromLatLngZoom(latLng, 18f)
                                        )
                                    )
                                }
                            }, onFailure = {
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Could not get current location")
                                }
                            })
                    } else {
                        locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                    }
                },
                modifier = Modifier.size(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            ) {
                Icon(
                    imageVector = Icons.Default.MyLocation,
                    contentDescription = stringResource(id = R.string.current_location)
                )
            }
        }

        // Search Bar
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RectangleShape,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondary
            )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                InputField(
                    value = address,
                    onValueChange = {
                        address = it
                        viewModel.addressFetchError.value = false
                    },
                    address = address,
                    viewModel = viewModel,
                    label = stringResource(id = R.string.enter_address),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = MaterialTheme.colorScheme.secondary,
                        focusedBorderColor = MaterialTheme.colorScheme.tertiary,
                        focusedContainerColor = MaterialTheme.colorScheme.background,
                        focusedLabelColor = MaterialTheme.colorScheme.tertiary,
                        cursorColor = MaterialTheme.colorScheme.tertiary,
                        unfocusedTextColor = MaterialTheme.colorScheme.tertiary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.tertiary,
                    ),
//                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
//                    keyboardActions = KeyboardActions(
//                        onDone = {
//                            keyboardController?.hide()
//                            viewModel.fetchCoordinates(address)
//                        }
//                    )
                )

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp),
                    shape = RectangleShape,
                    onClick = {
                        viewModel.fetchCoordinates(address)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = stringResource(id = R.string.search_coordinates),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        // Confirm Location Button
        if (!drawingEnabled) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 80.dp)
            ) {
                Button(
                    onClick = {
                        if (coordinates != null) {
                            showBottomSheet = true
                            selectedCoordinates = null
                            viewModel.removePoints()
                        } else {
                            showMissingLocationDialog = true
                        }
                    }
                ) {
                    Text(stringResource(id = R.string.confirm_location))
                }
            }
        }

        // Drawing controls
        if (drawingEnabled) {
            DrawingControls(
                polygonPoints = polygonPoints,
                viewModel = viewModel,
                showBottomSheet = { showBottomSheet = true },
                onCancel = {
                    drawingEnabled = false
                    isPolygonVisible = false
                    viewModel.removePoints()
                },
                onToggleVisibility = { isPolygonVisible = !isPolygonVisible }
            )
        }

        // Dialogs and bottom sheets
        if (showMissingLocationDialog) {
            LocationNotSelectedDialog(onDismiss = { showMissingLocationDialog = false })
        }

        AdditionalInputBottomSheet(
            visible = showBottomSheet,
            onDismiss = { showBottomSheet = false },
            onStartDrawing = {
                drawingEnabled = true
                selectedCoordinates = null
                viewModel.removePoints()
            },
            coordinates = coordinates,
            area = viewModel.calculateAreaOfPolygon(polygonPoints).toString(),
            navController = navController,
            viewModel = viewModel,
            weatherViewModel = weatherViewModel
        )
    }
}


@Composable
fun LocationNotSelectedDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(id = R.string.no_location_title)) },
        text = { Text(stringResource(id = R.string.no_location_message)) },
        confirmButton = {},
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(stringResource(id = R.string.dismiss))
            }
        }
    )
}

fun getCurrentLocation(
    context: Context,
    onSuccess: (LatLng) -> Unit,
    onFailure: () -> Unit
) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    if (ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                location?.let {
                    onSuccess(LatLng(it.latitude, it.longitude))
                } ?: run {
                    onFailure()
                }
            }
            .addOnFailureListener { onFailure() }
    } else {
        onFailure()
    }
}

@Composable
private fun DrawingControls(
    polygonPoints: List<LatLng>,
    viewModel: MapScreenViewModel,
    showBottomSheet: () -> Unit,
    onCancel: () -> Unit,
    onToggleVisibility: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
        ) {
            var areaShown by remember { mutableStateOf(false) }

            Button(
                onClick = onCancel,
                colors = ButtonDefaults.buttonColors(Color.Gray)
            ) {
                Text(stringResource(id = R.string.cancel))
            }

            if (polygonPoints.size > 2) {
                Button(
                    onClick = {
                        onToggleVisibility()
                        areaShown = true
                    },
                    colors = ButtonDefaults.buttonColors(Color(0xFF4CAF50))
                ) {
                    Text(stringResource(id = R.string.show_area))
                }

                if (areaShown) {
                    Button(onClick = showBottomSheet) {
                        Text(stringResource(id = R.string.confirm_drawing))
                    }
                }
            }

            if (polygonPoints.isNotEmpty()) {
                Row {
                    Button(
                        onClick = { viewModel.removeLastPoint() },
                        colors = ButtonDefaults.buttonColors(Color.Yellow),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(stringResource(id = R.string.remove_last_point))
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = { viewModel.removePoints() },
                        colors = ButtonDefaults.buttonColors(Red),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(stringResource(id = R.string.remove_points))
                    }
                }
            }
        }
    }
}
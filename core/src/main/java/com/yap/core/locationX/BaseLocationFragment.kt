package com.yap.core.locationX

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import android.os.Bundle
import android.os.Looper
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.yap.core.base.BaseNavFragment
import com.yap.core.base.interfaces.IBase
import com.yap.permissionx.PermissionX
import timber.log.Timber

private const val REQUEST_CHECK_SETTINGS = 12
private const val REQUEST_FOR_GPS = 13
private const val UPDATE_INTERVAL = 10000 // 10 sec
private const val FASTEST_INTERVAL = 5000 // 5 sec
private const val DISPLACEMENT = 10 // 10 meters

abstract class BaseLocationFragment<VB : ViewDataBinding, VS : IBase.State, VM : IBase.ViewModel<VS>>(
    @LayoutRes val layoutResId: Int
) : IBase.View<VM>, BaseNavFragment<VB, VS, VM>(layoutResId) {
    protected var isPermissionsGranted = false

    private lateinit var placesClient: PlacesClient
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    protected var latitude: Double? = null
    protected var longitude: Double? = null
    private lateinit var mLocationSettingsRequest: LocationSettingsRequest
    open var mRequestingLocationUpdates = true
    private var mLocationRequest: LocationRequest? = null
    open var mShowSettingDialog = true

    /**
     * Callback for Location events.
     */
    private var mLocationCallback: LocationCallback? = null

    /**
     * Represents a geographical location.
     */
    protected var mCurrentLocation: Location? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        createLocationCallback()
        createLocationRequest()
        buildLocationSettingsRequest()
        checkPermission(
            listOf(
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            isPermissionsGranted = true
            checkLocationSettings()
        }
    }

    private fun setupPlacesClient(apiKey: String) {
        Places.initialize(requireContext(), apiKey)
        placesClient = Places.createClient(requireContext())
    }

    private fun checkPermission(permissionsList: List<String>, function: () -> Unit) {
        PermissionX.init(this).permissions(
            permissionsList
        ).onForwardToSettings { scope, deniedList ->
            scope.showForwardToSettingsDialog(
                deniedList,
                locationPermissionDeniedMessage(),
                locationPermissionOpenSettingText()
            )
        }.request { allGranted, grantedList, deniedList ->
            if (allGranted) {
                function.invoke()
            } else {
                showToast(permissionDeniedCommonMessage())
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            REQUEST_CHECK_SETTINGS -> when (resultCode) {
                Activity.RESULT_OK -> startLocationUpdates()
                Activity.RESULT_CANCELED -> mShowSettingDialog = false
            }// Nothing to do. startLocationupdates() gets called in onResume again.
            REQUEST_FOR_GPS -> {
                if (isPermissionsGranted) {
                    mRequestingLocationUpdates = true
                    checkLocationSettings()
                }
            }
        }
    }


    private fun buildLocationSettingsRequest() {
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(mLocationRequest!!)
        builder.setAlwaysShow(false)
        builder.setNeedBle(false)
        mLocationSettingsRequest = builder.build()
    }


    /**
     * Creates a callback for receiving location events.
     */
    private fun createLocationCallback() {
        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                mCurrentLocation = locationResult.lastLocation
                latitude = mCurrentLocation?.latitude
                longitude = mCurrentLocation?.longitude

                onLocationAvailable(mCurrentLocation)

            }

            override fun onLocationAvailability(locationResults: LocationAvailability) {
                super.onLocationAvailability(locationResults)
            }
        }
    }

    /**
     * Sets up the location request. Android has two location request settings:
     * {@code ACCESS_COARSE_LOCATION} and {@code ACCESS_FINE_LOCATION}. These settings control
     * the accuracy of the current location. This sample uses ACCESS_FINE_LOCATION, as defined in
     * the AndroidManifest.xml.
     * <p/>
     * When the ACCESS_FINE_LOCATION setting is specified, combined with a fast update
     * interval (5 seconds), the Fused Location Provider API returns location updates that are
     * accurate to within a few feet.
     * <p/>
     * These settings are appropriate for mapping applications that show real-time location
     * updates.
     */
    protected open fun createLocationRequest() {
        mLocationRequest = LocationRequest.create().apply {
            interval = UPDATE_INTERVAL.toLong()
            fastestInterval = FASTEST_INTERVAL.toLong()
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            smallestDisplacement = DISPLACEMENT.toFloat()
        }
    }

    private fun checkLocationSettings() {
        val settingsClient = LocationServices.getSettingsClient(requireActivity())
        val task = settingsClient.checkLocationSettings(mLocationSettingsRequest)
        task.addOnCompleteListener { t -> locationSettingsResponseOnComplete(t) }
    }

    private fun locationSettingsResponseOnComplete(t: Task<LocationSettingsResponse>) {
        try {
            startLocationUpdates()
        } catch (exception: ApiException) {
            when (exception.statusCode) {
                LocationSettingsStatusCodes.SUCCESS -> {
                    startLocationUpdates()
                }
                LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                    try {
                        val resolvable = exception as ResolvableApiException
                        // Show the dialog by calling startResolutionForResult(), and check the result
                        // in onActivityResult().
                        if (mShowSettingDialog) {

                            resolvable.startResolutionForResult(
                                requireActivity(),
                                REQUEST_CHECK_SETTINGS
                            )
                        }
                    } catch (e: IntentSender.SendIntentException) {
//                        Log.i(TAG, "PendingIntent unable to execute request.")
                    }

                }
                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE ->
                    Timber.i("Location settings are inadequate, and cannot be fixed here. Dialog not created.")

            }
        }

    }

    protected fun startLocationUpdates() {
        if (!mRequestingLocationUpdates) {
            return
        }

        if (isPermissionsGranted) {
            fusedLocationClient.requestLocationUpdates(
                mLocationRequest!!,
                mLocationCallback!!, Looper.myLooper()
            )
        } else {
            checkPermission(
                listOf(
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                isPermissionsGranted = true
                startLocationUpdates()
            }
        }

    }

    /**
     * Removes location updates from the FusedLocationApi.
     */
    protected fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(mLocationCallback).addOnCompleteListener {}
    }

    protected fun bitmapDescriptorFromVector(vectorResId: Int): BitmapDescriptor? {
        return ContextCompat.getDrawable(requireContext(), vectorResId)?.run {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            val bitmap =
                Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
            draw(Canvas(bitmap))
            BitmapDescriptorFactory.fromBitmap(bitmap)
        }
    }

    override fun onStop() {
        stopLocationUpdates()
        super.onStop()
    }

    abstract fun onLocationAvailable(location: Location?)
    abstract fun locationPermissionDeniedMessage(): String
    abstract fun permissionDeniedCommonMessage(): String
    abstract fun locationPermissionOpenSettingText(): String

}
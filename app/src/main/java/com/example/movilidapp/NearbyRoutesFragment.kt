package com.example.movilidapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.Locale

class NearbyRoutesFragment : Fragment() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var tvReferenceZone: TextView
    private lateinit var llReferenceZone: LinearLayout

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) ||
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                // Precise or approximate location access granted.
                fetchCurrentLocation()
            }
            else -> {
                // No location access granted. Clear everything.
                updateLocationUI("")
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_nearby_routes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        tvReferenceZone = view.findViewById(R.id.tv_reference_zone)
        llReferenceZone = view.findViewById(R.id.ll_reference_zone)

        llReferenceZone.setOnClickListener {
            checkPermissionsAndLocate()
        }
    }

    private fun checkPermissionsAndLocate() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fetchCurrentLocation()
        } else {
            locationPermissionRequest.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    @SuppressLint("MissingPermission")
    private fun fetchCurrentLocation() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    val geocoder = Geocoder(requireContext(), Locale.getDefault())
                    try {
                        val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                        if (!addresses.isNullOrEmpty()) {
                            val address = addresses[0]
                            val city = address.locality ?: address.subAdminArea ?: address.adminArea
                            val state = address.adminArea ?: address.locality
                            
                            val referenceZoneText = if (city != null) "Zona de referencia: $city" else "Zona de referencia: Ubicación actual"
                            val topBarLocationText = state ?: city ?: "Ubicación actual"
                            
                            tvReferenceZone.text = referenceZoneText
                            tvReferenceZone.visibility = View.VISIBLE
                            (requireActivity() as? MainActivity)?.updateLocationText(topBarLocationText)
                        } else {
                            updateLocationUI("")
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        updateLocationUI("")
                    }
                } else {
                    updateLocationUI("")
                }
            }
            .addOnFailureListener {
                updateLocationUI("")
            }
    }

    private fun updateLocationUI(locationText: String) {
        if (locationText.isEmpty()) {
            tvReferenceZone.text = ""
            tvReferenceZone.visibility = View.GONE
            (requireActivity() as? MainActivity)?.updateLocationText("")
        } else {
            tvReferenceZone.text = locationText
            tvReferenceZone.visibility = View.VISIBLE
            (requireActivity() as? MainActivity)?.updateLocationText(locationText)
        }
    }
}
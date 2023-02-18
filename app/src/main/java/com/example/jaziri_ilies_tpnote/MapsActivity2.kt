package com.example.jaziri_ilies_tpnote

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.jaziri_ilies_tpnote.databinding.ActivityMaps2Binding

class MapsActivity2 : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMaps2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMaps2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val stationChoisie: Station = intent.extras!!.get("uneStation") as Station

        //on doit afficher uniquement un marqueur
            val _markerChoisi = LatLng(stationChoisie.getLatitude()!!, stationChoisie.getLongitude()!!)
            val _stationInfoChoisi: String =
                "" + stationChoisie.getNom() + "- " + stationChoisie.emplacementsLibres + " / " + stationChoisie.getEmplacements() + " places."

            //On ajoute un marqueur avec les coordonnées et l'étiquette correspondants.
            mMap.addMarker(MarkerOptions().position(_markerChoisi).title(_stationInfoChoisi))

            //Zoom de la caméra sur le dernier marqueur placé.
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(_markerChoisi, 11.5f))

    }
}
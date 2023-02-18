package com.example.jaziri_ilies_tpnote

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.jaziri_ilies_tpnote.databinding.ActivityMapsBinding

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    /***
     * onMapReady
     * Fonction d'initialisation de la vue.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        //récupération des stations envoyées dans le Intent
        val stationsList: ArrayList<Station> = intent.extras!!.get("stations") as ArrayList<Station>


            // Placer un marqueur pour chaque station
            for (i in 0 until stationsList.size) {
                val _station = stationsList.get(i)
                val _marker = LatLng(_station.getLatitude()!!, _station.getLongitude()!!)
                val _stationInfo: String =
                    "" + _station.getNom() + "- " + _station.emplacementsLibres + " / " + _station.getEmplacements() + " places."

                //On ajoute un marqueur avec les coordonnées et l'étiquette correspondants.
                mMap.addMarker(MarkerOptions().position(_marker).title(_stationInfo))

                //Zoom de la caméra sur le dernier marqueur placé.
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(_marker, 11.5f))
            }

    }
}
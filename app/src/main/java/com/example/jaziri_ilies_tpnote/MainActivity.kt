package com.example.jaziri_ilies_tpnote

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import org.json.JSONObject
import org.json.JSONTokener
import java.io.IOException

class MainActivity : AppCompatActivity() {
    val stationsList = ArrayList<Station>()
    val stationsString = ArrayList<String>()
    val client = OkHttpClient()
    val url = "https://api.agglo-larochelle.fr/production/opendata/api/records/1.0/search/dataset=yelo___disponibilite_des_velos_en_libre_service&facet=station_nom&api-key=a237e560-ca4b-4059-86c6-4f9111b6ae7a"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Mise en place de l'affichage de vélos dans le ListView
        val lvVelos: ListView = findViewById(R.id.lvVelo)
        val adapterVelos = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, stationsString)
        lvVelos.setAdapter(adapterVelos)

        //On va tout d'abord effectuer un appel à l'API Yélo pour récupérer l'état actuel du réseau de vélos.
        syncYelo(adapterVelos)

        //Lorsque l'utilisateur clique sur le bouton, basculer sur la vue MapsActivity avec toutes les stations.
        val buttonMaps: Button = findViewById(R.id.button)
        buttonMaps.setOnClickListener{
                val i = Intent(this, MapsActivity::class.java).apply{
                    putExtra("stations", stationsList)  //Envoyer la liste de stations à l'activity Maps
                }

                startActivity(i)
        }

        //Lorsque l'utilisateur clique sur une station de la ListView, afficher uniquement le marqueur correspondant sur la map.
        lvVelos.onItemClickListener = AdapterView.OnItemClickListener { parent, _, position, _ ->
            val ii = Intent(this, MapsActivity2::class.java).apply{
                val selectedStation = parent.getItemAtPosition(position) as String

                //On va chercher dans la liste d'objets Station la station ayant le même toString que l'item choisi sur la liste.
                for (i in 0 until stationsList.size){
                    if ((stationsList.get(i).toString().substring(3)).equals(selectedStation)){
                        println(stationsList.get(i).toString())
                        putExtra("uneStation", stationsList.get(i))

                    }
                    else {
                        println("Erreur")
                    }
                }

            }
            startActivity(ii)
        }

    }

    /***
     * syncYelo(adapterVelos)
     * - arg1: adapter to update (always 'adapterVelos').
     * Effectue une requête à l'API Yélo (avec OkHttp) et récupère les stations ainsi que les informations les concernant.
     */
    fun syncYelo(adapterVelos: ArrayAdapter<*>){
        val request = Request.Builder().url(url).build()
        println("Requête: $request")
        client.newCall(request).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                println("\n\n\n\n Erreur d'appel à l'api... \n\n\n\n")
            }
            override fun onResponse(call: Call, response: Response) {
                println("Reussite de la requête")

                //Si l'appel réussit, procéder à la mise à jour de l'affichage des stations
                val reponse = JSONTokener(response.body?.string()).nextValue() as JSONObject
                initListStations(reponse)

                //Mise à jour de la vue: affichage des stations et leurs vélos disponibles dans le ListView.
                this@MainActivity.runOnUiThread(java.lang.Runnable {
                    adapterVelos.notifyDataSetChanged()
                })
            }
        })
    }


    /***
     * initListStations(reponse)
     * - arg1: JSONObject with all the data.
     * Créé un objet Station avec ses informations pour chaque ligne renvoyée par la requête.
     */
    fun initListStations(reponse: JSONObject){
        //on récupère tous les JSONObjects Station
        val jsonStations = reponse.getJSONArray("records")
        for (i in 0 until jsonStations.length())
        {
            //Dans les variables suivantes, on catégorise les informations reçues de la réponse JSON.
            var _station = jsonStations.getJSONObject(i)
            var _stationId = _station.getString("recordid")
            var _stationInfo = _station.getJSONObject("fields")
            var _stationNom = _stationInfo.getString("station_nom").substring(3)
            var _stationEmplacements = _stationInfo.getString("nombre_emplacements")
            var _stationLibres = _stationInfo.getString("accroches_libres")
            var _stationLongitude = _stationInfo.getString("station_longitude")
            var _stationLatitude = _stationInfo.getString("station_latitude")

            //Une fois les informations récupérées, on les attribue à un nouvel objet Station.
            var _nouvelleStation: Station? = Station(_stationNom, _stationEmplacements.toInt(), _stationLongitude.toDouble(), _stationLatitude.toDouble())
            _nouvelleStation!!.emplacementsLibres = _stationLibres.toInt()
            _nouvelleStation!!.velosDisponibles = (_stationEmplacements.toInt() - _stationLibres.toInt())

            //On ajoute l'objet _nouvelleStation à la liste d'objets Station.
            stationsList.add(_nouvelleStation)

            //On ajoute la ligne à afficher dans le ListView pour _nouvelleStation
            stationsString.add(_nouvelleStation.toString())
        }


    }

}
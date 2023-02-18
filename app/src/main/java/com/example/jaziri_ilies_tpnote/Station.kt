package com.example.jaziri_ilies_tpnote

import android.os.Parcel
import android.os.Parcelable

class Station (private val nom: String?, private val emplacements: Int?, private val longitude: Double?, private val latitude: Double?): Parcelable{

    var emplacementsLibres: Int? = null
    var velosDisponibles: Int? = null

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double
    ) {
        emplacementsLibres = parcel.readValue(Int::class.java.classLoader) as? Int
        velosDisponibles = parcel.readValue(Int::class.java.classLoader) as? Int
    }

    fun getLongitude(): Double?{
        return longitude
    }

    fun getLatitude(): Double?{
        return latitude
    }

    fun getNom(): String?{
        return nom
    }

    fun getEmplacements(): Int?{
        return emplacements
    }

    override fun toString(): String {
        if (emplacementsLibres!! > 1){
            return "$nom - $emplacementsLibres places libres sur $emplacements"
        } else {
            return "$nom - $emplacementsLibres place libre sur $emplacements"
        }

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(nom)
        parcel.writeValue(emplacements)
        parcel.writeValue(longitude)
        parcel.writeValue(latitude)
        parcel.writeValue(emplacementsLibres)
        parcel.writeValue(velosDisponibles)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Station> {
        override fun createFromParcel(parcel: Parcel): Station {
            return Station(parcel)
        }

        override fun newArray(size: Int): Array<Station?> {
            return arrayOfNulls(size)
        }
    }
}
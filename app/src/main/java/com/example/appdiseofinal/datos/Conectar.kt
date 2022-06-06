package com.example.appdiseofinal.datos

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

const val url = "http://iesayala.ddns.net/fmartin/"
interface Conectar {
    @GET("Select_Campeones.php/")
    fun info(): Call<Liga>
}

object InstanciaPeli{
    val pInterfa : Conectar

    init {
        val retro = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        pInterfa = retro.create(Conectar::class.java)
    }



}
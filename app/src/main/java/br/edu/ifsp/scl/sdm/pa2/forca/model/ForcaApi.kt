package br.edu.ifsp.scl.sdm.pa2.forca.model

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ForcaApi {

    @GET("identificadores/{nivel}")
    fun retrieveIdentificadores(@Path("nivel") id: Int): Call<Dificuldade>

    @GET("palavra/{id}")
    fun retrievePalavra(@Path("id") id: Int): Call<Palavras>

}
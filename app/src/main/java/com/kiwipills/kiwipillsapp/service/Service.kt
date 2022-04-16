package com.kiwipills.kiwipillsapp.service

import com.kiwipills.kiwipillsapp.service.Models.User
import retrofit2.Call
import retrofit2.http.*

//Retrofi usa una interface para hacer la petición hacia el servidor
interface Service {

    /*
    @GET("Album/Albums")
    fun getAlbums():Call<List<Album>>

    @GET("Album/Albums/{id}")
    fun getAlbum(@Path("id") id: Int): Call<List<Album>>

     */

    //Servicios para consumir
    @Headers("Content-Type: application/json")
    @POST("registro.php")
    fun signup(@Body userData: User): Call<User>
}
package com.kiwipills.kiwipillsapp.service

import com.kiwipills.kiwipillsapp.service.Models.Medicament
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
    //Registrar usuario
    @Headers("Content-Type: application/json")
    @POST("user/signup")
    fun signup(@Body userData: User): Call<User>

    //Iniciar sesion
    @Headers("Content-Type: application/json")
    @POST("user/login")
    fun login(@Body userData: User): Call<User>

    //Editar perfil
    @Headers("Content-Type: application/json")
    @POST("user/editProfile")
    fun updateprofile(@Body userData: User): Call<User>

    //Agregar medicamento
    @Headers("Content-Type: application/json")
    @POST("medicament/add")
    fun addMedicament(@Body medicamentData: Medicament): Call<Int>

    //Obtener medicamentos de usuario
    @GET("medicament/getAll&user_id={user_id}")
    fun getMedicaments(@Path("user_id") user_id: Int):Call<List<Medicament>>
}
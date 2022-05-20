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

    //Obtener medicamentos de usuario por día
    @GET("medicament/getByDay&user_id={user_id}&day={day}")
    fun getMedicamentsByDay(@Path("user_id") user_id: Int, @Path("day") day: Int):Call<List<Medicament>>

    //Borrar medicamento
    @Headers("Content-Type: application/json")
    @POST("medicament/delete")
    fun deleteMedicament(@Body med_id: Int):Call<Int>

    //Obtener medicamentos de usuario
    @GET("medicament/getDrafts&user_id={user_id}")
    fun getDrafts(@Path("user_id") user_id: Int):Call<List<Medicament>>

    //Publicar medicamento
    @Headers("Content-Type: application/json")
    @POST("medicament/publishDraft")
    fun publishDraft(@Body med_id: Int):Call<Int>

    //Obtener medicamento
    @GET("medicament/get&med_id={med_id}")
    fun getMed(@Path("med_id") med_id: Int):Call<Medicament>
}
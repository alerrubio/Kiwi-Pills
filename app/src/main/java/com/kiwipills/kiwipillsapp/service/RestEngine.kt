package com.kiwipills.kiwipillsapp.service

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


//Sirve para definir nuestro
//Retrofit se comopone de dos partes
//rest Adapter configuración inicial del objecto retrofit
//La interface rest

//Esta clase configura  el rest Adapter, definindo su configuración inicial
class RestEngine{
    // nos permite acceder sin instanciar el objecto
    companion object{
        fun getRestEngine(): Retrofit {
            //Creamos el interceptor
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

            val client =  OkHttpClient.Builder().addInterceptor(interceptor).build()
            val retrofit =  Retrofit.Builder()
                .baseUrl("http://www.leonardosantosgrc.com/leo.api/") // tu url
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

            return  retrofit

        }
    }
}
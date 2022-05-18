package com.kiwipills.kiwipillsapp.service

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


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

            /*
            val builder =  OkHttpClient.Builder()

            builder.connectTimeout(30, TimeUnit.SECONDS)// connect timeout
                //.writeTimeout(200, TimeUnit.SECONDS) // write timeout
                //.readTimeout(200, TimeUnit.SECONDS) // read timeout

            val client = builder.addInterceptor(interceptor).build()
            */

            val retrofit =  Retrofit.Builder()
                .baseUrl("http://192.168.200.135:8080/Kiwipills/") // tu url
                //.baseUrl("http://kiwipills.colorit.host/") // tu url
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

            return  retrofit

        }
    }
}
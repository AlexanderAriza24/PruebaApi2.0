package com.example.pruebaapi20

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiServices {

    @GET("api/Producto")
    fun getAllProductos(): Call<List<Producto>>

    @GET("api/Producto/Buscar/{codigo}")
    fun getProductoById(@Path("codigo") codigo: String): Call<Producto>

    @POST("api/Usuario/InicioSesion")
    fun inicioSesion(@Body userData: Usuario): Call<Usuario>
}
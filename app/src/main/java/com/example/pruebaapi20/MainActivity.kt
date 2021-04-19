package com.example.pruebaapi20

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etCodigo = findViewById<EditText>(R.id.etCodigoBuscar)
        val btnIniciar = findViewById<Button>(R.id.btnIniciarSesion)
        val btnBuscar = findViewById<Button>(R.id.btnBuscar)
        btnBuscar.setOnClickListener { consultar() }
        btnIniciar.setOnClickListener { registrar() }
    }
    
    fun prueba(){
        Toast.makeText(this, "Buena esa Papa", Toast.LENGTH_SHORT).show()
        
    }

    fun confirmacion(productos: List<Producto>?){
        Toast.makeText(this, "Buena esa papa", Toast.LENGTH_SHORT).show()
        for (product in productos.orEmpty()){

            val tvCodigo = findViewById<TextView>(R.id.tvCodigo)
            val tvNombre = findViewById<TextView>(R.id.tvNombre)
            val tvCategoria = findViewById<TextView>(R.id.tvCategoria)
            tvCodigo.setText(product.codigo)
            tvNombre.setText(product.nombre)
            tvCategoria.setText(product.categoria)

        }
    }

    fun registrar(){
        val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl("https://distribuidoraesb.azurewebsites.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val service = retrofit.create<ApiServices>(ApiServices::class.java)

        var usuario = Usuario(correo = "admin@admin.com", contraseña = "123", idPersona = null, rol = null, token = null)
        service.inicioSesion(usuario).enqueue(object : Callback<Usuario> {
            override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
                val user = response.body()
                Log.i("TAG_LOGS", Gson().toJson(user))
                var idPersona = findViewById<EditText>(R.id.etUsuario)
                var rol = findViewById<EditText>(R.id.etContraseña)

                idPersona.setText(user?.idPersona)
                rol.setText(user?.rol)
            }

            override fun onFailure(call: Call<Usuario>, t: Throwable) {
                t?.printStackTrace()
            }

        })
    }

    fun consultar(){
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://distribuidoraesb.azurewebsites.net/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create<ApiServices>(ApiServices::class.java)

        service.getAllProductos().enqueue(object : Callback<List<Producto>> {
            override fun onResponse(
                call: Call<List<Producto>>,
                response: Response<List<Producto>>
            ) {
                val productos = response?.body()
                Log.i("TAG_LOGS", Gson().toJson(productos))
                confirmacion(productos)
            }

            override fun onFailure(call: Call<List<Producto>>, t: Throwable) {
                t?.printStackTrace()
                prueba()
            }

        })
    }

    fun buscar(codigo: String){
        val tvCodigo = findViewById<TextView>(R.id.tvCodigo)
        val tvNombre = findViewById<TextView>(R.id.tvNombre)
        val tvCategoria = findViewById<TextView>(R.id.tvCategoria)

        val gson = GsonBuilder()
            .setLenient()
            .create()

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://distribuidoraesb.azurewebsites.net/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        val service = retrofit.create<ApiServices>(ApiServices::class.java)

        var product: Producto?
        service.getProductoById(codigo).enqueue(object : Callback<Producto> {
            override fun onResponse(call: Call<Producto>?, response: Response<Producto>?) {
                product = response?.body()
                Log.i("TAG_LOGS", Gson().toJson(product))

                tvCodigo.setText(product?.codigo)
                tvNombre.setText(product?.nombre)
                tvCategoria.setText(product?.categoria)
            }

            override fun onFailure(call: Call<Producto>?, t: Throwable) {
                t?.printStackTrace()
                prueba()
            }

        })

    }
}
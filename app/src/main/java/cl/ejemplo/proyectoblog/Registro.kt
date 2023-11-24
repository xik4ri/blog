package cl.ejemplo.proyectoblog

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class Registro : AppCompatActivity() {
    lateinit var correo: EditText
    lateinit var kontrasenia: EditText
    lateinit var nombre: EditText
    lateinit var descripcion: EditText
    lateinit var contacto: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        correo = findViewById(R.id.correoTxt)
        kontrasenia = findViewById(R.id.contraTxt)
        nombre = findViewById(R.id.nombreTxt)
        descripcion = findViewById(R.id.descTxt)
        contacto = findViewById(R.id.contactoTxt)

        var reg: Button = findViewById(R.id.registrarseBtn)
        reg.setOnClickListener {
            if(testDatos()){
                val url = "https://jsoza.ilab.cl/DAM2023/sebastian.pradenas/php/Registrarse.php"
                val stringRequest = object : StringRequest(

                    Request.Method.POST, url, Response.Listener {
                            response ->
                        if (response.trim() == "registro exitoso") {
                            Toast.makeText(this,"registro exitoso", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()


                        }
                        else if(response.trim() == "error al registrarse"){
                            Toast.makeText(this,"no se pudo registrar", Toast.LENGTH_SHORT).show()

                        }
                    },
                    Response.ErrorListener { error ->
                        Toast.makeText(this,"error de servidor", Toast.LENGTH_SHORT).show()
                        Toast.makeText(this,error.toString(), Toast.LENGTH_SHORT).show()
                    }
                ) {
                    override fun getParams(): MutableMap<String, String> {
                        val params = HashMap<String, String>()
                        params["correo"] = correo.text.toString()
                        params["contrasenia"] = kontrasenia.text.toString()
                        params["nombre"] = nombre.text.toString()
                        params["descripcion"] = descripcion.text.toString()
                        params["contacto"] = contacto.text.toString()
                        return params
                    }
                }
                val requestQueue = Volley.newRequestQueue(this)
                requestQueue.add(stringRequest)
            }  else{

                Toast.makeText(this,"no a escrito nada", Toast.LENGTH_SHORT).show()

            }


        }

    }
    fun testDatos():Boolean{

        var respuesta = true

        if( correo.text.isEmpty() || kontrasenia.text.isEmpty() || nombre.text.isEmpty() || descripcion.text.isEmpty() || contacto.text.isEmpty()){
            respuesta = false
        }
        return respuesta

    }






    }

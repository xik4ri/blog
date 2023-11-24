package cl.ejemplo.proyectoblog

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView
import android.widget.Toast
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

class MainActivity : AppCompatActivity() {
    companion object {
        lateinit var requestQueue: RequestQueue
    }
    lateinit var usuario: EditText
    lateinit var contrasenia: EditText




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestQueue = Volley.newRequestQueue(this)




        var iniciar: Button = findViewById(R.id.login)
        var registrarse: Button = findViewById(R.id.BotonRegistrarse)

        usuario = findViewById(R.id.usuario)
        contrasenia = findViewById(R.id.contrasenia)


        iniciar.setOnClickListener {

            if (testDatos()){
                val url = "https://jsoza.ilab.cl/DAM2023/sebastian.pradenas/php/validar_usuario.php"

                val stringRequest = object : StringRequest(

                    Request.Method.POST, url, Response.Listener {
                        response ->
                        if (response.trim() == "Inicio de sesion exitoso") {
                            val intent = Intent(this, PrincipalMainActivity::class.java)
                            val userio = usuario.text.toString()
                            intent.putExtra("usuarioK", userio)

                            Toast.makeText(this,"iniciando...", Toast.LENGTH_SHORT).show()

                            startActivity(intent)
                            finish()
                        }
                        else if(response.trim() == "Inicio de sesion fallido"){
                            Toast.makeText(this,"valores incorrectos", Toast.LENGTH_SHORT).show()
                            Toast.makeText(this,response, Toast.LENGTH_SHORT).show()

                        }

                    },
                    Response.ErrorListener { error ->
                        Toast.makeText(this,"error de servidor", Toast.LENGTH_SHORT).show()

                    }
                ) {
                    override fun getParams(): MutableMap<String, String> {
                        val params = HashMap<String, String>()
                        params["correo"] = usuario.text.toString()
                        params["contrasenia"] = contrasenia.text.toString()
                        return params
                    }
                }
                val requestQueue = Volley.newRequestQueue(this)
                requestQueue.add(stringRequest)
            }else{
                Toast.makeText(this,"todos los datos son obligatorios", Toast.LENGTH_SHORT).show()
            }
        }
        registrarse.setOnClickListener {
            val intent = Intent(this, Registro::class.java)
            startActivity(intent)

        }



    }
    fun testDatos():Boolean{

        var respuesta = true

        if( usuario.text.isEmpty() ||  contrasenia.text.isEmpty()){

            respuesta = false
        }
        return respuesta

    }
}
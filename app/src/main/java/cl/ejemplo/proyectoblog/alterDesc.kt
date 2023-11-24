package cl.ejemplo.proyectoblog

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class alterDesc : AppCompatActivity() {

    lateinit var descripcion: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alter_desc)
        val bundle: Intent = intent
        val usuarioRecibido = bundle.getStringExtra("usuarioK")
        var btn = findViewById<Button>(R.id.btndesc)
        descripcion = findViewById(R.id.desc)




        btn.setOnClickListener {
            if (testDatos()) {
                val url = "https://jsoza.ilab.cl/DAM2023/sebastian.pradenas/php/datosuser.php"
                val stringRequest = object : StringRequest(
                    Request.Method.POST, url, Response.Listener { response ->
                            Toast.makeText(this, "cambio exitoso", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, user::class.java)
                            startActivity(intent)
                            finish()
                    },
                    Response.ErrorListener { error ->
                        Toast.makeText(this, "error de servidor", Toast.LENGTH_SHORT).show()
                        Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show()
                    }
                ) {
                    override fun getParams(): MutableMap<String, String> {
                        val params = HashMap<String, String>()
                        params["usuario"] = usuarioRecibido.toString()
                        params["nueva_desc"] = descripcion.text.toString()
                        return params
                    }
                }
                val requestQueue = Volley.newRequestQueue(this)
                requestQueue.add(stringRequest)
            } else {

                Toast.makeText(this, "no a escrito nada", Toast.LENGTH_SHORT).show()

            }

        }

    }
    fun testDatos(): Boolean {

        var respuesta = true

        if (descripcion.text.isEmpty()) {
            respuesta = false
        }
        return respuesta

    }
}
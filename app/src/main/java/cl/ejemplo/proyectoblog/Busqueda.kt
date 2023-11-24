package cl.ejemplo.proyectoblog

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray

class Busqueda : AppCompatActivity() {
    private val listOfPosts = mutableListOf<String>()

    private lateinit var linearLayout: LinearLayout
    lateinit var terminoBuscado: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_busqueda)


        linearLayout = findViewById(R.id.buscarPost)
        terminoBuscado = findViewById(R.id.buscador)

        val buscarButton = findViewById<Button>(R.id.buscar)


        buscarButton.setOnClickListener {
            if (testDatos()) {
            val url = "https://jsoza.ilab.cl/DAM2023/sebastian.pradenas/php/buscarPost.php"
            val stringRequest = object : StringRequest(
                Request.Method.POST, url,  {
                    response ->
                    linearLayout.removeAllViews()
                    val postsArray = JSONArray(response)

                    for (i in 0 until postsArray.length()) {
                        val postObject = postsArray.getJSONObject(i)
                        val contenido = postObject.getString("contenido")
                        val correo = postObject.getString("usuario_correo")
                        val postView = TextView(this)
                        postView.text = "$correo: \n $contenido \n"
                        val layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        postView.layoutParams = layoutParams
                        linearLayout.addView(postView)
                    }
                },
                { error ->
                    Toast.makeText(this, "Error de conexión", Toast.LENGTH_SHORT).show()
                }
            ) {
                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>()
                    params["termino"] = terminoBuscado.text.toString()
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

        if (terminoBuscado.text.isEmpty()) {

            respuesta = false
        }
        return respuesta

    }


}


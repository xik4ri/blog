package cl.ejemplo.proyectoblog

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONException

class user : AppCompatActivity() {
    private lateinit var linearLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        linearLayout = findViewById(R.id.linearPost)
        var user = findViewById<TextView>(R.id.userView)
        val bundle : Intent = intent
        val usuarioRecibido = bundle.getStringExtra("usuarioK")
        Toast.makeText(this, usuarioRecibido, Toast.LENGTH_SHORT).show()
        user.text = usuarioRecibido
        var ed = findViewById<Button>(R.id.editar)

        obtenerDesc(usuarioRecibido.toString())

        ed.setOnClickListener {
            val intent = Intent(this, alterDesc::class.java)
            val userio = usuarioRecibido.toString()
            intent.putExtra("usuarioK", userio)

            startActivity(intent)

        }
        val url1 = "https://jsoza.ilab.cl/DAM2023/sebastian.pradenas/php/obtenerPostUser.php"
        val stringRequest = object : StringRequest(
            Request.Method.POST, url1,  {
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
                params["termino"] = usuarioRecibido.toString()
                return params

            }
        }
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
    }
    private fun obtenerDesc(usuario: String) {
        val vardesc = findViewById<TextView>(R.id.textviewdesc)

        val url2 = "https://jsoza.ilab.cl/DAM2023/sebastian.pradenas/php/obtenerDesc.php"

        val stringRequest = object : StringRequest(
            Request.Method.POST, url2,
            { response ->
                try {
                    val postsArray = JSONArray(response)
                    if (postsArray.length() > 0) {
                        val postObject = postsArray.getJSONObject(0)
                        val contenido = postObject.getString("descripcion")
                        vardesc.text = contenido
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            { error ->
                Toast.makeText(this, "Error de conexión", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["usuario"] = usuario
                return params
            }
        }
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
    }

}
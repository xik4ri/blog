package cl.ejemplo.proyectoblog

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray

class PrincipalMainActivity : AppCompatActivity() {
    private val listOfPosts = mutableListOf<String>()
    private lateinit var linearLayout: LinearLayout
    lateinit var textoPost: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal_main)
        textoPost = findViewById(R.id.ContPost)
        val bundle : Intent = intent
        val usuarioRecibido = bundle.getStringExtra("usuarioK")

        linearLayout = findViewById(R.id.linearPost)
        val createPostButton = findViewById<Button>(R.id.PublicarButton)
        val buscar = findViewById<Button>(R.id.btnBuscar)
        val perfil = findViewById<Button>(R.id.btnperfil)


        perfil.setOnClickListener {
            val intent = Intent(this, user::class.java)
            val userio = usuarioRecibido.toString()
            intent.putExtra("usuarioK", userio)
            startActivity(intent)


        }


        val url1 = "https://jsoza.ilab.cl/DAM2023/sebastian.pradenas/php/obtenerPost.php"

        val stringRequest = StringRequest(
            Request.Method.GET, url1,
            { response ->
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
        )
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)




        createPostButton.setOnClickListener {
            if(testDatos()){

                val url = "https://jsoza.ilab.cl/DAM2023/sebastian.pradenas/php/ingresarPost.php"
                val stringRequest = object : StringRequest(

                    Request.Method.POST, url, Response.Listener {
                            response ->
                        if (response.trim() == "Post ingresado") {
                            val newPost = "$usuarioRecibido: \n ${textoPost.text} \n"
                            listOfPosts.add(newPost)

                            val postView = TextView(this)
                            postView.text = newPost
                            val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                            postView.layoutParams = layoutParams

                            linearLayout.addView(postView)
                            recreate()

                        }
                        else if(response.trim() == "Error con el Post"){
                            Toast.makeText(this,response, Toast.LENGTH_SHORT).show()

                        }
                    },
                    Response.ErrorListener { error ->
                        Toast.makeText(this,"error de servidor", Toast.LENGTH_SHORT).show()
                        Toast.makeText(this,error.toString(), Toast.LENGTH_SHORT).show()
                    }
                ) {
                    override fun getParams(): MutableMap<String, String> {
                        val params = HashMap<String, String>()
                        params["contenido"] = textoPost.text.toString()
                        params["usuario_correo"] = usuarioRecibido.toString()
                        return params
                    }
                }
                val requestQueue = Volley.newRequestQueue(this)
                requestQueue.add(stringRequest)
            }  else{

            Toast.makeText(this,"no a escrito nada", Toast.LENGTH_SHORT).show()

        }

        }
        buscar.setOnClickListener {
            val intent = Intent(this, Busqueda::class.java)
            startActivity(intent)

        }

    }
    fun testDatos():Boolean{

        var respuesta = true

        if( textoPost.text.isEmpty() ){

            respuesta = false
        }
        return respuesta

    }
}

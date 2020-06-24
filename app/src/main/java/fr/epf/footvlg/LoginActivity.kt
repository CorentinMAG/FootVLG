package fr.epf.footvlg

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import fr.epf.footvlg.API.APIService
import fr.epf.footvlg.models.Member
import fr.epf.footvlg.utils.UserDAO
import fr.epf.footvlg.utils.retrofit
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.Email
import kotlinx.android.synthetic.main.activity_login.password
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    lateinit var user: Member
    val apiService = retrofit().create(APIService::class.java)
    var bool:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //le click sur le bouton S'ENREGISTRER lance l'activité pour enregistrer une personne
        registerButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }



        //le click sur le bouton CONNEXION lance l'activité MainActivity
        // si l'utilisateur arrive à se connecter
        loginButton.setOnClickListener {
            val email = Email.text.toString().trim()
            val password = password.text.toString().trim()
            checkUserCredentials(email,password)
            //val intent = Intent(this, MainActivity::class.java)
            //startActivity(intent)

        }
        //on connecte automatiquement l'utilisateur
        // si les dientifiants fournis par la bdd locale
        // correspondent à ceux fournit par la bdd distante
        checkUserCredentialsBDD()

    }
    private fun checkUserCredentials(email:String,password:String):Boolean{
        val requestCall = apiService.checkMemberManually(email,password)

        requestCall.enqueue(object: Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(applicationContext,"Erreur", Toast.LENGTH_SHORT).show()
                bool = false
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                bool = response.isSuccessful
                if(bool){
                    Toast.makeText(applicationContext,"${response.body()}",Toast.LENGTH_SHORT).show()
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)
                }else{
                    Snackbar.make(loginButton, "Mauvais email / mot de passe", Snackbar.LENGTH_SHORT)
                        .setActionTextColor(Color.MAGENTA)
                        .show()
                }
            }
        })
        return bool
    }

    private fun checkUserCredentialsBDD():Boolean{
        val UserDAO = UserDAO()
        runBlocking {
            user = UserDAO.getUser()
        }
        val requestCall = apiService.checkMember(user)

        requestCall.enqueue(object: Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(applicationContext,"Erreur", Toast.LENGTH_SHORT).show()
                bool = false
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                bool = response.isSuccessful
                if(bool){
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)
                }
            }

        })
        return bool

    }
}

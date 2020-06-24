package fr.epf.footvlg.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import fr.epf.footvlg.API.APIService
import fr.epf.footvlg.MainActivity

import fr.epf.footvlg.R
import fr.epf.footvlg.interfaces.NavigationHost
import fr.epf.footvlg.models.Member
import fr.epf.footvlg.utils.UserDAO
import fr.epf.footvlg.utils.retrofit
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.view.*
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class loginFragment : Fragment() {
    lateinit var user: Member
    val apiService = retrofit().create(APIService::class.java)
    var bool:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkUserCredentialsBDD()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        //le click sur le bouton S'ENREGISTRER lance l'activité pour enregistrer une personne
        view.registerButton.setOnClickListener {
            (activity as NavigationHost).navigateTo(registerFragment(), false) // Navigate to the next Fragment
        }

        //le click sur le bouton CONNEXION lance l'activité MainActivity
        // si l'utilisateur arrive à se connecter
        view.loginButton.setOnClickListener {
            val email = Email.text.toString().trim()
            val password = password.text.toString().trim()
            checkUserCredentials(email,password)

        }

        return view
    }
    private fun checkUserCredentials(email:String,password:String):Boolean{
        val requestCall = apiService.checkMemberManually(email,password)

        requestCall.enqueue(object: Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(context,"Erreur", Toast.LENGTH_SHORT).show()
                bool = false
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                bool = response.isSuccessful
                if(bool){
                    Toast.makeText(context,"${response.body()}", Toast.LENGTH_SHORT).show()
                    (activity as NavigationHost).navigateTo(mainFragment(), false) // Navigate to the next Fragment
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
                Toast.makeText(context,"Erreur", Toast.LENGTH_SHORT).show()
                bool = false
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                bool = response.isSuccessful
                if(bool){
                    (activity as NavigationHost).navigateTo(mainFragment(), false) // Navigate to the next Fragment
                }
            }

        })
        return bool

    }

}


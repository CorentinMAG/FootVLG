package fr.epf.footvlg.fragment

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import fr.epf.footvlg.API.APIService

import fr.epf.footvlg.R
import fr.epf.footvlg.interfaces.NavigationHost
import fr.epf.footvlg.models.Member
import fr.epf.footvlg.utils.UserDAO
import fr.epf.footvlg.utils.retrofit
import kotlinx.android.synthetic.main.activity_login.view.*
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.view.*
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class loginFragment : Fragment() {
    lateinit var user: Member
    val apiService = retrofit().create(APIService::class.java)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login, container, false)


        //le click sur le bouton S'ENREGISTRER lance le fragment pour enregistrer une personne
        view.registerButton.setOnClickListener {
            (activity as NavigationHost).navigateTo(registerFragment(), false) // Navigate to the next Fragment
        }

        //le click sur le bouton CONNEXION lance le fragment main
        // si l'utilisateur arrive à se connecter
        view.loginButton.setOnClickListener {
            val email = Email.text.toString().trim()
            val password = password.text.toString().trim()
            checkUserCredentials(email,password)

        }

        return view
    }
    private fun checkUserCredentials(email:String,password:String):Unit{
        val requestCall = apiService.checkMemberManually(email,password)

        requestCall.enqueue(object: Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(context,"Erreur", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                val bool = response.isSuccessful
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
    }
}



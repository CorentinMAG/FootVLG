package fr.epf.footvlg

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import fr.epf.footvlg.API.APIService
import fr.epf.footvlg.fragment.*
import fr.epf.footvlg.interfaces.NavigationHost
import fr.epf.footvlg.models.Member
import fr.epf.footvlg.utils.UserDAO
import fr.epf.footvlg.utils.retrofit
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity(), NavigationHost {
    var user:Member?=null
    val apiService = retrofit().create(APIService::class.java)
    var bool:Boolean = false

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        bottom_navigation.setSelectedItemId(R.id.home)

        bottom_navigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.account -> {
                    (this as NavigationHost).navigateTo(AccountFragment(), false)
                    true
                } // Navigate to the next Fragment,
                R.id.home ->{
                    (this as NavigationHost).navigateTo(HomeFragment(), false)
                    true
                } // Navigate to the next Fragment,,
                R.id.groups ->{
                    (this as NavigationHost).navigateTo(GroupsFragment(), false)
                    true
                } // Navigate to the next Fragment,
                else -> super.onOptionsItemSelected(it)
            }
        }

        //on charge le loader fragment pour faire patienter l'utilisateur
        (this@LoginActivity as NavigationHost).navigateTo(loaderFragment(), false)

        if(isNetworkConnected()){
            checkUserCredentialsBDD()
        }else{
            AlertDialog.Builder(this).setTitle("Pas de connection internet")
                .setMessage("Vérifier votre connexion internet et réessayer")
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok) { _, _ ->finish() }
                .setIcon(android.R.drawable.ic_dialog_alert).show()
        }
    }
    override fun navigateTo(fragment: Fragment, addToBackstack: Boolean) {
        val transaction = supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragment)

        if (addToBackstack) {
            transaction.addToBackStack(null)
        }

        transaction.commit()
    }

    /**
     * on vérifie que l'utilisateur est bien connecté à internet
     */
    @RequiresApi(Build.VERSION_CODES.M)
    private fun isNetworkConnected(): Boolean {
        //1
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        //2
        val activeNetwork = connectivityManager.activeNetwork
        //3
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        //4
        return networkCapabilities != null &&
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }


    /**
     * on compare les données dans la base locale du téléphone
     * avec celles de la bdd distantes.
     * Si le couple mot de passe / email correspond bien à un utilisateur,
     * le propriétaire du téléphone est directement redirigé vers la page d'accueil.
     */
    private fun checkUserCredentialsBDD(){
        (this@LoginActivity as NavigationHost).navigateTo(loaderFragment(), false)
        val UserDAO = UserDAO()
        runBlocking {
            user = UserDAO.getUser()
        }
        if(user!=null){
            val requestCall = apiService.checkMember(user!!)
            requestCall.enqueue(object: Callback<String> {
                override fun onFailure(call: Call<String>, t: Throwable) {
                    Toast.makeText(this@LoginActivity,"Erreur", Toast.LENGTH_SHORT).show()
                    (this@LoginActivity as NavigationHost).navigateTo(loginFragment(), false) // Navigate to the next Fragment
                }
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    bool = response.isSuccessful
                    if(bool){
                        (this@LoginActivity as NavigationHost).navigateTo(mainFragment(), false) // Navigate to the next Fragment
                    }else{
                        (this@LoginActivity as NavigationHost).navigateTo(loginFragment(), false) // Navigate to the next Fragment
                    }
                }
            })
        }else{
            (this@LoginActivity as NavigationHost).navigateTo(loginFragment(), false) // Navigate to the next Fragment
        }

    }
}

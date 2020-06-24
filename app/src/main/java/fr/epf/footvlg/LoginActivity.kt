package fr.epf.footvlg

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import fr.epf.footvlg.fragment.loginFragment
import fr.epf.footvlg.interfaces.NavigationHost

class LoginActivity : AppCompatActivity(), NavigationHost {

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        if(isNetworkConnected()){
            if (savedInstanceState == null) {
                supportFragmentManager
                    .beginTransaction()
                    .add(R.id.container, loginFragment())
                    .commit()
            }
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
}

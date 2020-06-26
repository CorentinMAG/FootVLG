package fr.epf.footvlg.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import fr.epf.footvlg.API.APIService

import fr.epf.footvlg.R
import fr.epf.footvlg.interfaces.NavigationHost
import fr.epf.footvlg.models.Group
import fr.epf.footvlg.models.Member
import fr.epf.footvlg.utils.UserDAO
import fr.epf.footvlg.utils.retrofit
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminFragment : Fragment() {
    private val apiService = retrofit().create(APIService::class.java)
    private lateinit var user: Member
    private var ListUsers:List<Member> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
         super.onOptionsItemSelected(item)
        when(item.itemId){
            android.R.id.home ->{
                (activity as NavigationHost).navigateTo(AccountFragment(), false) // Navigate to the next Fragment
                return true
            }
            else -> return false
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if(savedInstanceState ==null){
            childFragmentManager
                .beginTransaction()
                .replace(R.id.admin_container,loaderFragment())
                .commit()
        }
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setHomeButtonEnabled(true)

        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_admin, container, false)

        runBlocking {
            user = UserDAO().getUser()!!
        }

        val requestCall = apiService.getAllUsers(user)

        requestCall.enqueue(object: Callback<List<Member>> {
            override fun onFailure(call: Call<List<Member>>, t: Throwable) {
                Toast.makeText(context,"Erreur", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<List<Member>>, response: Response<List<Member>>) {
                val bool = response.isSuccessful
                if(bool){
                    if(response.body()?.size!! >0){
                        ListUsers = response.body()!!

                        //on charge le fragment en lui passant
                        // la liste des Members en paramètres
                        val frag = ListUsersAdminFragment()
                        val bundle = Bundle()
                        bundle.putParcelableArrayList("users",ArrayList(ListUsers))
                        frag.arguments = bundle

                        childFragmentManager
                            .beginTransaction()
                            .replace(R.id.admin_container,frag)
                            .commit()
                    }else{
                        childFragmentManager
                            .beginTransaction()
                            .replace(R.id.admin_container,NoUserFragment())
                            .commit()
                    }
                }else{
                    Toast.makeText(context,"Erreur, base de données incomplète", Toast.LENGTH_SHORT).show()
                }
            }
        })
        return view
    }
}

package fr.epf.footvlg.fragment

import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import fr.epf.footvlg.API.APIService

import fr.epf.footvlg.R
import fr.epf.footvlg.interfaces.NavigationHost
import fr.epf.footvlg.models.GroupInit
import fr.epf.footvlg.models.Member
import fr.epf.footvlg.utils.UserDAO
import fr.epf.footvlg.utils.retrofit
import kotlinx.android.synthetic.main.fragment_create_group.*
import kotlinx.android.synthetic.main.fragment_create_group.view.*
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CreateGroupFragment : Fragment() {
    val apiService = retrofit().create(APIService::class.java)
    lateinit var user: Member
    lateinit var group:GroupInit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    //inflate the menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
    //handle item clicks of menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //get item id to handle item clicks
        val id = item.itemId
        //handle item clicks
        if (id == R.id.back_button){
            (activity as NavigationHost).navigateTo(GroupsFragment(), false) // Navigate to the next Fragment

        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_create_group, container, false)

        view.create_group_button.setOnClickListener{
            ValidateDATA()

        }
        return view
    }
    private fun ValidateDATA(){
        val groupNameTEXT = GroupName.text.toString().trim()
        if(TextUtils.isEmpty(groupNameTEXT)){
            GroupName.error = getString(R.string.emptyField)
        }else{
            val UserDAO = UserDAO()
            runBlocking {
                user = UserDAO.getUser()!!
            }
            group = GroupInit(user,groupNameTEXT)

            val requestCall = apiService.createGroup(group)

            requestCall.enqueue(object: Callback<String> {
                override fun onFailure(call: Call<String>, t: Throwable) {
                    Toast.makeText(context,"Erreur", Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<String>, response: Response<String>) {
                    val bool = response.isSuccessful
                    if(bool){
                        Toast.makeText(context,"${response.body()}", Toast.LENGTH_SHORT).show()
                        (activity as NavigationHost).navigateTo(GroupsFragment(), false) // Navigate to the next Fragment
                    }else{
                        Toast.makeText(context,"Impossible de créer le groupe, le nom est déjà pris et/ou la base de données locale est incorrecte", Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }
    }

}

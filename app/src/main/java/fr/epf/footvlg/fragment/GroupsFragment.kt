package fr.epf.footvlg.fragment

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import fr.epf.footvlg.API.APIService

import fr.epf.footvlg.R
import fr.epf.footvlg.adapters.GroupAdapter
import fr.epf.footvlg.interfaces.NavigationHost
import fr.epf.footvlg.models.Group
import fr.epf.footvlg.models.Member
import fr.epf.footvlg.utils.UserDAO
import fr.epf.footvlg.utils.retrofit
import kotlinx.android.synthetic.main.fragment_groups.*
import kotlinx.android.synthetic.main.fragment_groups.view.*
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class GroupsFragment : Fragment() {
    private val apiService = retrofit().create(APIService::class.java)
    private lateinit var user: Member
    private var ListGroups:List<Group> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_groups, container, false)

        view.list_groups.layoutManager = LinearLayoutManager(activity,
            LinearLayoutManager.VERTICAL,false)


        view.createGroup.setOnClickListener {
            (activity as NavigationHost).navigateTo(CreateGroupFragment(), false)

        }

        view.validate_join_button.setOnClickListener {

        }
        runBlocking {
            user = UserDAO().getUser()!!
        }

        val requestCall = apiService.getUserGroups(user)

        requestCall.enqueue(object: Callback<List<Group>> {
            override fun onFailure(call: Call<List<Group>>, t: Throwable) {
                Toast.makeText(context,"Erreur ${t}", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<List<Group>>, response: Response<List<Group>>) {
                val bool = response.isSuccessful
                if(bool){
                    if(response.body()?.size!! >0){
                        ListGroups = response.body()!!
                        view.list_groups.adapter = GroupAdapter(ListGroups)

                    }
                }else{
                    Toast.makeText(context,"Impossible de récupérer vos groupes, votre base de données n'est pas à jour", Toast.LENGTH_SHORT).show()
                }
            }
        })
        return view
    }
    private fun DisplayLoader(){

    }
}

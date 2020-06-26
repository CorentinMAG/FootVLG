package fr.epf.footvlg.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import fr.epf.footvlg.API.APIService

import fr.epf.footvlg.R
import fr.epf.footvlg.models.Group
import fr.epf.footvlg.models.Member
import fr.epf.footvlg.utils.UserDAO
import fr.epf.footvlg.utils.retrofit
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

        if(savedInstanceState ==null){
            childFragmentManager
                .beginTransaction()
                .replace(R.id.mycontainer,CreateAndJoinGroupFragment())
                .replace(R.id.group_container,loaderFragment())
                .commit()
        }

        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        (activity as AppCompatActivity).supportActionBar?.setHomeButtonEnabled(false)

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_groups, container, false)


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

                        //on charge le fragment en lui passant
                        // la liste des groupes en paramètres
                        val frag = ListGroupFragment()
                        val bundle = Bundle()
                        bundle.putParcelableArrayList("groups",ArrayList(ListGroups))
                        frag.arguments = bundle

                        childFragmentManager
                            .beginTransaction()
                            .replace(R.id.group_container,frag)
                            .commit()
                    }else{
                        childFragmentManager
                            .beginTransaction()
                            .replace(R.id.group_container,NoGroupFragment())
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

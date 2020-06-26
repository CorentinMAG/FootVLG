package fr.epf.footvlg.fragment

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import fr.epf.footvlg.API.APIService

import fr.epf.footvlg.R
import fr.epf.footvlg.interfaces.NavigationHost
import fr.epf.footvlg.models.JoinGroup
import fr.epf.footvlg.models.Member
import fr.epf.footvlg.utils.UserDAO
import fr.epf.footvlg.utils.retrofit
import kotlinx.android.synthetic.main.fragment_create_and_join_group.*
import kotlinx.android.synthetic.main.fragment_create_and_join_group.view.*
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CreateAndJoinGroupFragment : Fragment() {

    lateinit var user: Member
    val apiService = retrofit().create(APIService::class.java)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_create_and_join_group, container, false)

        runBlocking {
            user = UserDAO().getUser()!!
        }
        if(!user.is_admin){
            view.createGroup.visibility=View.GONE
        }

        view.createGroup.setOnClickListener {
        (activity as NavigationHost).navigateTo(CreateGroupFragment(), false)

        }

        view.validate_join_button.setOnClickListener {
            ValidateDATA()

        }
        return view
    }
    private fun ValidateDATA(){
        val joincodeTEXT = joinCode.text.toString().trim()
        if(TextUtils.isEmpty(joincodeTEXT)){
            joinCode.error = getString(R.string.emptyField)
        }else{
            val credentials = JoinGroup(user,joincodeTEXT)
            val requestCall = apiService.userJoinGroup(credentials)
            requestCall.enqueue(object: Callback<String> {
                override fun onFailure(call: Call<String>, t: Throwable) {
                    Toast.makeText(context,"Vous faites déjà parti de ce groupe", Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<String>, response: Response<String>) {
                    val bool = response.isSuccessful
                    if(bool){
                        Toast.makeText(context,"${response.body()}", Toast.LENGTH_SHORT).show()
                        (activity as NavigationHost).navigateTo(GroupsFragment(), false) // Navigate to the next Fragment
                    }else{
                        Toast.makeText(context,"Impossible de rejoindre le groupe", Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }
    }
}

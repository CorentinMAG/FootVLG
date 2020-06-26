package fr.epf.footvlg.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import fr.epf.footvlg.API.APIService
import fr.epf.footvlg.R
import fr.epf.footvlg.models.Member
import fr.epf.footvlg.utils.retrofit
import kotlinx.android.synthetic.main.user_view.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UserAdminAdapter (val users:List<Member>): RecyclerView.Adapter<UserAdminAdapter.UserViewHolder>() {
    private val apiService = retrofit().create(APIService::class.java)
    class UserViewHolder(val userView: View) : RecyclerView.ViewHolder(userView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.user_view, parent, false)
        return UserViewHolder(view)
    }

    override fun getItemCount(): Int = users.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]

        holder.userView.last_name.text = user.last_name
        holder.userView.first_name.text = user.first_name
        if(user.is_admin){
            holder.userView.toggle_admin_state.isChecked = true
        }
        holder.userView.toggle_admin_state.setOnCheckedChangeListener { buttonView, isChecked ->
            user.is_admin = isChecked
            val requestCall = apiService.toggleAdminState(user)
            requestCall.enqueue(object: Callback<String> {
                override fun onFailure(call: Call<String>, t: Throwable) {
                    Toast.makeText(holder.userView.context,"Erreur", Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<String>, response: Response<String>) {
                    val bool = response.isSuccessful
                    if(bool){
                        Toast.makeText(holder.userView.context,"${response.body()}", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(holder.userView.context,"Erreur, base de données incomplète", Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }
    }
}
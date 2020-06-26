package fr.epf.footvlg.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import fr.epf.footvlg.R
import fr.epf.footvlg.models.Member
import kotlinx.android.synthetic.main.user_view.view.*


class UserAdminAdapter (val users:List<Member>): RecyclerView.Adapter<UserAdminAdapter.UserViewHolder>() {
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

        holder.userView.setOnClickListener {
            Toast.makeText(holder.userView.context, "${user.last_name}", Toast.LENGTH_SHORT).show()
        }
    }
}
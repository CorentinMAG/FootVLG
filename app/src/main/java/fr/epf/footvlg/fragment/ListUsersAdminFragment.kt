package fr.epf.footvlg.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager

import fr.epf.footvlg.R
import fr.epf.footvlg.adapters.UserAdminAdapter
import fr.epf.footvlg.models.Member
import kotlinx.android.synthetic.main.fragment_list_users_admin.view.*

class ListUsersAdminFragment : Fragment() {
    private var ListUsers:ArrayList<Member>?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        ListUsers = arguments?.getParcelableArrayList<Member>("users")

        val view = inflater.inflate(R.layout.fragment_list_users_admin, container, false)

        view.list_users_admin.layoutManager = LinearLayoutManager(activity,
            LinearLayoutManager.VERTICAL,false)

        view.list_users_admin.adapter = ListUsers?.let { UserAdminAdapter(it) }

        return view
    }
}

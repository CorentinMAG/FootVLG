package fr.epf.footvlg.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager

import fr.epf.footvlg.R
import fr.epf.footvlg.adapters.GroupAdapter
import fr.epf.footvlg.interfaces.NavigationHost
import fr.epf.footvlg.models.Group
import kotlinx.android.synthetic.main.fragment_list_group.view.*


class ListGroupFragment : Fragment() {
    private var ListGroups:ArrayList<Group>?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //on récupère la liste des groupes de l'utilisateur
        ListGroups = arguments?.getParcelableArrayList<Group>("groups")

        val view = inflater.inflate(R.layout.fragment_list_group, container, false)

        view.list_groups.layoutManager = LinearLayoutManager(activity,
           LinearLayoutManager.VERTICAL,false)

        view.list_groups.adapter = ListGroups?.let {
            GroupAdapter(it){
                val frag = ManageGroupFragment()
                val bundle = Bundle()
                bundle.putParcelable("group",it)
                frag.arguments = bundle
                (activity as NavigationHost).navigateTo(frag, false) // Navigate to the next Fragment
            }
        }
        return view
    }
}

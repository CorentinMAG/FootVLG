package fr.epf.footvlg.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import fr.epf.footvlg.R
import fr.epf.footvlg.interfaces.NavigationHost
import fr.epf.footvlg.models.Group
import kotlinx.android.synthetic.main.fragment_manage_group.view.*


class ManageGroupFragment : Fragment() {
    private var group: Group?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //on récupère le groupe passé dans le bundle
        group = arguments?.getParcelable("group")

        // Inflate the layout for this fragment
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setHomeButtonEnabled(true)
        val view = inflater.inflate(R.layout.fragment_manage_group, container, false)

        view.test.text = group?.groupName
        return view
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        when(item.itemId){
            android.R.id.home -> {
                (activity as NavigationHost).navigateTo(GroupsFragment(), false) // Navigate to the next Fragment
                return true
            }
            else -> return false
        }

    }

}

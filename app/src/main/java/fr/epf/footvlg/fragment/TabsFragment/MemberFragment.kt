package fr.epf.footvlg.fragment.TabsFragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import fr.epf.footvlg.R
import fr.epf.footvlg.models.Member

/**
 * A simple [Fragment] subclass.
 */
class MemberFragment : Fragment() {
    private var ListMembers:ArrayList<Member>?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        ListMembers = arguments?.getParcelableArrayList<Member>("GroupMembers")

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_member, container, false)
        return view
    }

}

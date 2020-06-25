package fr.epf.footvlg.fragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment

import fr.epf.footvlg.R
import fr.epf.footvlg.interfaces.NavigationHost
import kotlinx.android.synthetic.main.activity_login.*

class mainFragment : Fragment(),NavigationHost {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //on charge le fragment Home des qu'on arrive sur le main fragment
        (this as NavigationHost).navigateTo(HomeFragment(), false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        activity?.bottom_navigation?.visibility=View.VISIBLE
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        return view
    }

    override fun navigateTo(fragment: Fragment, addToBackstack: Boolean) {
        val transaction = activity!!.supportFragmentManager
            .beginTransaction()
            .replace(R.id.Maincontainer, fragment)

        if (addToBackstack) {
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }
}

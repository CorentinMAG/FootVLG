package fr.epf.footvlg.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.contentValuesOf
import androidx.recyclerview.widget.RecyclerView
import fr.epf.footvlg.R
import fr.epf.footvlg.models.Group
import kotlinx.android.synthetic.main.group_view.view.*

class GroupAdapter (val groups:List<Group>):RecyclerView.Adapter<GroupAdapter.GroupViewHolder>(){
    class GroupViewHolder(val groupView: View):RecyclerView.ViewHolder(groupView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view:View = layoutInflater.inflate(R.layout.group_view,parent,false)
        return GroupViewHolder(view)
    }

    override fun getItemCount(): Int = groups.size

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
       val group = groups[position]
        holder.groupView.group_name.text = group.groupName
        holder.groupView.creation_date_group.text = group.creation_date

        holder.groupView.setOnClickListener {
            Toast.makeText(holder.groupView.context,"${group.groupName}",Toast.LENGTH_SHORT).show()
        }
    }
}
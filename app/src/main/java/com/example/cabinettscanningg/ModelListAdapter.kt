import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cabinettscanningg.OuterBoxModel
import com.example.cabinettscanningg.R

class ModelListAdapter(private val mList: List<OuterBoxModel>) : RecyclerView.Adapter<ModelListAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.outermodel_item, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val ItemsViewModel = mList[position]
//.srNo.text= "${position+1}"
        holder.serialNumber.text= mList[position].cabitmodelno
        holder.rackNumber.text= mList[position].cabitscan

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        //val srNo: TextView = itemView.findViewById(R.id.tvSrNoPickItem)
        val serialNumber: TextView = itemView.findViewById(R.id.modelnum)
        val rackNumber: TextView = itemView.findViewById(R.id.cabinetnumber)


    }
}
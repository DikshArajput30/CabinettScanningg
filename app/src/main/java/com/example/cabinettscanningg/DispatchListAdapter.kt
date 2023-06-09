package com.example.cabinettscanningg

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.AudioManager
import android.media.ToneGenerator
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.jindal22.model.DispatchListModel

import java.util.ArrayList


class DispatchListAdapter(val list: ArrayList<DispatchListModel>):RecyclerView.Adapter<DispatchListAdapter.MyViewHolder>() {

        inner class MyViewHolder(view: View):RecyclerView.ViewHolder(view){
            var tvDesignNo = itemView.findViewById<TextView>(R.id.tvDesignNo)
            var tvBarcode = itemView.findViewById<TextView>(R.id.tvBarcode)
            var tvGrade = itemView.findViewById<TextView>(R.id.tvGrade)
            var tvSQTY = itemView.findViewById<TextView>(R.id.tvSQTY)
            //var tvWeight = itemView.findViewById<TextView>(R.id.tvWeight)
            var ivDelete = itemView.findViewById<ImageView>(R.id.ivDelete)

        }

    /**
     * Called when RecyclerView needs a new [ViewHolder] of the given type to represent
     * an item.
     *
     *
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     *
     *
     * The new ViewHolder will be used to display items of the adapter using
     * [.onBindViewHolder]. Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary [View.findViewById] calls.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     * an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see .getItemViewType
     * @see .onBindViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_layout,parent,false))
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
       // holder.srNo.text= "${position+1}"
//        holder.tvDesignNo.text=list[position].date
//        holder.tvBarcode.text=list[position].cabinetname
//        holder.tvSQTY.text="${list[position].cabinetitem}"

        holder.tvDesignNo.text=list[position].cabinetitem
        holder.tvBarcode.text=list[position].date
        holder.tvSQTY.text="${list[position].cabinetname}"
        holder.ivDelete.visibility=View.GONE
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    override fun getItemCount(): Int {
      return  list.size
    }

}
//
//
//
//{
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
//        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.outermodel_item,parent,false))
//
//    }
//
//    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//
//        holder.srNo.text= "${position+1}"
//        holder.serialNumber.text= list[position].cabitmodelno
//        holder.rackNumber.text= list[position].cabitscan
//
//
//        Log.d("listsizeeee", "onBindViewHolder: "+list.size)
//    }
//
//
//
//
//    override fun getItemCount(): Int {
//        return list.size
//    }
//
//
//    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
//        val srNo: TextView = itemView.findViewById(R.id.tvSrNoPickItem)
//        val serialNumber: TextView = itemView.findViewById(R.id.tvSerialPickItem)
//        val rackNumber: TextView = itemView.findViewById(R.id.tvRackPickItem)
//
//    }
//
//}

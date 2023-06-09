package com.jindal22.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.jindal22.adapter.AdapterData.DataView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.cabinettscanningg.R
import com.jindal22.model.DispatchModel

class AdapterData(private val context: Context, private val list: List<DispatchModel>,private val listner:OnRemove) :
    RecyclerView.Adapter<DataView>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataView {

        return DataView(LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false))
    }

    override fun onBindViewHolder(holder: DataView, position: Int) {
        holder.tvDesignNo.text=list[position].cabunetitemname
        holder.tvBarcode.text=list[position].cabinetname
       holder.tvSQTY.text="${list[position].cabinetmodelno}"
       // holder.tvWeight.text="${list[position].GrossWeight}"
//        if (list[position].GRADE!=""){
//            holder.tvGrade.visibility=View.GONE
//            holder.tvGrade.text=list[position].GRADE
//        }else{
//            holder.tvGrade.visibility=View.GONE
//        }

        holder.ivDelete.setOnClickListener {
            listner.onRemoveItem(position,list[position].cabinetname)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class DataView(itemView: View) : RecyclerView.ViewHolder(itemView) {

           var tvDesignNo = itemView.findViewById<TextView>(R.id.tvDesignNo)
           var tvBarcode = itemView.findViewById<TextView>(R.id.tvBarcode)
           var tvGrade = itemView.findViewById<TextView>(R.id.tvGrade)
           var tvSQTY = itemView.findViewById<TextView>(R.id.tvSQTY)
           //var tvWeight = itemView.findViewById<TextView>(R.id.tvWeight)
           var ivDelete = itemView.findViewById<ImageView>(R.id.ivDelete)

    }

    interface OnRemove{
        public fun onRemoveItem(position:Int,barcode:String)
    }
}
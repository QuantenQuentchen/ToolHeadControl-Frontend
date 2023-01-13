package com.example.toolheadadjust.ui.gallery

import android.media.Image
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.toolheadadjust.R
import com.example.toolheadadjust.SQLiteManager
import com.example.toolheadadjust.ToolCfg
import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList

class ToolCfgAdapter(
    var toolCfgList: ArrayList<ToolCfg>,
    val DBHelper: SQLiteManager
): RecyclerView.Adapter<ToolCfgAdapter.ToolCfgHolder>() {

    init {
        toolCfgList = DBHelper.getAllToolCfg()
    }

    var onItemClick: ((ToolCfg) -> Unit)? = null

    inner class ToolCfgHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        //val nameTextView = itemView.findViewById<TextView>(R.id.RowText) as TextView
        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(toolCfgList[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToolCfgHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.tool_cfg_item, parent, false)
        return ToolCfgHolder(view)
    }

    override fun onBindViewHolder(holder: ToolCfgHolder, position: Int) {
        val currentToolCfg = toolCfgList[position]
        holder.itemView.apply {
            findViewById<TextView>(R.id.textViewTitle).text = position.plus(1).toString() + ". " + currentToolCfg.name
        }
        }

    fun removeItem(position: Int) {
        Log.d("ToolCfgAdapter", "removeItem: $position")
        DBHelper.removeToolCfgByID(toolCfgList[position].id)
        toolCfgList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun addItem(toolCfg: ToolCfg) {
        DBHelper.addToolCfg(toolCfg)
        toolCfgList.add(toolCfg)
        notifyItemInserted(toolCfgList.size)
    }

    override fun getItemCount(): Int {
        return toolCfgList.size
    }
}


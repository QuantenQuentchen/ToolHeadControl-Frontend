package com.example.toolheadadjust.ui.gallery

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.toolheadadjust.R

class ToolConfigAdapter: ArrayAdapter<ToolConfig> {

    constructor(context: Context, resource: Int, objects: List<ToolConfig>) : super(context, resource, objects) {
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var ToolConfig: ToolConfig? = getItem(position)
        var view: View? = convertView
        if (view == null) {
            view = View.inflate(context, R.layout.recyclingview_tool_item, null)
            //val title: TextView = view.findViewById(R.id.cell_title)
            //val description: TextView = view.findViewById(R.id.cell_description)
            //title.text = ToolConfig?.name
            //description.text = ToolConfig?.description
        }
        return convertView!!//super.getView(position, convertView, parent)
    }


}
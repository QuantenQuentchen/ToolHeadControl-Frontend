package com.example.toolheadadjust.ui.gallery

import java.util.*

class ToolConfig(var name: String, var description: String, var x: Int, var y: Int, val id: Int, date: Date) {

    companion object{
        val toolConfigList = mutableListOf<ToolConfig>()
        fun addToolConfig(toolConfig: ToolConfig) {
            toolConfigList.add(toolConfig)
        }
    }
}
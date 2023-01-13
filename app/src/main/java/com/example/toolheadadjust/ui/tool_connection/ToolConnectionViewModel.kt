package com.example.toolheadadjust.ui.tool_connection

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class ToolConnectionViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is the ToolConnector Fragment"
    }
    val text: LiveData<String> = _text
}
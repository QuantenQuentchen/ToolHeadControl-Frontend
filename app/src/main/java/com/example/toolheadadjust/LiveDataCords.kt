package com.example.toolheadadjust

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class LiveDataCords : ViewModel(){

    val _xCord: MutableLiveData<Int> = MutableLiveData<Int>(0)
    val _yCord: MutableLiveData<Int> = MutableLiveData<Int>(0)
    val _lock: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)

    val _isConnected: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    val _isConnecting: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    val _isAvailable: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)

    val _DeviceAddress: MutableLiveData<String> = MutableLiveData<String>("98:D3:31:FD:0C:0C")


    fun setCords(x: Int, y: Int, lock: Boolean){
        _xCord.postValue(x)
        _yCord.postValue(y)
        _lock.postValue(lock)
    }
}
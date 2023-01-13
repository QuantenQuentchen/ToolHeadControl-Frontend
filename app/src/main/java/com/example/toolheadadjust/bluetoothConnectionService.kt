package com.example.toolheadadjust

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.util.Log
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.*

class BluetoothService(BTAdapter: BluetoothAdapter, context: Context, mainActivity: MainActivity) {
    private val bluetoothAdapter: BluetoothAdapter
    private var mConnectedThread: ConnectedThread? = null
    val mUUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    private val opening: String = "s"
    private val closing: String = "q"
    private val fSetPos: String = "1"
    private val fUpdatePos: String = "2"
    private val fLock: String = "3"
    private val fUnlock: String = "4"
    private val fSetZero: String = "5"
    private  var liveCords: LiveDataCords = mainActivity.mLiveDataCords
    private var mBluetoothDevice: BluetoothDevice? = null
    private var mBluetoothDeviceName: String? = "HC-05"
    private var mBluetoothDeviceAddress: String? = "98:D3:31:FD:0C:0C"
    private var mBluetoothSocket: BluetoothSocket? = null
    var isConnected: Boolean = false
    var isAvailable: Boolean = false
    var isRunning: Boolean = false

    init {
        this.bluetoothAdapter = BTAdapter
    }

    fun loadDevice() {
        val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter.bondedDevices
        pairedDevices?.forEach { device ->
            if (device.address == mBluetoothDeviceAddress) {
                mBluetoothDevice = device
                Log.d("HomeFragment", "Device found: " + device)
                try {
                    connect(mBluetoothDevice!!)
                } catch (e: Exception) {
                    Log.d("HomeFragment", "Device not found: " + e)
                }
            }
        }
    }

    fun connect(device: BluetoothDevice) {
        val connectThread = ConnectThread(device)
        connectThread.start()
    }

    fun write(bytes: ByteArray) {
        Log.d("HomeFragment", "Writing: " + bytes)
        //mConnectedThread!!.write(bytes)
        if (mConnectedThread != null) {
            mConnectedThread!!.write(bytes)
        }
        else{
            Log.d("HomeFragment", "mConnectedThread is null")
        }
    }

    fun setPos(xNumber: Number, yNumber: Number){
        var sendVal = (opening+ fSetPos + String.format("%05d", xNumber) + String.format("%05d", yNumber)+ 0 + closing).toByteArray()
        write(sendVal)
    }
    fun updatePos(xNumber: Number, yNumber: Number){
        var sendVal = (opening+ fUpdatePos + String.format("%05d", xNumber) + String.format("%05d", yNumber)+ 0 + closing).toByteArray()
        write(sendVal)
    }
    fun setZero(){
        var sendVal = (opening+ fSetZero + "00000" + "00000" + 0 + closing).toByteArray()
        write(sendVal)
    }
    fun lock(){
        var sendVal = (opening+ fLock + "00000" + "00000" + 0 + closing).toByteArray()
        write(sendVal)
    }
    fun unlock(){
        var sendVal = (opening+ fUnlock + "00000" + "00000" + 0 + closing).toByteArray()
        write(sendVal)
    }


    private inner class ConnectThread(device: BluetoothDevice) : Thread() {
        private val mmSocket: BluetoothSocket? by lazy(LazyThreadSafetyMode.NONE) {
            Log.d("HomeFragment", "ConnectThread started $device")
            device.createInsecureRfcommSocketToServiceRecord(mUUID)
        }

        override fun run() {
            // Cancel discovery because it otherwise slows down the connection.
            bluetoothAdapter?.cancelDiscovery()
                mmSocket?.let { socket ->
                    Log.d("HomeFragment", "ConnectThread run $socket")
                    // Connect to the remote device through the socket. This call blocks
                    // until it succeeds or throws an exception.
                    socket.connect()
                    //Log.d("BluetoothService", "Connected to ${mBluetoothSocket!!.remoteDevice.name}")
                    // The connection attempt succeeded. Perform work associated with
                    // the connection in a separate thread.
                    //Log.d("BluetoothService", "Starting ConnectedThread")
                    mConnectedThread = ConnectedThread(socket)
                    mConnectedThread!!.start()
                }
        }

        // Closes the client socket and causes the thread to finish.
        fun cancel() {
            try {
                mmSocket?.close()
                Log.d("BluetoothService", "Socket was successfully closed")
            } catch (e: IOException) {
                Log.e("ConnectThread", "Could not close the client socket", e)
            }
        }
    }

    private inner class ConnectedThread(socket: BluetoothSocket): Thread() {
        //private val mmSocket: BluetoothSocket = socket
        private val mmInStream: InputStream = socket.inputStream
        private val mmOutStream: OutputStream = socket.outputStream
        private var mmBuffer: ByteArray = ByteArray(1024) // mmBuffer store for the stream
        private var littleBuffer: UByte = 0u
        var posX: Int = 0
        var strX: String = ""
        var posY: Int = 0
        var strY: String = ""
        var posZ: Int = 0
        var lock: Boolean = false
        var storeArray: Array<String> = arrayOf("","","")
        val TAG = "MyBluetoothService"
        var reading: Boolean = false
        var readingPos: Int = 0

        override fun run() {
            Log.d("BluetoothService", "ConnectedThread started")
            var numBytes: Int // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs.
            while (true) {
                // Read from the InputStream.
                try {
                    mmInStream.read(mmBuffer)
                    //Log.d("BluetoothService", "Read from InputStream ${mmBuffer.toString(Charsets.UTF_8)}")
                    var i = 0
                    String(mmBuffer, Charsets.UTF_8).forEach {
                        var Es =it.toString()
                        //Log.d("BluetoothService", "Read from InputStream ${it.toInt()}")
                        if(it.toInt() != 0){
                            Log.d("BluetoothService", "Read from InputStream ${Es.toString()}")
                        }
                        if (reading) {
                            if(it == 'q'){}
                            else if(it != ' '){
                                try {
                                    //Log.d("BluetoothService", "Read from InputStream ${Es.toString()}")
                                    storeArray[i] += it.toString()
                                }
                                catch (e: Exception){
                                    Log.d("BluetoothService", "Read from InputStream ${e.toString()}")
                                    reading = false
                                }
                            }
                            else{
                                i++
                            }
                        }
                        if (it == 's') {
                            reading = true
                            readingPos = 0
                            Log.d("BluetoothService", "Reading started")
                        }
                        if (it == 'q') {
                            Log.d("BluetoothService", "Reading ended${storeArray[0]} ${storeArray[1]} ${storeArray[2]}")
                            reading = false
                            readingPos = 0
                            i = 0;
                            try {
                                posX = storeArray[0].toInt()
                                posY = storeArray[1].toInt()
                                lock = storeArray[2].toBoolean()
                                Log.d("BluetoothService", "Reading ended${posX} $posY $lock")
                                if (liveCords._xCord.value != posX || liveCords._yCord.value != posY || liveCords._lock.value != lock) {
                                    liveCords._xCord.postValue(posX)
                                    liveCords._yCord.postValue(posY)
                                    liveCords._lock.postValue(lock)
                                }
                            //liveCords.setCords(posX, posY, lock)
                            //liveCords._xCord.postValue(posX)
                            //liveCords._yCord.postValue(posY)
                            //liveCords._lock.postValue(lock)
                            } catch (e: Exception) {
                                Log.d("BluetoothService", "Reading ended${e}")
                            }
                            storeArray[0] = ""
                            storeArray[1] = ""
                            storeArray[2] = ""
                        }
                    }
                    mmBuffer = ByteArray(1024)
                } catch (e: IOException) {
                    Log.d(TAG, "Input stream was disconnected", e)
                    break
                }
                Log.d("BluetoothService", "Received ${mmBuffer[0]}")
            }
        }
        fun write(bytes: ByteArray) {
            Log.d("BluetoothService", "Writing to OutputStream")
            Log.d("BluetoothService", "Writing to OutputStream ${bytes.toString(Charsets.UTF_8)}")
            Log.d("BluetoothService", "Writing to OutputStream ${mBluetoothSocket}")
                try {
                    mmOutStream.write(bytes)
                    Log.d("BluetoothService", "Wrote to OutputStream ${bytes.toString(Charsets.UTF_8)}")
                } catch (e: IOException) {
                    Log.e("BluetoothService", "Error occurred when sending data", e)
                    // Send a failure message back to the activity.
                    //val writeErrorMsg = mHandler.obtainMessage(
                    //    MESSAGE_TOAST)
                    //val bundle = Bundle().apply {
                    //    putString("toast",
                    //        "Couldn't send data to the other device")
                    //}
                    //writeErrorMsg.data = bundle
                    //mHandler.sendMessage(writeErrorMsg)
                    return
                }
            }
        }
    }

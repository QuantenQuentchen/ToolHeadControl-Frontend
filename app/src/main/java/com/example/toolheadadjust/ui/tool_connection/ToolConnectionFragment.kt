package com.example.toolheadadjust.ui.tool_connection

import android.Manifest
import android.animation.ValueAnimator
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context.BLUETOOTH_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.squircleview.SquircleView
import com.example.toolheadadjust.MainActivity
import com.example.toolheadadjust.R
import com.example.toolheadadjust.databinding.FragmentToolConnectionBinding
import com.google.android.material.shape.CornerTreatment
import com.google.android.material.shape.ShapeAppearanceModel
import kotlinx.coroutines.NonCancellable.start
import kotlin.concurrent.thread

class ToolConnectionFragment : Fragment() {
    private var _binding: FragmentToolConnectionBinding? = null
    private val binding get() = _binding!!
    private var toolHandler: Handler = Handler()
    private val requestPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        if (it) {
            // Permission granted
            val bluetoothManager = activity?.getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
            val bluetoothAdapter = bluetoothManager.adapter
            if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled) {
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(enableBtIntent, 1)
            }
        } else {
            // Permission denied
            // Do nothing here because the app can't function without the permission
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        println("onCreateView")
        val toolConnectionViewModel =
            ViewModelProvider(this).get(ToolConnectionViewModel::class.java)

        _binding = FragmentToolConnectionBinding.inflate(inflater, container, false)
        //Boilerplate Shit
        val root : View = binding.root
        val mainActivity = activity as MainActivity
        val blAdapter = mainActivity.mBluetoothAdapter
        val btService = mainActivity.mBluetoothConnectionService
        requestPermission.launch(
            Manifest.permission.BLUETOOTH_ADMIN
        )
        //UI Elements
        val toolConnectorBtn: ImageButton = binding.toolConnectorBtn
        val toolConnectorTV: TextView = binding.toolConnectorTV
        //Graph Vars
        val noBL = R.drawable.ic_connection_indicator_no_bluetooth_foreground
        val noBLCl = R.color.ic_connection_indicator_no_bluetooth_background

        val noConBL = R.drawable.ic_connection_indicator_not_connected_foreground
        val noConBLCl = R.color.ic_connection_indicator_not_connected_background

        val conBL = R.drawable.ic_connection_indicator_connected_foreground
        val conBLCl = R.color.ic_connection_indicator_connected_background

        toolConnectorBtn.setOnClickListener(){
            if (blAdapter == null || !blAdapter.isEnabled) {
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(enableBtIntent, 1)
            } else if(!btService?.isConnected!!){
                btService.loadDevice()
                toolConnectorTV.text = "Connected"
                toolConnectorBtn.setImageResource(conBL)
            } else {
                toolConnectorTV.text = "No Device"
                toolConnectorBtn.setImageResource(noConBL)
            }
            }

        val connectionObserver = Observer<Boolean> { isConnected ->
            if (isConnected) {
                toolConnectorTV.text = "Connected"
                toolConnectorBtn.setImageResource(conBL)
                toolConnectorBtn.setBackgroundColor(resources.getColor(conBLCl))
            } else {
                toolConnectorTV.text = "Not Connected"
                toolConnectorBtn.setImageResource(noConBL)
                toolConnectorBtn.setBackgroundColor(resources.getColor(noConBLCl))
            }
        }
        mainActivity.mLiveDataCords._isConnected.observe(viewLifecycleOwner, connectionObserver)

        return root
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

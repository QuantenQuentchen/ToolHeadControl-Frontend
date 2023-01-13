package com.example.toolheadadjust.ui.home

import android.bluetooth.BluetoothDevice
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.toolheadadjust.MainActivity
import com.example.toolheadadjust.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    public var targetDevice: BluetoothDevice? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        fun byteArrayOfInts(vararg ints: Int) = ByteArray(ints.size) { pos -> ints[pos].toByte() }

        val mainActivity = activity as MainActivity
        val bluetoothAdapter = mainActivity.mBluetoothAdapter
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        val testButton: Button = binding.TestButton
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        testButton.setOnClickListener {
            textView.text = "Button Clicked"
        }
        testButton.setOnLongClickListener {
            var longAssNumber = -5
            var send: ByteArray = byteArrayOf(0x7E, 0x06,0x00,0x16,255.toByte(),245.toByte(),0x01,0x7F)
            send = ("s600005"+String.format("%05d",longAssNumber)+"1q").toByteArray()
            //send[2] = (longAssNumber and 0xFF).toByte()
            //send[3] = ((longAssNumber shr 8) and 0xFF).toByte()
            Log.d("HomeFragment", "send: ${send[2]} ${send[3]}")
            //send = byteArrayOfInts(5000, 500)
            Log.d("HomeFragment", "Sending:"+ send)
            send = "a".toByteArray()
            mainActivity.mBluetoothConnectionService.write(send)
            true
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
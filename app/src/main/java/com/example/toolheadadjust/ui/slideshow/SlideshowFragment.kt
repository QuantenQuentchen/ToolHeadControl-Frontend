package com.example.toolheadadjust.ui.slideshow

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.toolheadadjust.LiveDataCords
import com.example.toolheadadjust.MainActivity
import com.example.toolheadadjust.databinding.FragmentSlideshowBinding

class SlideshowFragment : Fragment() {

    private var _binding: FragmentSlideshowBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val slideshowViewModel =
            ViewModelProvider(this).get(SlideshowViewModel::class.java)
        _binding = FragmentSlideshowBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val mainActivity = activity as MainActivity
        val LDViewModel: LiveDataCords = mainActivity.mLiveDataCords
        //val textView: TextView = binding.textSlideshow
        /*
        slideshowViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        */

        val button_left: ImageButton = binding.buttonLeft
        val button_right: ImageButton = binding.buttonRight
        val button_up: ImageButton = binding.buttonUp
        val button_down: ImageButton = binding.buttonDown
        val XcordsTextView: TextView = binding.textviewCordsX
        val YcordsTextView: TextView = binding.textviewCordsY
        button_up.setOnClickListener{
            mainActivity.mBluetoothConnectionService.updatePos(0, 1)
            //LDViewModel._zCord.value = 50
        }
        button_down.setOnClickListener{
            mainActivity.mBluetoothConnectionService.updatePos(0, -1)
        }
        button_left.setOnClickListener{
            mainActivity.mBluetoothConnectionService.updatePos(-1, 0)
        }
        button_right.setOnClickListener{
            mainActivity.mBluetoothConnectionService.updatePos(1, 0)
        }

        var button_upRunnable = object : Runnable {
            override fun run() {
                mainActivity.mBluetoothConnectionService.updatePos(0, 1)
                button_up.postDelayed(this, 100)
            }
        }

        val XcordsObserver = Observer<Int>({ cords ->
            cords?.let {
                Log.d("SlideshowFragment", "X cords: $cords")
                XcordsTextView.text = cords.toString()
            }
        })
        val YcordsObserver = Observer<Int>({ cords ->
            cords?.let {
                Log.d("SlideshowFragment", "Y cords: $cords")
                YcordsTextView.text = cords.toString()
            }
        })
        LDViewModel._xCord.observe(viewLifecycleOwner, XcordsObserver)
        LDViewModel._yCord.observe(viewLifecycleOwner, YcordsObserver)
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
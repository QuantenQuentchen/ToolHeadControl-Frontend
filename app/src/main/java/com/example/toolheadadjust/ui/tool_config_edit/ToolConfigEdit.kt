package com.example.toolheadadjust.ui.tool_config_edit

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.navigation.findNavController
import com.example.toolheadadjust.MainActivity
import com.example.toolheadadjust.R
import com.example.toolheadadjust.ToolCfg
import com.example.toolheadadjust.databinding.FragmentToolConfigEditBinding
import java.sql.Timestamp
import java.time.LocalDateTime

class ToolConfigEdit : Fragment() {

    companion object {
        fun newInstance() = ToolConfigEdit()
    }

    private lateinit var viewModel: ToolConfigEditViewModel
    private var _binding: FragmentToolConfigEditBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Boilerplate Shit
        val ToolConfigEditViewModel =
            ViewModelProvider(this).get(ToolConfigEditViewModel::class.java)
        _binding = FragmentToolConfigEditBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val mainActivity = activity as MainActivity

        Log.d("ToolConfigEdit", "onCreateView: ${arguments}")
        var Xcord: Int = requireArguments().get("Xcord") as Int
        var Ycord: Int = requireArguments().get("Ycord") as Int
        //UI Elements
        val AcceptBtn = root.findViewById<ImageButton>(R.id.acceptBtn)
        val CancelBtn = root.findViewById<ImageButton>(R.id.cancelBtn)
        val NameEditTxt = root.findViewById<EditText>(R.id.nameEditText)
        val XcordEditTxt = root.findViewById<EditText>(R.id.xcordEditText)
        val YcordEditTxt = root.findViewById<EditText>(R.id.ycordEditText)

        XcordEditTxt.setText(Xcord.toString())
        YcordEditTxt.setText(Ycord.toString())

        CancelBtn.setOnClickListener {
            mainActivity.onBackPressed()
        }
        AcceptBtn.setOnClickListener() {
            mainActivity.mToolCfgAdapter.addItem(
                ToolCfg(
                    NameEditTxt.text.toString(),
                    "X: " + XcordEditTxt.text.toString() + " Y: " + YcordEditTxt.text.toString(),
                    XcordEditTxt.text.toString().toInt(),
                    YcordEditTxt.text.toString().toInt(),
                    Timestamp(System.currentTimeMillis())
                )
            )
            mainActivity.findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.nav_gallery)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
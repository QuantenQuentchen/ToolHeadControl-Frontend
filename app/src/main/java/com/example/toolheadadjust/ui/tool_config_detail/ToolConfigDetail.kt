package com.example.toolheadadjust.ui.tool_config_detail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.example.toolheadadjust.MainActivity
import com.example.toolheadadjust.R
import com.example.toolheadadjust.databinding.FragmentToolConfigDetailBinding

class ToolConfigDetail : Fragment() {

    companion object {
        fun newInstance() = ToolConfigDetail()
    }

    private lateinit var viewModel: ToolConfigDetailViewModel
    private var _binding: FragmentToolConfigDetailBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Boilerplate Shit
        val ToolConfigEditViewModel =
            ViewModelProvider(this).get(ToolConfigDetailViewModel::class.java)
        _binding = FragmentToolConfigDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val mainActivity = activity as MainActivity

        //Var Passing
        val xCord: Int = requireArguments().get("XCord") as Int
        val yXord: Int = requireArguments().get("YCord") as Int
        val name: String = requireArguments().get("Name") as String
        val index: Int = requireArguments().get("index") as Int
        val id: Int = requireArguments().get("id") as Int
        //UI Elements
        val NameTxt = root.findViewById<TextView>(R.id.toolCfgDetailTV)
        val XcordTxt = root.findViewById<TextView>(R.id.toolConfigDetailTVXCords)
        val YcordTxt = root.findViewById<TextView>(R.id.toolConfigDetailTVYCords)
        val imgBtn = root.findViewById<ImageButton>(R.id.imageView3)
        val trashBtn = root.findViewById<ImageButton>(R.id.toolConfigDetailIBTrash)
        NameTxt.text = name
        XcordTxt.text = "X-Cords: " + xCord.toString()
        YcordTxt.text = "Y-Cords: " + yXord.toString()

        trashBtn.setOnClickListener {
            mainActivity.mToolCfgAdapter.removeItem(index)
            mainActivity.onBackPressed()
        }
        imgBtn.setOnClickListener {
            mainActivity.mBluetoothConnectionService.setPos(xCord, yXord)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
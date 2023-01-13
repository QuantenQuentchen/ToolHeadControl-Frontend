package com.example.toolheadadjust.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.toolheadadjust.*
import com.example.toolheadadjust.databinding.FragmentGalleryBinding
import java.util.*

class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val galleryViewModel =
            ViewModelProvider(this).get(GalleryViewModel::class.java)

        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val mainActivity = activity as MainActivity
        val recycler_view = root.findViewById<RecyclerView>(R.id.recycler_view)
        val adapter = mainActivity.mToolCfgAdapter
        /*
        adapter.onItemClick = {
            Toast.makeText(context, "Item $it clicked", Toast.LENGTH_SHORT).show()
        }
         */
        recycler_view.adapter = adapter
        recycler_view.layoutManager = LinearLayoutManager(mainActivity)
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


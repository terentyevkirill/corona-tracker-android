package com.terentiev.coronatracker.ui.world

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.terentiev.coronatracker.R

class WorldFragment : Fragment() {

    private lateinit var worldViewModel: WorldViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        worldViewModel =
                ViewModelProviders.of(this).get(WorldViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_world, container, false)
        val textView: TextView = root.findViewById(R.id.text_world)
        worldViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}

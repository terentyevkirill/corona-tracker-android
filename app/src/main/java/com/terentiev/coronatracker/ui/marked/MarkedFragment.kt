package com.terentiev.coronatracker.ui.marked

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer

import com.terentiev.coronatracker.R

class MarkedFragment : Fragment() {

    private lateinit var markedViewModel: MarkedViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        markedViewModel =
            ViewModelProviders.of(this).get(MarkedViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_marked, container, false)
        val textView: TextView = root.findViewById(R.id.text_marked)
        markedViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}

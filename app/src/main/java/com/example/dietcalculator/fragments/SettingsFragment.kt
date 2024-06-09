package com.example.dietcalculator.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.dietcalculator.dao.IDatabaseConnector
import com.example.dietcalculator.databinding.SettingsFragmentBinding

class SettingsFragment(private val connector: IDatabaseConnector): Fragment() {

    private var _binding: SettingsFragmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SettingsFragmentBinding.inflate(inflater, container, false)
        this.context?.let {
            binding.settingsListView.adapter = SettingsListAdapter(it, connector)
        }
        return binding.root
    }
}
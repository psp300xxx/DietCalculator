package com.example.dietcalculator.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.Toast
import com.example.dietcalculator.R
import com.example.dietcalculator.controller.ISubstitutionCalculator
import com.example.dietcalculator.controller.IsocaloricCalculator
import com.example.dietcalculator.controller.SubstituteCalculatorImpl
import com.example.dietcalculator.databinding.FragmentFirstBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class SubstituteCalculatorFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private var calculator: ISubstitutionCalculator = SubstituteCalculatorImpl

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var switch: Switch

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        switch = binding.modeSwitch
        switch.setOnCheckedChangeListener { compoundButton, value ->
            val stringId = if (value) R.string.mode_switch_isocaloric else R.string.mode_switch_dietist
            val newCalculator = if(value) IsocaloricCalculator else SubstituteCalculatorImpl
            val text = this.context?.getString(stringId)
            switch.text = text
            this.calculator = newCalculator
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.convertButton.setOnClickListener{
            view ->
            Toast.makeText(this.context, "ciao", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
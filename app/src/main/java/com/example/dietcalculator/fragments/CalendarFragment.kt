package com.example.dietcalculator.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.dietcalculator.R
import com.example.dietcalculator.androidviews.WeekCalendarListener
import com.example.dietcalculator.androidviews.WeekCalendarView
import com.example.dietcalculator.androidviews.WeekDays
import com.example.dietcalculator.controller.ISubstitutionCalculator
import com.example.dietcalculator.controller.SubstituteCalculatorImpl
import com.example.dietcalculator.databinding.FragmentCalendarBinding
import com.example.dietcalculator.databinding.FragmentFirstBinding
import com.example.dietcalculator.utility.FragmentVisibleDelegate


class CalendarFragment : Fragment(), FragmentVisibleDelegate, WeekCalendarListener {

    private var _binding: FragmentCalendarBinding? = null
    private var calculator: ISubstitutionCalculator = SubstituteCalculatorImpl

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var weekCalendarView: WeekCalendarView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        val view = inflater.inflate(R.layout.fragment_calendar, container, false)
        weekCalendarView = binding.weekCalendarView
        weekCalendarView.addListener(this)
        return view
    }

    override fun onGoingToNewFragment(fragment: Fragment) {

    }

    override fun onDaySelected(view: WeekCalendarView, day: WeekDays) {

    }

    override fun onDayTouched(view: WeekCalendarView, day: WeekDays) {
        view.setSelectedDay(day)
    }


}
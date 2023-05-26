package com.example.orthoepy

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.MPPointF


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var name : TextView? = null
    private lateinit var pieChart: PieChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        name = view.findViewById(R.id.name)
        val sharedPref : SharedPreferences = requireContext().getSharedPreferences("Test", Context.MODE_PRIVATE)
        name!!.text = sharedPref.getString("USER_NAME", "Jack")
        pieChart = view.findViewById(R.id.pc)
        pieChart.description.isEnabled = false

        val entry : ArrayList<PieEntry> = ArrayList()
        entry.add(PieEntry(sharedPref.getInt("WRONG_ANSWERS", 0).toFloat() , "Неправильные ответы"))
        entry.add(PieEntry(sharedPref.getInt("RIGHT_ANSWERS", 0).toFloat(), "Правильные ответы"))

        val dataSet = PieDataSet(entry, "")
        val data = PieData(dataSet)

        val colors: ArrayList<Int> = ArrayList()
        colors.add(Color.parseColor("#FF6961"))
        colors.add(Color.parseColor("#77DD77"))

        pieChart.setDrawSliceText(false)
        data.setValueTextSize(21f)
        data.setValueTypeface(Typeface.DEFAULT_BOLD)
        dataSet.iconsOffset = MPPointF(0F, 40F)
        dataSet.colors = colors
        dataSet.sliceSpace = 10f

        pieChart.legend.isEnabled = true
        val legend : Legend = pieChart.legend
        legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        legend.orientation = Legend.LegendOrientation.VERTICAL
        legend.setDrawInside(false)
        legend.xEntrySpace = 0f
        legend.yEntrySpace = 0f

        pieChart.legend.textSize = 21f
        pieChart.animateY(950, Easing.EaseInOutQuad)
        pieChart.data = data
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
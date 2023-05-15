package com.example.orthoepy

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Spinner

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class DictionaryFragment : Fragment(), AdapterView.OnItemSelectedListener{
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listView : ListView? = null
    private lateinit var spinner: Spinner
    private val words :Words = Words()

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
        return inflater.inflate(R.layout.fragment_dictionary, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listView = view.findViewById(R.id.list_view)
        spinner = view.findViewById(R.id.spinner_1)
        spinner.onItemSelectedListener = this
        ArrayAdapter.createFromResource(requireContext(), R.array.words_types, android.R.layout.simple_list_item_1)
            .also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
                spinner.adapter = adapter
            }

    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DictionaryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        //верный вариант
        if(position == 0){
            createDictionary()
        }
        else{
            createRules(position, requireContext())
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    private fun createDictionary(){
        val wordsList = words.getWords(requireContext())
        val adapter = CustomListAdapter(requireContext(), wordsList)
        listView!!.adapter = adapter
    }

    private fun createRules(ruleType:Int, context: Context){
        val wordsList = listOf<String>(
            words.getRule(ruleType, context)
        )
        val adapter = CustomRuleAdapter(requireContext(), wordsList)
        listView!!.adapter = adapter
    }
}
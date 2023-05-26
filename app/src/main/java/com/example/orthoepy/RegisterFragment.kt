package com.example.orthoepy

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class RegisterFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var button : Button? = null
    private var nameText : EditText? = null

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
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        button = view.findViewById(R.id.continue_btn)
        nameText = view.findViewById(R.id.editText_name)
        button!!.setOnClickListener {
            userRegistration()
        }
    }

    private fun userRegistration(){
        val name = nameText!!.text
        if(name.isEmpty()){
            nameText!!.setError("Поле не должно быть пустым")
            return
        }
        if(name.length > 12){
            nameText!!.setError("Слишком длинное имя")
            return
        }
        val sharedPref : SharedPreferences = requireContext().getSharedPreferences("Test", Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = sharedPref.edit()
        editor.putBoolean("NEW_USER", true)
        editor.apply()
        editor.putString("USER_NAME", name.toString())
        editor.apply()
        val mainFragment = MainFragment()
        requireFragmentManager().beginTransaction().apply {
            replace(R.id.main_frame, mainFragment)
                .commit()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RegisterFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
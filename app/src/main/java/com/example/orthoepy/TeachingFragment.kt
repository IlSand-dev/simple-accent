package com.example.orthoepy

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class TeachingFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private var listView : ListView? = null
    private var button : Button? = null
    private var winText : TextView? = null
    private val words : Words = Words()
    private var mistakeCounter = 0
    private var taskCount = 0

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
        return inflater.inflate(R.layout.fragment_teaching, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listView = view.findViewById(R.id.answers_list)
        button = view.findViewById(R.id.button_1)
        button!!.setOnClickListener {
            createList()
        }
        winText = view.findViewById(R.id.win_text)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TeachingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun makeTask(word:String, vowels:List<String>):List<String>{
        val testWord = word.lowercase()
        val listOfVariants = mutableListOf<String>()
        var listIsFull = false
        for (i in 0 until testWord.length){
            println(testWord[i])
            if(vowels.contains(testWord[i].toString())){
                val wordToAdd = testWord.slice(0..i-1) + testWord[i].uppercase() + testWord.slice(i+1..testWord.length-1)
                println(wordToAdd)
                if(listOfVariants.size < 4){
                    listOfVariants.add(wordToAdd)
                }
                else{
                    listIsFull = true
                }
            }
            if(listIsFull){
                break
            }
        }
        //если правильный вариант не выпал он всегда будет
        if(!listOfVariants.contains(word)){
            val randomIndex = (0..listOfVariants.size-1).random()
            listOfVariants[randomIndex] = word
        }
        return listOfVariants
    }

    @SuppressLint("SetTextI18n")
    private fun createList() {
        button!!.text = "Далее"
        winText!!.text = ""
        var isPossibleToClick = true
        var isTestOver = false
        val wordsList = words.getWords()
        val vowels = words.getVowels()
        val rightWord = wordsList[(0..wordsList.size-1).random()]
        val answerVarinats = makeTask(rightWord, vowels)
        listView!!.setOnItemClickListener { parent, view, position, id ->
            if(isPossibleToClick && !isTestOver){
                val element:String= parent.getItemAtPosition(position) as String

                if(element.equals(rightWord) && isPossibleToClick){
                    parent.getChildAt(position).setBackgroundColor(Color.GREEN)
                    taskCount++
                    if(taskCount < 5){
                        winText!!.text = "Верно!"
                        isPossibleToClick = false
                    }
                    else{
                        winText!!.text =  "Количество ошибок: " + mistakeCounter.toString()
                        taskCount = 0
                        isTestOver = true
                        mistakeCounter = 0
                        button!!.text = "Новый тест"
                    }
                }
                else{
                    parent.getChildAt(position).setBackgroundColor(Color.RED)
                    mistakeCounter++
                    taskCount++
                    if(taskCount < 5){
                        winText!!.text = "Неверно! Правильный вариант " + rightWord
                        isPossibleToClick = false
                    }
                    else{
                        winText!!.text = "Количество ошибок: " + mistakeCounter.toString()
                        taskCount = 0
                        isTestOver = true
                        mistakeCounter = 0
                        button!!.text = "Новый тест"
                    }
                }
            }

        }
        val arrayAdapter: ArrayAdapter<String> = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1,
            answerVarinats)
        listView!!.adapter = arrayAdapter
    }
}
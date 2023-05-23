package com.example.orthoepy

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import com.github.mikephil.charting.charts.PieChart


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class TeachingFragment : Fragment() {
    private lateinit var randomWords: MutableList<MutableList<String>>
    private lateinit var sharedPref : SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    private var param1: String? = null
    private var param2: String? = null
    private var listView: ListView? = null
    private var button: Button? = null
    private var winText: TextView? = null
    private val words: Words = Words()
    private var mistakeCounter = 0
    private var taskCount = 0
    private var wordCount = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_teaching, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listView = view.findViewById(R.id.answers_list)
        button = view.findViewById(R.id.button_1)
        button!!.setOnClickListener {
            createTask()
        }
        winText = view.findViewById(R.id.win_text)
        randomWords = words.getRandomWords(requireContext())
        sharedPref = requireContext().getSharedPreferences("Test", Context.MODE_PRIVATE)
        editor = sharedPref.edit()

    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) = TeachingFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_PARAM1, param1)
                putString(ARG_PARAM2, param2)
            }
        }
    }

    private fun makeTask(word: String, vowels: List<String>): List<String> {
        val testWord = word.lowercase()
        val listOfVariants = mutableListOf<String>()
        var listIsFull = false
        for (i in testWord.indices) {
            println(testWord[i])
            if (vowels.contains(testWord[i].toString())) {
                val wordToAdd =
                    testWord.slice(0 until i) + testWord[i].uppercase() + testWord.slice(
                        i + 1 until testWord.length
                    )
                println(wordToAdd)
                if (listOfVariants.size < 4) {
                    listOfVariants.add(wordToAdd)
                } else {
                    listIsFull = true
                }
            }
            if (listIsFull) {
                break
            }
        }
        //если правильный вариант не выпал он всегда будет
        if (!listOfVariants.contains(word)) {
            val randomIndex = (0 until listOfVariants.size).random()
            listOfVariants[randomIndex] = word
        }
        return listOfVariants
    }

    private fun checkWordCount(layer: Int): Boolean {
        val levels = intArrayOf(10, 15, 25, 35)
        return wordCount % levels[layer - 1] == 0 && randomWords[layer].size > 0
    }

    @SuppressLint("SetTextI18n")
    private fun createTask() {
        button!!.text = "Далее"
        winText!!.text = ""
        var isPossibleToClickNext = false
        val vowels = words.getVowels()
        wordCount = if (randomWords[0].size == 0) 0 else wordCount
        val level: Int =
            if (checkWordCount(4)) 4
            else (if (checkWordCount(3)) 3
            else (if (checkWordCount(2)) 2
            else (if (checkWordCount(1)) 1 else 0)))
        val currentWord = randomWords[level][0]
        val answerVariants = makeTask(currentWord, vowels)
        listView!!.setOnItemClickListener { parent, view, position, id ->
            if (!isPossibleToClickNext) {
                val element: String = parent.getItemAtPosition(position) as String
                if (level == 0) {
                    if (element == currentWord) {

                        updateStatistics(true)

                        parent.getChildAt(position).setBackgroundColor(Color.GREEN)
                        randomWords[0].add(randomWords[0].removeFirst())
                    } else {

                        updateStatistics(false)

                        parent.getChildAt(position).setBackgroundColor(Color.RED)
                        randomWords[1].add(randomWords[0].removeFirst())
                    }
                } else {
                    if (element == currentWord) {

                        updateStatistics(true)

                        parent.getChildAt(position).setBackgroundColor(Color.GREEN)
                        randomWords[if (level < 4) level + 1 else 0].add(randomWords[level].removeFirst())
                    } else {

                        updateStatistics(false)

                        parent.getChildAt(position).setBackgroundColor(Color.RED)
                        randomWords[if (level > 1) level - 1 else 1].add(randomWords[level].removeFirst())
                    }
                }
                wordCount++
                isPossibleToClickNext = true
            }
        }
        val adapter = CustomListAdapter(requireContext(), answerVariants)
        listView!!.adapter = adapter

        button!!.setOnClickListener {
            if (isPossibleToClickNext) {
                createTask()
            }
        }
    }

    private fun updateStatistics(isAnswerRight:Boolean){
        editor.putInt("ALL_TESTS", sharedPref.getInt("ALL_TESTS", 0)+1)
        if(isAnswerRight){
            editor.putInt("RIGHT_ANSWERS", sharedPref.getInt("RIGHT_ANSWERS", 0)+1)
        }
        else{
            editor.putInt("WRONG_ANSWERS", sharedPref.getInt("WRONG_ANSWERS", 0)+1)
        }
        editor.apply()
    }

    @SuppressLint("SetTextI18n")
    private fun createList() {
        button!!.text = "Далее"
        winText!!.text = ""
        var isPossibleToClick = true
        var isTestOver = false
        val wordsList = words.getWords(requireContext())
        val vowels = words.getVowels()
        val rightWord = wordsList[(0 until wordsList.size).random()]
        val answerVariants = makeTask(rightWord, vowels)
        listView!!.setOnItemClickListener { parent, view, position, id ->
            if (isPossibleToClick && !isTestOver) {
                val element: String = parent.getItemAtPosition(position) as String

                if (element.equals(rightWord) && isPossibleToClick) {
                    parent.getChildAt(position).setBackgroundColor(Color.GREEN)
                    taskCount++
                    if (taskCount < 5) {
                        winText!!.text = "Верно!"
                        isPossibleToClick = false
                    } else {
                        winText!!.text = "Количество ошибок: $mistakeCounter"
                        taskCount = 0
                        isTestOver = true
                        mistakeCounter = 0
                        button!!.text = "Новый тест"
                    }
                } else {
                    parent.getChildAt(position).setBackgroundColor(Color.RED)
                    mistakeCounter++
                    taskCount++
                    if (taskCount < 5) {
                        winText!!.text = "Неверно! Правильный вариант $rightWord"
                        isPossibleToClick = false
                    } else {
                        winText!!.text = "Количество ошибок: $mistakeCounter"
                        taskCount = 0
                        isTestOver = true
                        mistakeCounter = 0
                        button!!.text = "Новый тест"
                    }
                }
            }

        }
        val adapter = CustomListAdapter(requireContext(), answerVariants)
        listView!!.adapter = adapter
    }
}
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


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class TeachingFragment : Fragment() {
    private lateinit var oldRandomWords: MutableList<MutableList<String>>
    private lateinit var randomWords: MutableList<Pair<String, Int>>
    private lateinit var sharedPref: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    private var param1: String? = null
    private var param2: String? = null
    private var listView: ListView? = null
    private var button: Button? = null
    private val words: Words = Words()

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
            newCreateTask()
        }
        oldRandomWords = words.getRandomWords(requireContext())
        randomWords = words.newGetRandomWords(requireContext())
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
            //println(testWord[i])
            if (vowels.contains(testWord[i].toString())) {
                val wordToAdd =
                    testWord.slice(0 until i) + testWord[i].uppercase() + testWord.slice(
                        i + 1 until testWord.length
                    )
                //println(wordToAdd)
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

    @SuppressLint("SetTextI18n")
    private fun newCreateTask() {
        button!!.text = "Далее"
        var isPossibleToClickNext = false
        val vowels = words.getVowels()
        val currentWord = randomWords[0]
        val answerVariants = makeTask(currentWord.first, vowels)
        listView!!.setOnItemClickListener { parent, view, position, id ->
            if (!isPossibleToClickNext) {
                val element: String = parent.getItemAtPosition(position) as String
                if (element == currentWord.first) {
                    updateStatistics(true)
                    parent.getChildAt(position).setBackgroundColor(Color.parseColor("#77DD77"))
                    val index =
                        if (currentWord.second * 2 < randomWords.size) currentWord.second * 2 else 0
                    randomWords.add(
                        if (index == 0) randomWords.size - 1 else index,
                        Pair(currentWord.first, index)
                    )
                    randomWords.removeFirst()
                } else {
                    updateStatistics(false)
                    parent.getChildAt(position).setBackgroundColor(Color.parseColor("#FF6961"))
                    val index = 5
                    randomWords.add(index, Pair(currentWord.first, index))
                    randomWords.removeFirst()
                }
                isPossibleToClickNext = true
            }
        }
        val adapter = CustomListAdapter(requireContext(), answerVariants)
        listView!!.adapter = adapter
        button!!.setOnClickListener {
            if (isPossibleToClickNext) {
                newCreateTask()
            }
        }
    }

    private fun updateStatistics(isAnswerRight: Boolean) {
        editor.putInt("ALL_TESTS", sharedPref.getInt("ALL_TESTS", 0) + 1)
        if (isAnswerRight) {
            editor.putInt("RIGHT_ANSWERS", sharedPref.getInt("RIGHT_ANSWERS", 0) + 1)
        } else {
            editor.putInt("WRONG_ANSWERS", sharedPref.getInt("WRONG_ANSWERS", 0) + 1)
        }
        editor.apply()
    }

    override fun onDestroy() {
        super.onDestroy()
        words.saveState(randomWords, requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        words.saveState(randomWords, requireContext())
    }
}
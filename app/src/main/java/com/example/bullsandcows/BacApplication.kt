package com.example.bullsandcows

import android.app.Application
import android.util.Log
import android.util.Pair
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog

class BacApplication : Application() {
    private val roundLimit = 6
    private var round = 0
    var targetNumber = ""
    var candidateNumber = ""

    var nList = Array(6) { "" }
    var bList = Array(6) { "" }
    var cList = Array(6) { "" }

    fun newGame() {
        round = 0
        targetNumber = generateNewNumber()
        for (i in 0..5) {
            nList[i] = ""
            bList[i] = ""
            cList[i] = ""
        }
    }

    private fun generateNewNumber(): String {
        val numbers = (0..9).toList()
        val result = numbers.shuffled()
        var ans =
            result.subList(0, 4).joinToString(separator = "", transform = { i -> i.toString() })
        Log.d("bac", "new target generated: $ans")
        return ans
    }

    private fun countBullsCows(): Pair<Int, Int> {
        var bulls = 0
        var all = 0
        for (i in 0..3) {
            if (targetNumber[i] == candidateNumber[i]) bulls++
            if (targetNumber.contains(candidateNumber[i])) all++
        }
        return Pair(bulls, all - bulls)
    }

    fun guess(s: String): Pair<Boolean, Boolean> {
        val result = countBullsCows()
        nList[round] = s
        bList[round] = result.first.toString()
        cList[round] = result.second.toString()
        if (result.first == 4) {
            return Pair(true, true)
        }
        round++
        if (round == roundLimit) {
            return Pair(true, false)
        }
        return Pair(false, false)
    }

    override fun onCreate() {
        super.onCreate()
        newGame()
        Log.d("bac", "application start, targetNumber=$targetNumber")
    }
}
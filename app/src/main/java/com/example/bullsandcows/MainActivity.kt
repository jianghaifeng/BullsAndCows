package com.example.bullsandcows

import android.os.Bundle
import android.util.Log
import android.util.Pair
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private val roundLimit = 6
    private lateinit var candidate: TextView
    private var disabledButtons = hashSetOf<Button>()
    private var nList = mutableListOf<TextView>()
    private var bList = mutableListOf<TextView>()
    private var cList = mutableListOf<TextView>()
    private var round = 0
    private var targetNumber = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        candidate = findViewById(R.id.candidate)

        findViewById<Button>(R.id.btn0).setOnClickListener(this)
        findViewById<Button>(R.id.btn1).setOnClickListener(this)
        findViewById<Button>(R.id.btn2).setOnClickListener(this)
        findViewById<Button>(R.id.btn3).setOnClickListener(this)
        findViewById<Button>(R.id.btn4).setOnClickListener(this)
        findViewById<Button>(R.id.btn5).setOnClickListener(this)
        findViewById<Button>(R.id.btn6).setOnClickListener(this)
        findViewById<Button>(R.id.btn7).setOnClickListener(this)
        findViewById<Button>(R.id.btn8).setOnClickListener(this)
        findViewById<Button>(R.id.btn9).setOnClickListener(this)
        findViewById<Button>(R.id.btnCE).setOnClickListener(this)
        findViewById<Button>(R.id.btnGO).setOnClickListener(this)
        findViewById<Button>(R.id.btnNew).setOnClickListener(this)

        nList.add(0, findViewById(R.id.n1))
        nList.add(1, findViewById(R.id.n2))
        nList.add(2, findViewById(R.id.n3))
        nList.add(3, findViewById(R.id.n4))
        nList.add(4, findViewById(R.id.n5))
        nList.add(5, findViewById(R.id.n6))

        bList.add(0, findViewById(R.id.b1))
        bList.add(1, findViewById(R.id.b2))
        bList.add(2, findViewById(R.id.b3))
        bList.add(3, findViewById(R.id.b4))
        bList.add(4, findViewById(R.id.b5))
        bList.add(5, findViewById(R.id.b6))

        cList.add(0, findViewById(R.id.c1))
        cList.add(1, findViewById(R.id.c2))
        cList.add(2, findViewById(R.id.c3))
        cList.add(3, findViewById(R.id.c4))
        cList.add(4, findViewById(R.id.c5))
        cList.add(5, findViewById(R.id.c6))

        newGame()
    }

    private fun newGame() {
        generateNewNumber()
        enableAll()
        clearGuessList()
        resetCandidate()
        round = 0
    }

    private fun generateNewNumber() {
        val numbers = (0..9).toList()
        val result = numbers.shuffled()
        targetNumber =
            result.subList(0, 4).joinToString(separator = "", transform = { i -> i.toString() })
        Log.d("bac", targetNumber)
    }

    private fun isNumberBtn(id: Int): Boolean {
        val idSet = hashSetOf(
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
            R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9
        )
        return idSet.contains(id)
    }

    private fun isClearBtn(id: Int): Boolean {
        return id == R.id.btnCE
    }

    private fun isSubmitBtn(id: Int): Boolean {
        return id == R.id.btnGO
    }

    private fun isNewGameBtn(id: Int): Boolean {
        return id == R.id.btnNew
    }

    private fun enableAll() {
        disabledButtons.forEach { it.isEnabled = true }
    }

    private fun clearGuessList() {
        nList.forEach { it.text = "" }
        bList.forEach { it.text = "" }
        cList.forEach { it.text = "" }
    }

    private fun countBullsCows(guess: String): Pair<Int, Int> {
        var bulls = 0
        var all = 0
        for (i in 0..3) {
            if (targetNumber[i] == guess[i]) bulls++
            if (targetNumber.contains(guess[i])) all++
        }

        return Pair(bulls, all - bulls)
    }

    private fun showAlertDialog(title: String, message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("GO ON") { _, _ -> newGame() }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun candidateFinished(): Boolean {
        return candidate.length() == 4
    }

    private fun inputCandidate(btn: Button) {
        if (!candidateFinished()) {
            candidate.append(btn.text)
            btn.isEnabled = false
            disabledButtons.add(btn)
        }
    }

    private fun resetCandidate() {
        candidate.text = ""
    }

    private fun reachRoundLimit(): Boolean {
        return round == roundLimit
    }

    private fun increaseRound() {
        round++
    }

    private fun guess(s: String): Boolean {
        nList[round].text = s
        val result = countBullsCows(candidate.text.toString())
        bList[round].text = result.first.toString()
        cList[round].text = result.second.toString()
        return result.first == 4
    }

    override fun onClick(v: View?) {
        val btn = v as Button
        if (isNumberBtn(v.id)) {
            inputCandidate(btn)
        } else if (isClearBtn(v.id)) {
            resetCandidate()
            enableAll()
        } else if (isSubmitBtn(v.id)) {
            if (candidateFinished() && !reachRoundLimit()) {
                if (guess(candidate.text.toString())) {
                    showAlertDialog("Great", "哇，答对了，你太棒了")
                    return
                }
                increaseRound()
                if (reachRoundLimit()) {
                    showAlertDialog("Sorry", "正确答案是$targetNumber，再接再厉哦")
                    return
                }
                enableAll()
                resetCandidate()
            }
        } else if (isNewGameBtn(v.id)) {
            newGame()
        }
    }
}
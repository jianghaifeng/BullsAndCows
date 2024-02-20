package com.example.bullsandcows

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var candidate: TextView

    private var btnList = mutableListOf<Button>()
    private var nTvList = mutableListOf<TextView>()
    private var bTvList = mutableListOf<TextView>()
    private var cTvList = mutableListOf<TextView>()

    private lateinit var app : BacApplication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        candidate = findViewById(R.id.candidate)

        btnList.add(0, findViewById(R.id.btn0))
        btnList.add(1, findViewById(R.id.btn1))
        btnList.add(2, findViewById(R.id.btn2))
        btnList.add(3, findViewById(R.id.btn3))
        btnList.add(4, findViewById(R.id.btn4))
        btnList.add(5, findViewById(R.id.btn5))
        btnList.add(6, findViewById(R.id.btn6))
        btnList.add(7, findViewById(R.id.btn7))
        btnList.add(8, findViewById(R.id.btn8))
        btnList.add(9, findViewById(R.id.btn9))

        nTvList.add(0, findViewById(R.id.n1))
        nTvList.add(1, findViewById(R.id.n2))
        nTvList.add(2, findViewById(R.id.n3))
        nTvList.add(3, findViewById(R.id.n4))
        nTvList.add(4, findViewById(R.id.n5))
        nTvList.add(5, findViewById(R.id.n6))

        bTvList.add(0, findViewById(R.id.b1))
        bTvList.add(1, findViewById(R.id.b2))
        bTvList.add(2, findViewById(R.id.b3))
        bTvList.add(3, findViewById(R.id.b4))
        bTvList.add(4, findViewById(R.id.b5))
        bTvList.add(5, findViewById(R.id.b6))

        cTvList.add(0, findViewById(R.id.c1))
        cTvList.add(1, findViewById(R.id.c2))
        cTvList.add(2, findViewById(R.id.c3))
        cTvList.add(3, findViewById(R.id.c4))
        cTvList.add(4, findViewById(R.id.c5))
        cTvList.add(5, findViewById(R.id.c6))

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

        app = application as BacApplication

        reload()
    }

    private fun reload() {
        candidate.text = app.candidateNumber
        app.candidateNumber.forEach { btnList[it - '0'].isEnabled = false }
        renderGuessList()
    }

    private fun reset() {
        app.candidateNumber = ""
        candidate.text = ""
        btnList.forEach { it.isEnabled = true }
        renderGuessList()
    }

    private fun renderGuessList() {
        app.nList.forEachIndexed { index, s -> nTvList[index].text = s }
        app.bList.forEachIndexed { index, s -> bTvList[index].text = s }
        app.cList.forEachIndexed { index, s -> cTvList[index].text = s }
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

    private fun newGame() {
        app.newGame()
        reset()
    }

    private fun showAlertDialog(title: String, message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("GO ON") { _, _ -> newGame() }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    override fun onClick(v: View?) {
        val btn = v as Button
        if (isNumberBtn(v.id)) {
            if (app.candidateNumber.length < 4) {
                app.candidateNumber += btn.text
                candidate.text = app.candidateNumber
                btn.isEnabled = false
            }
        } else if (isClearBtn(v.id)) {
            reset()
        } else if (isNewGameBtn(v.id)) {
            newGame()
        } else if (isSubmitBtn(v.id)) {
            if (candidate.length() == 4) {
                val result = app.guess(candidate.text.toString())
                if (result.first) {
                    if (result.second) {
                        showAlertDialog("Great", "太棒了，回答正确！")
                    } else {
                        showAlertDialog("Sorry", "正确答案是 ${app.targetNumber}, 再接再厉哦！")
                    }
                }
                reset()
            }
        }
    }
}
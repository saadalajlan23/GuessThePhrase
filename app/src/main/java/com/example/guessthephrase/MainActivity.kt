package com.example.guessthephrase

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import com.google.android.material.snackbar.Snackbar
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private lateinit var clRoot: ConstraintLayout
    private lateinit var guessEditText: EditText
    private lateinit var guessButton: Button
    private lateinit var answers: ArrayList<String>
    private lateinit var tvPhrase: TextView
    private lateinit var tvLetters: TextView

    private val answer = "android studio"
    private val myAnswerDictionary = mutableMapOf<Int, Char>()
    private var myAnswer = " "
    private var guessedLetters = ""
    private var count = 0
    var msg = ""
    private var guessPhrase = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        for(i in answer.indices){
            if(answer[i] == ' '){
                myAnswer += ' '
                myAnswerDictionary[i] = ' '

            }else{
                myAnswer += '-'
                myAnswerDictionary[i] = '-'

            }
        }

        clRoot = findViewById(R.id.clRoot)
        answers = ArrayList()

        rvMessages.adapter = RecyclerViewAdapter( answers)
        rvMessages.layoutManager = LinearLayoutManager(this)

        guessEditText = findViewById(R.id.etGuessField)
        guessButton = findViewById(R.id.btGuessButton)
        guessButton.setOnClickListener { addMessage() }

        tvPhrase = findViewById(R.id.tvPhrase)
        tvLetters = findViewById(R.id.tvLetters)

        updateText()
    }

    private fun addMessage(){
         msg = guessEditText.text.toString()

            if(msg.isNotEmpty() && msg.length==1){
                myAnswer = ""
                guessPhrase = false
                checkLetters(msg[0])
            }else{
                Snackbar.make(clRoot, "Please enter one letter only", Snackbar.LENGTH_LONG).show()
            }


        guessEditText.text.clear()
        guessEditText.clearFocus()
        rvMessages.adapter?.notifyDataSetChanged()
    }

    private fun disableEntry(){
        guessButton.isEnabled = false
        guessButton.isClickable = false
        guessButton.isVisible = false
        guessEditText.isEnabled = false
        guessEditText.isClickable = false
        guessEditText.isVisible = false


    }

    private fun updateText(){
        tvPhrase.text = "Phrase:" + myAnswer
        tvLetters.text = "Guessed Letters:" + guessedLetters

    }

    private fun checkLetters(guessedLetter: Char){
        var found = 0
        for(i in answer.indices){
            if(answer[i] == guessedLetter){
                myAnswerDictionary[i] = guessedLetter
                found++
            }
        }
        for(i in myAnswerDictionary){myAnswer += myAnswerDictionary[i.key]}
        if(myAnswer==answer){
            disableEntry()
            showAlertDialog("You win!\n\nPlay again?")
        }
        if(guessedLetters.isEmpty()){guessedLetters+=guessedLetter}else{guessedLetters+=", "+guessedLetter}
        if(found>0){

            answers.add("Found $found ${guessedLetter}")
        }else{
            answers.add("Wrong guess: $msg")

        }
        count++
        val guessesLeft = 10 - count
        if(count<10)
        {answers.add("$guessesLeft guesses remaining")}
        else {
            showAlertDialog("Sorry You lose!\n\n Do you want Play again?")
            disableEntry()
        }

        updateText()
        rvMessages.scrollToPosition(answers.size - 1)

    }

    private fun showAlertDialog(title: String) {
        // build alert dialog
        val dialogBuilder = AlertDialog.Builder(this)

        // set message of alert dialog
        dialogBuilder.setMessage(title)
            // if the dialog is cancelable
            .setCancelable(false)
            // positive button text and action
            .setPositiveButton("Yes", DialogInterface.OnClickListener {
                    dialog, id -> this.recreate()
            })
            // negative button text and action
            .setNegativeButton("No", DialogInterface.OnClickListener {
                    dialog, id -> dialog.cancel()
            })

        // create dialog box
        val alert = dialogBuilder.create()
        // set title for alert dialog box
        alert.setTitle("Game Over")
        // show alert dialog
        alert.show()
    }
}

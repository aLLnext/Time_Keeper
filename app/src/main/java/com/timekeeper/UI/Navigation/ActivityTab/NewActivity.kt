package com.timekeeper.UI.Navigation.ActivityTab

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import com.example.toxaxab.timekeeper.R

class NewActivity : AppCompatActivity() {
    private lateinit var editName: EditText
    private lateinit var editComment: EditText

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_activity)
        editName = findViewById(R.id.InputName)
        editComment = findViewById(R.id.InputComment)

        val button = findViewById<Button>(R.id.button_save)
        button.setOnClickListener {
            val replyIntent = Intent()
            if (TextUtils.isEmpty(editName.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                var name = editName.text.toString()
                var comment = ""
                if (!TextUtils.isEmpty(editComment.text)) {
                    comment = editComment.text.toString()
                }
                val list = arrayListOf(name, comment)
                replyIntent.putExtra(EXTRA_REPLY, list)
                //replyIntent.putExtra(EXTRA_REPLY, name)
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }
    }

    companion object {
        const val EXTRA_REPLY = "newActivity.Reply"
    }

}
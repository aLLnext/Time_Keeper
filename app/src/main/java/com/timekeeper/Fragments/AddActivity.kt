package com.timekeeper.Fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import com.timekeeper.R
import kotlinx.android.synthetic.main.activity_add.*

class AddActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = resources.getString(R.string.add_activity_title)

        button_save.setOnClickListener {
            val replyIntent = Intent()
            if (TextUtils.isEmpty(InputName.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                val name = InputName.text.toString()
                var comment = ""
                if (!TextUtils.isEmpty(InputComment.text)) {
                    comment = InputComment.text.toString()
                }
                val list = arrayListOf(name, comment)
                replyIntent.putExtra(EXTRA_REPLY, list)
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item!!.itemId
        if (id == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val EXTRA_REPLY = "com.timekeeper.AddActivity.Reply"
    }
}

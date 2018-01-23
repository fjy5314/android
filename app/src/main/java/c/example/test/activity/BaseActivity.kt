package c.example.test.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import c.example.test.R
import kotlinx.android.synthetic.main.layout_title.*

/**
 * Created by flny on 2018/1/15.
 */
abstract class BaseActivity : AppCompatActivity() {

    lateinit var titleTextView: TextView
    lateinit var messageButton: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(initContentView())
        titleTextView = findViewById(R.id.tv_title)
        titleTextView.text = initTitle()
        messageButton = findViewById(R.id.ib_message_button)
        messageButton.setOnClickListener {
            Toast.makeText(this, "taskListView" , Toast.LENGTH_SHORT).show();
            startActivity(Intent(this, MessageActivity::class.java))

        }
        initView(savedInstanceState)
    }

    abstract fun initContentView(): Int

    abstract fun initView(savedInstanceState: Bundle?)

    abstract fun initTitle(): String
}
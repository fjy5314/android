package c.example.test.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import  c.example.test.R

/**
 * Created by flny on 2018/1/15.
 */
class MessageInfoActivity : BaseActivity() {
    private var id: String?=null
    override fun initView(savedInstanceState: Bundle?) {
        val getIntent = getIntent()
        id = getIntent.getStringExtra("id")
        val messageButton = findViewById<ImageButton>(R.id.ib_message_button)
        messageButton.setVisibility(View.INVISIBLE)
        val mark = findViewById<TextView>(R.id.tv_mark)
        mark.setVisibility(View.INVISIBLE)
        init()
        val button :Button=findViewById(R.id.btn_message_submit)
        button.setOnClickListener {
            Toast.makeText(baseContext, "xxxxx", Toast.LENGTH_SHORT).show()
            val intent=Intent(this, MessageReportActivity::class.java)
            intent.putExtra("id",id)
            startActivity(intent)
        }
    }
    private fun init(){
        Toast.makeText(baseContext, id.toString(), Toast.LENGTH_SHORT).show()
    }
    override fun initTitle(): String {
        return "消息详情"
    }

    override fun initContentView(): Int {
        return R.layout.activity_message_info
    }
}
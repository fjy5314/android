package c.example.test.activity

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import c.example.test.R

/**
 * Created by flny on 2018/1/15.
 */
class MessageActivity : BaseActivity() {
    override fun initView(savedInstanceState: Bundle?) {
      val  messageButton = findViewById<ImageButton>(R.id.ib_message_button)
        messageButton.setVisibility(View.INVISIBLE)
        val  mark = findViewById<TextView>(R.id.tv_mark)
        mark.setVisibility(View.INVISIBLE)
    }

    override fun initTitle(): String {
        return "消息通知"
    }

    override fun initContentView(): Int {
        return R.layout.activity_map
    }
}
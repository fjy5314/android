package c.example.test.activity

import android.os.Bundle
import c.example.test.R

/**
 * Created by flny on 2018/1/15.
 */
class EventInfoActivity : BaseActivity() {
    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun initTitle(): String {
        return "事件记录详情"
    }

    override fun initContentView(): Int {
        return R.layout.activity_event_info
    }
}
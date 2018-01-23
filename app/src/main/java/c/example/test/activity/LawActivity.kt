package c.example.test.activity

import android.os.Bundle
import  c.example.test.R

/**
 * Created by flny on 2018/1/15.
 */
class LawActivity : BaseActivity() {
    override fun initView(savedInstanceState: Bundle?) {
    }

    override fun initTitle(): String {
        return "法律法规"
    }

    override fun initContentView(): Int {
        return R.layout.activity_law
    }
}
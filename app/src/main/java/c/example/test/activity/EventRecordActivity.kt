package c.example.test.activity

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.Toast
import  c.example.test.R
import c.example.test.adapter.BaseCardKeyAdapter
import c.example.test.model.BaseCard

/**
 * Created by flny on 2018/1/15.
 */
class EventRecordActivity : BaseActivity() {
    private var eventRecordList = ArrayList<BaseCard>()
    override fun initView(savedInstanceState: Bundle?) {

    }

    fun initEventRecord() {
        eventRecordList.add(BaseCard(R.mipmap.ic_bell, "x", "c", "v", "未读消息"))
        eventRecordList.add(BaseCard(R.mipmap.ic_bell, "x", "c", "v", "未读消息"))
        eventRecordList.add(BaseCard(R.mipmap.ic_bell, "x", "c", "v", "qwe"))
        eventRecordList.add(BaseCard(R.mipmap.ic_bell, "x", "c", "v", "qwe"))
        initListView(eventRecordList, Intent(this, MessageInfoActivity::class.java))
    }

    private fun initListView(list: ArrayList<BaseCard>, intent: Intent) {
        var listView: ListView = ListView(this)
        listView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        listView.adapter = BaseCardKeyAdapter(list, baseContext, false, intent)
        listView.setOnItemClickListener { _, _, position, _ ->
            Toast.makeText(this, "activity_event_record" + position, Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MapActivity::class.java))
        }
    }


    override fun initTitle(): String {
        return "事件记录"
    }

    override fun initContentView(): Int {
        return R.layout.activity_event_record
    }
}
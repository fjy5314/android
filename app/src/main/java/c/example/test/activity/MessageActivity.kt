package c.example.test.activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.View
import android.widget.*
import  c.example.test.R
import c.example.test.adapter.BaseCardKeyAdapter
import c.example.test.adapter.TaskPagerAdapter
import c.example.test.model.BaseCard

/**
 * Created by flny on 2018/1/15.
 */
class MessageActivity : BaseActivity() {
    private var buttonList = ArrayList<Button>()
    private lateinit var taskViewPager: ViewPager
    private var viewList = ArrayList<View>()
    private var informList = ArrayList<BaseCard>()
    private var alarmList = ArrayList<BaseCard>()
    private var reportList = ArrayList<BaseCard>()
    override fun initView(savedInstanceState: Bundle?) {
        val messageButton = findViewById<ImageButton>(R.id.ib_message_button)
        messageButton.setVisibility(View.INVISIBLE)
        val mark = findViewById<TextView>(R.id.tv_mark)
        mark.setVisibility(View.INVISIBLE)
        initButtonList()
        initInform()
        initAlarm()
        initReport()
        taskViewPager.adapter = TaskPagerAdapter(viewList)

        taskViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                for ((index, button) in buttonList.withIndex()) {
                    if (index == position) {
                        buttonList[index].setTextColor(resources.getColor(R.color.colorWhite))
                        buttonList[index].setBackgroundColor(resources.getColor(R.color.colorSkyBlue))
                    }else{
                        buttonList[index].setTextColor(resources.getColor(R.color.colorSkyBlue))
                        buttonList[index].setBackgroundColor(resources.getColor(R.color.colorWhite))
                    }
                }
            }
        })
    }
    private fun initButtonList(){
        taskViewPager = findViewById<ViewPager>(R.id.vp_base)
        findViewById<Button>(R.id.btn_message_inform).setBackgroundColor(resources.getColor(R.color.colorSkyBlue))
        findViewById<Button>(R.id.btn_message_inform).setTextColor(resources.getColor(R.color.colorWhite))
        buttonList.add(findViewById<Button>(R.id.btn_message_inform))
        buttonList.add(findViewById<Button>(R.id.btn_message_alarm))
        buttonList.add(findViewById<Button>(R.id.btn_message_report))
        for ((index, button) in buttonList.withIndex()) {
            button.setOnClickListener { view ->
                Toast.makeText(baseContext, index.toString(), Toast.LENGTH_SHORT).show()
                taskViewPager.setCurrentItem(index, false)
                buttonList.forEach {
                    it.setTextColor(resources.getColor(R.color.colorSkyBlue))
                    it.setBackgroundColor(resources.getColor(R.color.colorWhite))
                }
                (view as Button).setTextColor(resources.getColor(R.color.colorWhite))
                (view as Button).setBackgroundColor(resources.getColor(R.color.colorSkyBlue))
            }
        }
    }

    private fun initInform(){
        informList.add(BaseCard(R.mipmap.ic_bell, "x", "c", "v", "未读消息"))
        informList.add(BaseCard(R.mipmap.ic_bell, "x", "c", "v", "未读消息"))
        informList.add(BaseCard(R.mipmap.ic_bell, "x", "c", "v", "qwe"))
        informList.add(BaseCard(R.mipmap.ic_bell, "x", "c", "v", "qwe"))
        initListView(informList,Intent(this, MessageInfoActivity::class.java))
    }
    private fun initAlarm(){
        alarmList.add(BaseCard(R.mipmap.ic_bell, "x", "c", "v", "未读消息"))
        alarmList.add(BaseCard(R.mipmap.ic_bell, "x", "c", "v", "未读消息"))
        alarmList.add(BaseCard(R.mipmap.ic_bell, "x", "c", "v", "qwe"))
        initListView(alarmList,Intent(this, MessageInfoActivity::class.java))
    }
    private fun initReport(){
        reportList.add(BaseCard(R.mipmap.ic_bell, "x", "c", "v", "未读消息"))
        reportList.add(BaseCard(R.mipmap.ic_bell, "x", "c", "v", "未读消息"))
        reportList.add(BaseCard(R.mipmap.ic_bell, "x", "c", "v", "qwe"))
        reportList.add(BaseCard(R.mipmap.ic_bell, "x", "c", "v", "qwe"))
        initListView(reportList,Intent(this, MessageInfoActivity::class.java))
    }
    private fun initListView(list: ArrayList<BaseCard>, intent: Intent){
        var listView: ListView = ListView(this)
        listView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        listView.adapter = BaseCardKeyAdapter(list, baseContext, false, intent)
        viewList.add(listView)
    }

    override fun initTitle(): String {
        return "消息中心"
    }

    override fun initContentView(): Int {
        return R.layout.activity_message
    }
}
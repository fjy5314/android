package c.example.test.activity

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.Toast
import  c.example.test.R
import c.example.test.adapter.BaseCardAdapter
import c.example.test.adapter.TaskPagerAdapter
import c.example.test.model.BaseCard

/**
 * Created by flny on 2018/1/15.
 */
class ReportActivity : BaseActivity() {
    private var buttonList = ArrayList<Button>()
    private lateinit var taskViewPager: ViewPager
    private var viewList = ArrayList<View>()
    private var todoList = ArrayList<BaseCard>()
    private var historyList = ArrayList<BaseCard>()

    override fun initView(savedInstanceState: Bundle?) {

        taskViewPager = findViewById<ViewPager>(R.id.vp_task)
        val btn1: Button=findViewById(R.id.btn_todo_task)
        btn1.text="待提交"
        val btn2: Button=findViewById(R.id.btn_history_task)
        btn2.text="历史报告"
        buttonList.add(btn1)
        buttonList.add(btn2)
        for ((index, button) in buttonList.withIndex()) {
            button.setOnClickListener { view ->
                Toast.makeText(baseContext, index.toString(), Toast.LENGTH_SHORT).show()
                taskViewPager.setCurrentItem(index, false)
                buttonList.forEach {
                    it.setTextColor(resources.getColor(R.color.colorGray))
                }
                (view as Button).setTextColor(resources.getColor(R.color.colorPrimary))
            }
        }
        initTodoList()
        initHistoryList()
        taskViewPager.adapter = TaskPagerAdapter(viewList)

        taskViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                for ((index, button) in buttonList.withIndex()) {
                    if (index == position) {
                        buttonList[index].setTextColor(resources.getColor(R.color.colorPrimary))
                    }else{
                        buttonList[index].setTextColor(resources.getColor(R.color.colorGray))
                    }
                }
            }
        })


    }
    private fun initTodoList() {
        todoList.add(BaseCard(R.mipmap.ic_bell, "x", "c", "v"))
        todoList.add(BaseCard(R.mipmap.ic_email, "x", "c", "v"))
        todoList.add(BaseCard(R.mipmap.ic_bell, "x", "c", "v"))
        todoList.add(BaseCard(R.mipmap.ic_bell, "x", "c", "v"))
        initTodoListView(todoList)

    }
    private fun initHistoryList() {
        historyList.add(BaseCard(R.mipmap.ic_bell, "x", "c", "v"))
        historyList.add(BaseCard(R.mipmap.ic_email, "x", "c", "v"))
        historyList.add(BaseCard(R.mipmap.ic_bell, "x", "c", "v"))
        historyList.add(BaseCard(R.mipmap.ic_bell, "x", "c", "v"))
        initTodoListView(historyList)

    }

    fun initTodoListView(todoList:ArrayList<BaseCard>) {
        var xxx: ListView = ListView(this)
        xxx.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        xxx.adapter = BaseCardAdapter(todoList, baseContext)
        viewList.add(xxx)
    }


    override fun initTitle(): String {
        return "我的报告"
    }

    override fun initContentView(): Int {
        return R.layout.activity_task
    }
}
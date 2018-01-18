package c.example.test.activity

import android.os.Bundle
import android.widget.CalendarView
import c.example.test.R
import android.widget.Toast
import android.widget.CalendarView.OnDateChangeListener
import c.example.test.R.id.calendarView



/**
 * Created by flny on 2018/1/15.
 */
class ScheduleActivity : BaseActivity() {
    private lateinit var calendarView: CalendarView
    override fun initView(savedInstanceState: Bundle?) {
        calendarView = findViewById(R.id.calendarView)
        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            Toast.makeText(this@ScheduleActivity,
                    "选中的日期为" + year + "年" + (month+1) + "月" + dayOfMonth + "日", Toast.LENGTH_LONG).show()


        }
    }
    fun initTask(){

    }
    override fun initTitle(): String {
        return "我的日程"
    }

    override fun initContentView(): Int {
        return R.layout.activity_schedule
    }
}
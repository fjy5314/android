package c.example.test.activity

import android.content.Context
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.View
import android.view.ViewGroup
import android.widget.*
import  c.example.test.R
import c.example.test.adapter.TaskPagerAdapter
import c.example.test.model.Task

/**
 * Created by flny on 2018/1/15.
 */
class TaskActivity : BaseActivity() {

    private var buttonList = ArrayList<Button>()

    private lateinit var taskViewPager: ViewPager
    private var viewList = ArrayList<View>()

    private lateinit var todoTaskListView: ListView
    private var todoTaskList = ArrayList<Task>()

    private lateinit var historyTaskListView: ListView
    private var historyTaskList = ArrayList<Task>()


    override fun initView(savedInstanceState: Bundle?) {
        taskViewPager = findViewById<ViewPager>(R.id.vp_task)

        buttonList.add(findViewById(R.id.btn_todo_task))
        buttonList.add(findViewById(R.id.btn_history_task))
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

        initTodoTaskList()
        initTodoTaskListView()

        initHistoryTaskList()
        initHistoryTaskListView()

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

    private fun initTodoTaskList() {
        todoTaskList.add(Task(R.mipmap.ic_bell, "x", "c", "v"))
        todoTaskList.add(Task(R.mipmap.ic_email, "x", "c", "v"))
        todoTaskList.add(Task(R.mipmap.ic_bell, "x", "c", "v"))
        todoTaskList.add(Task(R.mipmap.ic_bell, "x", "c", "v"))


    }

    fun initTodoTaskListView() {
        todoTaskListView = ListView(this)
        todoTaskListView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        todoTaskListView.adapter = TodoTaskListViewAdapter(todoTaskList, baseContext)
        viewList.add(todoTaskListView)
    }

    fun initHistoryTaskListView() {
        historyTaskListView = ListView(this)
        historyTaskListView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        historyTaskListView.adapter = HistoryTaskListViewAdapter(historyTaskList, baseContext)
        viewList.add(historyTaskListView)
    }

    private fun initHistoryTaskList() {
        historyTaskList.add(Task(R.mipmap.ic_back, "x", "c", "v"))
        historyTaskList.add(Task(R.mipmap.ic_bell, "x", "c", "v"))
        historyTaskList.add(Task(R.mipmap.ic_email, "x", "c", "v"))
        historyTaskList.add(Task(R.mipmap.ic_bell, "x", "c", "v"))
    }

    override fun initContentView(): Int {
        return R.layout.activity_task
    }

    override fun initTitle(): String {
        return "我的任务"
    }

    class TodoTaskListViewAdapter(private var taskList: ArrayList<Task>, private var context: Context) : BaseAdapter() {

        class ViewHolder(viewItem: View) {
            val iconImagheVierw: ImageView = viewItem.findViewById(R.id.iv_todo_type_pic)
            val title: TextView=viewItem.findViewById(R.id.tv_todo_title)
            val desc: TextView=viewItem.findViewById(R.id.tv_todo_desc)
            val time: TextView=viewItem.findViewById(R.id.tv_todo_time)
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val viewHolder: ViewHolder?
            val view: View
            if (convertView == null) {
                view = View.inflate(context, R.layout.item_todo_task, null)
                viewHolder = ViewHolder(view)
                view.tag = viewHolder
            } else {
                view = convertView
                viewHolder = view.tag as ViewHolder
            }
            val task = getItem(position) as Task
            viewHolder.iconImagheVierw.setImageResource(task.type)
            viewHolder.time.text= task.endTime
            viewHolder.desc.text= task.content
            viewHolder.title.text=task.title
            return view
        }

        override fun getItem(position: Int):  Any{
            return taskList.get(position)
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return taskList.size
        }

    }


    class HistoryTaskListViewAdapter(var taskList: ArrayList<Task>, var context: Context) : BaseAdapter() {

        class ViewHolder(viewItem: View) {
            val historyTaskTypePic :ImageView=viewItem.findViewById(R.id.iv_history_task_type_pic)
            val title : TextView=viewItem.findViewById(R.id.tv_history_task_title)
            val time : TextView=viewItem.findViewById(R.id.tv_history_task_time)
            val key : TextView =viewItem.findViewById(R.id.tv_history_task_month)
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val viewHolder: ViewHolder?
            val view: View
            if (convertView == null) {
                view = View.inflate(context, R.layout.item_history_task, null)
                viewHolder = ViewHolder(view)
                view.tag = viewHolder
            } else {
                view = convertView
                viewHolder = view.tag as ViewHolder
            }
            val item = getItem(position) as Task
            viewHolder.historyTaskTypePic.setImageResource(item.type)
            viewHolder.title.text=item.title
            viewHolder.time.text=item.endTime
            viewHolder.key.visibility=View.GONE
            return view
        }

        override fun getItem(position: Int): Any {
            return taskList.get(position)
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return taskList.size
        }

    }

}
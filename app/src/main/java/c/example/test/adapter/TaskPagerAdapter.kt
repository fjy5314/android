package c.example.test.adapter

import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.View


class TaskPagerAdapter(private var viewList: List<View>) : PagerAdapter() {

    override fun getCount(): Int {
        return viewList.size
    }

    override fun isViewFromObject(arg0: View, arg1: Any): Boolean {
        // TODO Auto-generated method stub
        return arg0 === arg1
    }

    override fun destroyItem(view: View, position: Int, `object`: Any) {
        (view as ViewPager).removeView(viewList[position])
    }

    override fun instantiateItem(view: View, position: Int): Any {
        (view as ViewPager).addView(viewList[position], 0)
        return viewList[position]
    }

}
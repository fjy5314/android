package com.daniulive.smartpublisher.common

import com.daniulive.smartpublisher.model.Friend
import java.util.Comparator

/**
 * Created by flny on 2018/1/24.
 */
class PinYinComparator : Comparator<Friend> {
    override fun compare(o1: Friend, o2: Friend): Int {
        if (o1.pinyin.equals("#")) {
            return 1
        } else if (o2.pinyin.equals("#")) {
            return -1
        }
        return o1.pinyin.compareTo(o2.pinyin,true)
    }
}
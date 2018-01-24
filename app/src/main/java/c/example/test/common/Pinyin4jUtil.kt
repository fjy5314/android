package c.example.test.common

import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination
import net.sourceforge.pinyin4j.PinyinHelper
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat
import java.util.*


/**
 * Created by flny on 2018/1/24.
 */
object Pinyin4jUtil {
    fun convertToFirstSpell(chinese: String): String {
        val pinyinName = StringBuffer()
        val nameChar = chinese.toCharArray()
        val defaultFormat = HanyuPinyinOutputFormat()
        defaultFormat.caseType = HanyuPinyinCaseType.UPPERCASE
        defaultFormat.toneType = HanyuPinyinToneType.WITHOUT_TONE
        for (c in nameChar) {
            if (c.toInt() > 128) {
                try {
                    val strs = PinyinHelper.toHanyuPinyinStringArray(c, defaultFormat)
                    if (strs != null) {
                        for (i in strs.indices) {
                            pinyinName.append(strs[i][0])
                            if (i != strs.size - 1) {
                                pinyinName.append(",")
                            }
                        }
                    }
                } catch (badHanyuPinyinOutputFormatCombination: BadHanyuPinyinOutputFormatCombination) {
                    badHanyuPinyinOutputFormatCombination.printStackTrace()
                }

            } else {
                pinyinName.append(c)
            }
            pinyinName.append(" ")
        }
        return parseTheChineseByObject(discountTheChinese(pinyinName.toString()))
    }

    fun convertToSpell(chinese: String): String {
        val pinyinName = StringBuffer()
        val nameChar = chinese.toCharArray()
        val defaultFormat = HanyuPinyinOutputFormat()
        defaultFormat.caseType = HanyuPinyinCaseType.LOWERCASE
        defaultFormat.toneType = HanyuPinyinToneType.WITHOUT_TONE
        for (c in nameChar) {
            if (c.toInt() > 128) {
                try {
                    val strs = PinyinHelper.toHanyuPinyinStringArray(c, defaultFormat)
                    if (strs != null) {
                        for (i in strs.indices) {
                            pinyinName.append(strs[i])
                            if (i != strs.size - 1) {
                                pinyinName.append(",")
                            }
                        }
                    }
                } catch (badHanyuPinyinOutputFormatCombination: BadHanyuPinyinOutputFormatCombination) {
                    badHanyuPinyinOutputFormatCombination.printStackTrace()
                }

            } else {
                pinyinName.append(c)
            }
            pinyinName.append("  ")
        }
        return parseTheChineseByObject(discountTheChinese(pinyinName.toString()))
    }

    fun convertToSpellList(chinese: String): List<String> {
        val pinyinName = StringBuffer()
        val nameChar = chinese.toCharArray()
        val defaultFormat = HanyuPinyinOutputFormat()
        defaultFormat.caseType = HanyuPinyinCaseType.LOWERCASE
        defaultFormat.toneType = HanyuPinyinToneType.WITHOUT_TONE
        for (c in nameChar) {
            if (c.toInt() > 128) {
                try {
                    val strs = PinyinHelper.toHanyuPinyinStringArray(c, defaultFormat)
                    if (strs != null) {
                        for (i in strs.indices) {
                            pinyinName.append(strs[i])
                            if (i != strs.size - 1) {
                                pinyinName.append(",")
                            }
                        }
                    }
                } catch (badHanyuPinyinOutputFormatCombination: BadHanyuPinyinOutputFormatCombination) {
                    badHanyuPinyinOutputFormatCombination.printStackTrace()
                }

            } else {
                pinyinName.append(c)
            }
            pinyinName.append("  ")
        }
        return parseTheChineseByObjectToList(discountTheChinese(pinyinName.toString()))
    }

    private fun discountTheChinese(theStr: String): List<Map<String, Int>> {
        val mapList = ArrayList<Map<String, Int>>()
        var onlyOne: MutableMap<String, Int>? = null
        val firsts = theStr.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        for (str in firsts) {
            onlyOne = Hashtable<String, Int>()
            val china = str.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            for (s in china) {
                var count: Int? = onlyOne!![s]
                if (count == null) {
                    onlyOne.put(s, 1)
                } else {
                    onlyOne.remove(s)
                    count++
                    onlyOne.put(s, count)
                }
            }
            mapList.add(onlyOne)
        }
        return mapList
    }

    private fun parseTheChineseByObject(list: List<Map<String, Int>>): String {
        var first: MutableMap<String, Int>? = null
        for (i in list.indices) {
            val temp = Hashtable<String, Int>()
            if (first != null) {
                for (s in first.keys) {
                    for (s1 in list[i].keys) {
                        val str = s + s1
                        temp.put(str, 1)
                    }
                }
                if (temp != null && temp.size > 0) {
                    first.clear()
                }
            } else {
                for (s in list[i].keys) {
                    temp.put(s, 1)
                }
            }
            if (temp != null && temp.size > 0) {
                first = temp
            }
        }
        var returnStr = ""
        val returnList = ArrayList<String>()
        if (first != null) {
            for (str in first.keys) {
                returnStr += str + " "
                returnList.add(str)
            }
        }
        if (returnStr.length > 0) {
            returnStr = returnStr.substring(0, returnStr.length - 1)
        }
        return returnList.get(0)
    }

    private fun parseTheChineseByObjectToList(list: List<Map<String, Int>>): List<String> {
        var first: MutableMap<String, Int>? = null
        for (i in list.indices) {
            val temp = Hashtable<String, Int>()
            if (first != null) {
                for (s in first.keys) {
                    for (s1 in list[i].keys) {
                        val str = s + s1
                        temp.put(str, 1)
                    }
                }
                if (temp != null && temp.size > 0) {
                    first.clear()
                }
            } else {
                for (s in list[i].keys) {
                    temp.put(s, 1)
                }
            }
            if (temp != null && temp.size > 0) {
                first = temp
            }
        }
        val returnList = ArrayList<String>()
        if (first != null) {
            for (str in first.keys) {
                returnList.add(str)
            }
        }
        return returnList
    }

    fun isPinYin(string: String): Boolean {
        val chars = string.toCharArray()
        for (c in chars) {
            if (c.toInt() >= 65 && c.toInt() <= 90 || c.toInt() >= 97 && c.toInt() <= 122) {

            } else {
                return false
            }
        }
        return true
    }
}

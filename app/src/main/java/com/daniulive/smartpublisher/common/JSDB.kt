package com.daniulive.smartpublisher.common

import android.content.Context
import com.daniulive.smartpublisher.greendao.DaoMaster
import com.daniulive.smartpublisher.greendao.PointDao

/**
 * Created by flny on 2018/3/30.
 */
class JSDB() {
    companion object {
        val DB_NAME="js.db"
    }
    fun getPoint(context: Context): PointDao {
        var devOpenHelper = DaoMaster.DevOpenHelper(context, DB_NAME, null)
        var daoMaster = DaoMaster(devOpenHelper.writableDb)
        var daoSession = daoMaster.newSession()
        return daoSession.pointDao
    }


}
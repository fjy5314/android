@file:Suppress("DEPRECATION")

package com.daniulive.smartpublisher.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.graphics.ImageFormat
import android.graphics.PixelFormat
import android.hardware.Camera
import android.hardware.Camera.*
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.*
import android.view.SurfaceHolder.Callback
import android.view.View.OnClickListener
import android.widget.*
import com.daniulive.smartplayer.SmartPlayerJniV2
import com.daniulive.smartpublisher.R
import com.daniulive.smartpublisher.SmartPublisherJni
import com.daniulive.smartpublisher.common.JSStringCallback
import com.daniulive.smartpublisher.model.ApiResult
import com.daniulive.smartpublisher.model.ClientUrl
import com.daniulive.smartpublisher.model.JSConfig
import com.eventhandle.NTSmartEventCallbackV2
import com.eventhandle.NTSmartEventID
import com.eventhandle.SmartEventCallback
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rabbitmq.client.ConnectionFactory
import com.videoengine.NTRenderer
import com.voiceengine.NTAudioRecord
import com.voiceengine.NTExternalAudioOutput
import com.zhy.http.okhttp.OkHttpUtils
import okhttp3.MediaType
import java.io.*
import java.nio.ByteBuffer
import java.text.SimpleDateFormat
import java.util.*

class CameraActivity : BaseActivity(), Callback, PreviewCallback {
    internal var audioRecord_: NTAudioRecord? = null    //for audio capture
    private var libPublisher: SmartPublisherJni? = null
    private var libPlayer = SmartPlayerJniV2() //通话
    private var playerHandle: Long = 0 // 通话状态
    private var pushType = 0
    private var sw_video_encoder_profile = 1    //default with baseline profile
    private var btnRecoderMgr: Button? = null
    private var btnMute: Button? = null    //是否静音按钮
    private var imgSwitchCamera: ImageView? = null //切换摄像头按钮
    private lateinit var btnStartPush: TextView //开始推流
    private var btnStartRecorder: Button? = null
    private var btnCaptureImage: ImageButton? = null
    private var mSurfaceView: SurfaceView? = null
    private var mSurfaceHolder: SurfaceHolder? = null
    private var mCamera: Camera? = null
    private var myAutoFocusCallback: AutoFocusCallback? = null
    private var mPreviewRunning = false
    private var isStartPushVideo = false //是否开始推流
    private var isPushing = false
    private var isRecording = false
    private var isWritelogoFileSuccess = false
    private lateinit var publishURL: String
    private var txt = "当前状态"
    private var currentCameraType = BACK    //当前打开的摄像头标记
    private var currentOrigentation = PORTRAIT
    private var curCameraIndex = -1
    private var videoWidth = 640
    private var videoHight = 480
    private var frameCount = 0
    private val recDir = "/sdcard/JScom/rec"    //for recorder path 录像存储位置
    private var is_need_local_recorder = true        // do not enable recorder in default
    private var is_mute = false
    private var imageSavePath: String? = null
    var mTimer: Timer = Timer(true) //定时器
    lateinit var mTimerTask: TimerTask  //定时任务
    lateinit var mTimerTaskGetTime: TimerTask  //定时任务
    var pushTime = 0 //推流时长
    val mhandler = Myhandler()
    @Throws(IOException::class)
    private fun readAssetFileDataToByte(ins: InputStream): ByteArray? {
        val bytestream = ByteArrayOutputStream()
        var c = 0
        c = ins.read()
        while (c != -1) {
            bytestream.write(c)
            c = ins.read()
        }

        val bytedata = bytestream.toByteArray()
        bytestream.close()
        return bytedata
    }

    override fun initView(savedInstanceState: Bundle?) {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)    //屏幕常亮
        myContext = this.applicationContext
        //设置快照路径(具体路径可自行设置)
        val storageDir = getOwnCacheDirectory(myContext, "JScom/image")//创建保存的路径
        imageSavePath = storageDir.path
        Log.i(TAG, "快照存储路径: " + imageSavePath!!)

//        try {
//
//            val logoInputStream = javaClass.getResourceAsStream("/assets/logo.png")
//            val logoData = readAssetFileDataToByte(logoInputStream)
//            if (logoData != null) {
//                try {
//                    val out = FileOutputStream(" /sdcard/JScom/failed")
//                    out.write(logoData)
//                    out.close()
//                    isWritelogoFileSuccess = true
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                    Log.e(TAG, "write logo file to /sdcard/ failed")
//                }
//            }
//
//        } catch (e: Exception) {
//            e.printStackTrace()
//            Log.e(TAG, "write logo file to /sdcard/ failed")
//        }


        btnRecoderMgr = findViewById(R.id.button_recoder_manage)
        btnRecoderMgr!!.setOnClickListener(ButtonRecorderMangerListener())
        //end
        btnMute = findViewById(R.id.button_mute)
        btnMute!!.setOnClickListener(ButtonMuteListener())


        //推流
        btnStartPush = findViewById(R.id.button_start_push)
        btnStartPush.setOnClickListener {
            if (isStartPushVideo) {
                pushTime = 0
                mTimerTask.cancel()
                mTimerTaskGetTime.cancel()
                StopPublish()
                btnStartPush.text = " "
                titleTextView.text = "执法记录"
                btnStartPush.background = resources.getDrawable(R.mipmap.ic_shoot)
                isStartPushVideo = false
                sendMessage("hangup")
                libPlayer.SmartPlayerStopPlay(playerHandle)
                libPlayer.SmartPlayerClose(playerHandle)
                playerHandle = 0
            } else {
                isStartPushVideo = true
                titleTextView.text = "执法记录中..."
                btnStartPush.background = resources.getDrawable(R.drawable.ic_circle_red)
                initTimerTaskGetTime()
                mTimer.schedule(mTimerTaskGetTime, 0, 1000)
                getPublishURL()
            }

        }
        //录像
        btnStartRecorder = findViewById(R.id.button_start_recorder)
        btnStartRecorder!!.setOnClickListener(ButtonStartRecorderListener())
        //快照
        btnCaptureImage = findViewById(R.id.button_capture_image)
        btnCaptureImage!!.setOnClickListener(ButtonCaptureImageListener())
        //前置后置摄像头切换
        imgSwitchCamera = findViewById(R.id.button_switchCamera)
        imgSwitchCamera!!.setOnClickListener(SwitchCameraListener())

        mSurfaceView = this.findViewById(R.id.surface)
        mSurfaceHolder = mSurfaceView!!.holder
        mSurfaceHolder!!.addCallback(this)
        mSurfaceHolder!!.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)

        //自动聚焦变量回调
        myAutoFocusCallback = AutoFocusCallback { success, _ ->
            if (success)
            //success表示对焦成功
            {
                Log.i(TAG, "onAutoFocus succeed...")
            } else {
                Log.i(TAG, "onAutoFocus failed...")
            }
        }

        libPublisher = SmartPublisherJni()
    }

    internal inner class SwitchCameraListener : OnClickListener {
        override fun onClick(v: View) {
            Log.i(TAG, "Switch camera..")
            try {
                switchCamera()
            } catch (e: IOException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            }

        }
    }

    internal fun CheckInitAudioRecorder() {
        if (audioRecord_ == null) {
            audioRecord_ = NTAudioRecord(this, 1)
        }

        if (audioRecord_ != null) {
            Log.i(TAG, "onCreate, call executeAudioRecordMethod..")
            // auido_ret: 0 ok, other failed
            val auidoRet = audioRecord_!!.executeAudioRecordMethod()
            Log.i(TAG, "onCreate, call executeAudioRecordMethod.. auido_ret=" + auidoRet)
        }
    }

    //   设置录像
    internal fun configRecorderFuntion(isNeedLocalRecorder: Boolean) {
        if (libPublisher != null) {
            if (isNeedLocalRecorder) {
                if (recDir != null && !recDir.isEmpty()) {
                    val ret = libPublisher!!.SmartPublisherCreateFileDirectory(recDir)
                    if (0 == ret) {
                        if (0 != libPublisher!!.SmartPublisherSetRecorderDirectory(recDir)) {
                            Log.e(TAG, "Set recoder dir failed , path:" + recDir)
                            return
                        }

                        if (0 != libPublisher!!.SmartPublisherSetRecorder(1)) {
                            Log.e(TAG, "SmartPublisherSetRecoder failed.")
                            return
                        }

                        if (0 != libPublisher!!.SmartPublisherSetRecorderFileMaxSize(200)) {
                            Log.e(TAG, "SmartPublisherSetRecoderFileMaxSize failed.")
                            return
                        }

                    } else {
                        Log.e(TAG, "Create recoder dir failed, path:" + recDir)
                    }
                }
            } else {
                if (0 != libPublisher!!.SmartPublisherSetRecorder(0)) {
                    Log.e(TAG, "SmartPublisherSetRecoder failed.")
                    return
                }
            }
        }
    }

    internal inner class ButtonRecorderMangerListener : OnClickListener {
        override fun onClick(v: View) {
            if (mCamera != null) {
                mCamera!!.stopPreview()
                mCamera!!.release()
                mCamera = null
            }

//            val intent = Intent()
//            intent.setClass(this@CameraActivity, ContactListActivity::class.java)
//            intent.putExtra("RecoderDir", recDir)
//            startActivity(intent)
        }
    }


    internal inner class ButtonMuteListener : OnClickListener {
        override fun onClick(v: View) {
            is_mute = !is_mute

            if (is_mute)
                btnMute!!.text = "取消静音"
            else
                btnMute!!.text = "静音"

            if (libPublisher != null)
                libPublisher!!.SmartPublisherSetMute(if (is_mute) 1 else 0)
        }
    }

    internal inner class EventHande : SmartEventCallback {
        override fun onCallback(code: Int, param1: Long, param2: Long, param3: String?, param4: String?, param5: Any?) {
            when (code) {
                SmartEventCallback.EVENTID.EVENT_DANIULIVE_ERC_PUBLISHER_STARTED -> txt = "开始。。"
                SmartEventCallback.EVENTID.EVENT_DANIULIVE_ERC_PUBLISHER_CONNECTING -> txt = "连接中。。"
                SmartEventCallback.EVENTID.EVENT_DANIULIVE_ERC_PUBLISHER_CONNECTION_FAILED -> txt = "连接失败。。"
                SmartEventCallback.EVENTID.EVENT_DANIULIVE_ERC_PUBLISHER_CONNECTED -> txt = "连接成功。。"
                SmartEventCallback.EVENTID.EVENT_DANIULIVE_ERC_PUBLISHER_DISCONNECTED -> txt = "连接断开。。"
                SmartEventCallback.EVENTID.EVENT_DANIULIVE_ERC_PUBLISHER_STOP -> txt = "关闭。。"
                SmartEventCallback.EVENTID.EVENT_DANIULIVE_ERC_PUBLISHER_RECORDER_START_NEW_FILE -> {
                    Log.i(TAG, "开始一个新的录像文件 : " + param3)
                    txt = "开始一个新的录像文件。。"
                }
                SmartEventCallback.EVENTID.EVENT_DANIULIVE_ERC_PUBLISHER_ONE_RECORDER_FILE_FINISHED -> {
                    Log.i(TAG, "已生成一个录像文件 : " + param3)
                    txt = "已生成一个录像文件。。"
                }

                SmartEventCallback.EVENTID.EVENT_DANIULIVE_ERC_PUBLISHER_SEND_DELAY -> {
                    Log.i(TAG, "发送时延: $param1 帧数:$param2")
                    txt = "收到发送时延.."
                }

                SmartEventCallback.EVENTID.EVENT_DANIULIVE_ERC_PUBLISHER_CAPTURE_IMAGE -> {
                    Log.i(TAG, "快照: $param1 路径：$param3")

                    if (param1 == 0L) {
                        txt = "截取快照成功。."
                    } else {
                        txt = "截取快照失败。."
                    }
                }
            }

            val str = "当前回调状态：" + txt

            Log.i(TAG, str)

        }
    }

    inner  class EventHandePlayerV2 : NTSmartEventCallbackV2 {
        override fun onNTSmartEventCallbackV2(handle: Long, id: Int, param1: Long, param2: Long, param3: String?
                                              , param4: String?, param5: Any?) {
            when(id){
                NTSmartEventID.EVENT_DANIULIVE_ERC_PLAYER_STARTED->Log.i(TAG, "[player]开始。。");
            }
        }

    }


    private fun PushVideo() {
        if (isRecording) {
            return
        }
        Log.i(TAG, "onClick start..")
        if (libPublisher != null) {
            Log.i(TAG, "start, generate random url:" + publishURL)
            configRecorderFuntion(is_need_local_recorder)

            Log.i(TAG, "videoWidth: $videoWidth videoHight: $videoHight pushType:$pushType")
            InitAndSetConfig()

            if (libPublisher!!.SmartPublisherSetURL(publishURL) != 0) {
                Log.e(TAG, "Failed to set publish stream URL..")
            }

            val isStarted = libPublisher!!.SmartPublisherStart()
            if (isStarted != 0) {
                Log.e(TAG, "Failed to publish stream..")
            } else {
                btnRecoderMgr!!.isEnabled = false
            }
        }

        if (pushType == 0 || pushType == 1) {
            CheckInitAudioRecorder()    //enable pure video publisher..
        }
    }

    private fun ConfigControlEnable(isEnable: Boolean) {
        btnRecoderMgr!!.isEnabled = isEnable

    }

    //推流配置
    private fun InitAndSetConfig() {
        Log.i(TAG, "videoWidth: " + videoWidth + " videoHight: " + videoHight
                + " pushType:" + pushType)

        var audio_opt = 1
        var video_opt = 1

        if (pushType == 1) {
            video_opt = 0
        } else if (pushType == 2) {
            audio_opt = 0
        }
        //
        // 设置上下文信息，返回 player句柄
        libPublisher!!.SmartPublisherInit(myContext, audio_opt, video_opt,
                videoWidth, videoHight)
        //设置event callback
        libPublisher!!.SetSmartPublisherEventCallback(EventHande())
        // 设置编码类型，默认 AAC编码， type设置为2时，启用 speex编码（码流更低
        libPublisher!!.SmartPublisherSetAudioCodecType(2)
        //设置 speex 编码质量，数值 越大，质量越高，范围（0,10），默认 8
        libPublisher!!.SmartPublisherSetSpeexEncoderQuality(8)
        //设置噪音抑制，噪音抑制开启后， 去除采集端背景杂音；
        libPublisher!!.SmartPublisherSetNoiseSuppression(1)
        //设置自动增益控制，保持声音稳定
        libPublisher!!.SmartPublisherSetAGC(0)
        //，设置裁剪模式(仅用于 640*480分辨 率, 裁剪主要用于移动端宽高适配)，如不设置，默认裁剪模式；
        // libPublisher.SmartPublisherSetClippingMode(0);
        //设置软编码模式下的video encoder profile，默认 baseline profil
        libPublisher!!.SmartPublisherSetSWVideoEncoderProfile(sw_video_encoder_profile)
        //设置软编码编码速度，设置 范围（1,6），1最快，6最慢，默认是 6
        libPublisher!!.SmartPublisherSetSWVideoEncoderSpeed(6)
        //设置是否启用快照
        libPublisher!!.SmartPublisherSaveImageFlag(1)
    }


    internal inner class ButtonStartRecorderListener : OnClickListener {
        override fun onClick(v: View) {
            if (isStartPushVideo) {
                return
            }

            if (isRecording) {
                stopRecorder()

                if (!isPushing) {
                    ConfigControlEnable(true)
                }

                btnStartRecorder!!.text = " 录像"

                isRecording = false

                return
            }


            Log.i(TAG, "onClick start recorder..")

            if (libPublisher == null)
                return

            isRecording = true

            if (!isPushing) {
                InitAndSetConfig()
            }

            configRecorderFuntion(true)

            val startRet = libPublisher!!.SmartPublisherStartRecorder()
            if (startRet != 0) {
                isRecording = false

                Log.e(TAG, "Failed to start recorder.")
                return
            }

            if (!isPushing) {
                if (pushType == 0 || pushType == 1) {
                    CheckInitAudioRecorder()    //enable pure video publisher..
                }
            }

            if (!isPushing) {
                ConfigControlEnable(false)
            }

            btnStartRecorder!!.text = " 停止录像"
        }
    }

    internal inner class ButtonCaptureImageListener : OnClickListener {
        @SuppressLint("SimpleDateFormat")
        override fun onClick(v: View) {
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            val imageFileName = "dn_" + timeStamp    //创建以时间命名的文件名称

            val imagePath = "$imageSavePath/$imageFileName.png"

            Log.i(TAG, "imagePath:" + imagePath)

            libPublisher!!.SmartPublisherSaveCurImage(imagePath)
        }
    }


    private fun stopPush() {
        if (!isRecording) {
            if (audioRecord_ != null) {
                Log.i(TAG, "stopPush, call audioRecord_.StopRecording..")
                audioRecord_!!.StopRecording()
                audioRecord_ = null
            }
        }

        if (libPublisher != null) {
            libPublisher!!.SmartPublisherStopPublisher()
        }
    }

    private fun stopRecorder() {
        if (!isPushing) {
            if (audioRecord_ != null) {
                Log.i(TAG, "stopRecorder, call audioRecord_.StopRecording..")
                audioRecord_!!.StopRecording()
                audioRecord_ = null
            }
        }

        if (libPublisher != null) {
            libPublisher!!.SmartPublisherStopRecorder()
        }
    }


    private fun setCameraFPS(parameters: Camera.Parameters?) {
        if (parameters == null)
            return

        var findRange: IntArray? = null

        val defFPS = 20 * 1000

        val fpsList = parameters.supportedPreviewFpsRange
        if (fpsList != null && fpsList.size > 0) {
            for (i in fpsList.indices) {
                val range = fpsList[i]
                if (range != null
                        && Camera.Parameters.PREVIEW_FPS_MIN_INDEX < range.size
                        && Camera.Parameters.PREVIEW_FPS_MAX_INDEX < range.size) {
                    Log.i(TAG, "Camera index:" + i + " support min fps:" + range[Camera.Parameters.PREVIEW_FPS_MIN_INDEX])

                    Log.i(TAG, "Camera index:" + i + " support max fps:" + range[Camera.Parameters.PREVIEW_FPS_MAX_INDEX])

                    if (findRange == null) {
                        if (defFPS <= range[Camera.Parameters.PREVIEW_FPS_MAX_INDEX]) {
                            findRange = range

                            Log.i(TAG, "Camera found appropriate fps, min fps:" + range[Camera.Parameters.PREVIEW_FPS_MIN_INDEX]
                                    + " ,max fps:" + range[Camera.Parameters.PREVIEW_FPS_MAX_INDEX])
                        }
                    }
                }
            }
        }

        if (findRange != null) {
            parameters.setPreviewFpsRange(findRange[Camera.Parameters.PREVIEW_FPS_MIN_INDEX], findRange[Camera.Parameters.PREVIEW_FPS_MAX_INDEX])
        }
    }

    /*it will call when surfaceChanged*/
    private fun initCamera(holder: SurfaceHolder?) {
        Log.i(TAG, "initCamera..")

        if (mPreviewRunning)
            mCamera!!.stopPreview()

        val parameters: Camera.Parameters
        try {
            parameters = mCamera!!.parameters
        } catch (e: Exception) {
            // TODO Auto-generated catch block
            e.printStackTrace()
            return
        }

        parameters.setPreviewSize(videoWidth, videoHight)
        parameters.pictureFormat = PixelFormat.JPEG
        parameters.previewFormat = PixelFormat.YCbCr_420_SP

        setCameraFPS(parameters)

        setCameraDisplayOrientation(this, curCameraIndex, mCamera)

        mCamera!!.parameters = parameters

        val bufferSize = ((videoWidth or 0xf) + 1) * videoHight * ImageFormat.getBitsPerPixel(parameters.previewFormat) / 8
        //设置预览数据的缓冲区
        mCamera!!.addCallbackBuffer(ByteArray(bufferSize))
        //启动预览
        mCamera!!.setPreviewCallbackWithBuffer(this)
        try {
            mCamera!!.setPreviewDisplay(holder)
        } catch (ex: Exception) {
            if (null != mCamera) {
                mCamera!!.release()
                mCamera = null
            }
            ex.printStackTrace()
        }

        mCamera!!.startPreview()
        mCamera!!.autoFocus(myAutoFocusCallback)
        mPreviewRunning = true
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        Log.i(TAG, "surfaceCreated..")
        try {

            var CammeraIndex = findBackCamera()
            Log.i(TAG, "BackCamera: " + CammeraIndex)

            if (CammeraIndex == -1) {
                CammeraIndex = findFrontCamera()
                currentCameraType = FRONT
                imgSwitchCamera!!.isEnabled = false
                if (CammeraIndex == -1) {
                    Log.i(TAG, "NO camera!!")
                    return
                }
            } else {
                currentCameraType = BACK
            }

            if (mCamera == null) {
                mCamera = openCamera(currentCameraType)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        Log.i(TAG, "surfaceChanged..")
        initCamera(holder)
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        Log.i(TAG, "Surface Destroyed")
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        try {
            super.onConfigurationChanged(newConfig)
            Log.i(TAG, "onConfigurationChanged, start:" + isStartPushVideo)
            if (this.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                if (!isStartPushVideo && !isPushing && !isRecording) {
                    currentOrigentation = LANDSCAPE
                }
            } else if (this.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                if (!isStartPushVideo && !isPushing && !isRecording) {
                    currentOrigentation = PORTRAIT
                }
            }
        } catch (ex: Exception) {
        }

    }

    //获取到的每一帧数据
    override fun onPreviewFrame(data: ByteArray?, camera: Camera) {
        frameCount++
        if (frameCount % 3000 == 0) {
            Log.i("OnPre", "gc+")
            System.gc()
            Log.i("OnPre", "gc-")
        }

        if (data == null) {
            val params = camera.parameters
            val size = params.previewSize
            val bufferSize = ((size.width or 0x1f) + 1) * size.height * ImageFormat.getBitsPerPixel(params.previewFormat) / 8
            camera.addCallbackBuffer(ByteArray(bufferSize))
        } else {
            if (isStartPushVideo || isPushing || isRecording) {
                libPublisher!!.SmartPublisherOnCaptureVideoData(data, data.size, currentCameraType, currentOrigentation)
            }

            camera.addCallbackBuffer(data)
        }
    }

    @SuppressLint("NewApi")
    private fun openCamera(type: Int): Camera? {
        var frontIndex = -1
        var backIndex = -1
        val cameraCount = Camera.getNumberOfCameras()
        Log.i(TAG, "cameraCount: " + cameraCount)

        val info = CameraInfo()
        for (cameraIndex in 0 until cameraCount) {
            Camera.getCameraInfo(cameraIndex, info)

            if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
                frontIndex = cameraIndex
            } else if (info.facing == CameraInfo.CAMERA_FACING_BACK) {
                backIndex = cameraIndex
            }
        }

        currentCameraType = type
        if (type == FRONT && frontIndex != -1) {
            curCameraIndex = frontIndex
            return Camera.open(frontIndex)
        } else if (type == BACK && backIndex != -1) {
            curCameraIndex = backIndex
            return Camera.open(backIndex)
        }
        return null
    }

    //切换摄像头
    @Throws(IOException::class)
    private fun switchCamera() {
        mCamera!!.setPreviewCallback(null)
        mCamera!!.stopPreview()
        mCamera!!.release()
        if (currentCameraType == FRONT) {
            mCamera = openCamera(BACK)
        } else if (currentCameraType == BACK) {
            mCamera = openCamera(FRONT)
        }

        initCamera(mSurfaceHolder)
    }

    //停止推流
    private fun StopPublish() {
        if (audioRecord_ != null) {
            Log.i(TAG, "surfaceDestroyed, call StopRecording..")
            audioRecord_!!.StopRecording()
            audioRecord_ = null
        }

        if (libPublisher != null) {
            libPublisher!!.SmartPublisherStop()
        }
    }

    //Check if it has front camera
    private fun findFrontCamera(): Int {
        var cameraCount = 0
        val cameraInfo = Camera.CameraInfo()
        cameraCount = Camera.getNumberOfCameras()

        for (camIdx in 0 until cameraCount) {
            Camera.getCameraInfo(camIdx, cameraInfo)
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                return camIdx
            }
        }
        return -1
    }

    //Check if it has back camera
    private fun findBackCamera(): Int {
        var cameraCount = 0
        val cameraInfo = Camera.CameraInfo()
        cameraCount = Camera.getNumberOfCameras()

        for (camIdx in 0 until cameraCount) {
            Camera.getCameraInfo(camIdx, cameraInfo)
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                return camIdx
            }
        }
        return -1
    }

    private fun setCameraDisplayOrientation(activity: Activity, cameraId: Int, camera: android.hardware.Camera?) {
        val info = android.hardware.Camera.CameraInfo()
        android.hardware.Camera.getCameraInfo(cameraId, info)
        val rotation = activity.windowManager.defaultDisplay.rotation
        var degrees = 0
        when (rotation) {
            Surface.ROTATION_0 -> degrees = 0
            Surface.ROTATION_90 -> degrees = 90
            Surface.ROTATION_180 -> degrees = 180
            Surface.ROTATION_270 -> degrees = 270
        }
        var result: Int
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360
            result = (360 - result) % 360
        } else {
            // back-facing
            result = (info.orientation - degrees + 360) % 360
        }

        Log.i(TAG, "curDegree: " + result)

        camera!!.setDisplayOrientation(result)
    }

    // 点击回退键
    override fun onClickLeftButton() {
        mTimer.cancel()
        if (mCamera != null) {
            mCamera!!.stopPreview()
            mCamera!!.release()
            mCamera = null
        }
        finish()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onClickLeftButton()
            return true //return true;拦截事件传递,从而屏蔽back键。
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onDestroy() {
        Log.i(TAG, "activity destory!")

        if (isStartPushVideo) {
            isStartPushVideo = false
            sendMessage("hangup")
            StopPublish()
            libPlayer.SmartPlayerStopPlay(playerHandle)
            libPlayer.SmartPlayerClose(playerHandle)
            playerHandle = 0
            Log.i(TAG, "onDestroy StopPublish")
        }

        if (isPushing || isRecording) {
            if (audioRecord_ != null) {
                Log.i(TAG, "surfaceDestroyed, call StopRecording..")
                audioRecord_!!.StopRecording()
                audioRecord_ = null
            }

            stopPush()
            stopRecorder()

            isPushing = false
            isRecording = false
        }

        super.onDestroy()
    }

    //获取地址
    private fun getPublishURL() {
        OkHttpUtils
                .postString()
                .url(JSConfig.GetVideoUrl + "/device/videoAuth/genClientAuthCode")
                .content("{\"userId\":\"" + JSConfig.userId + "\",\"centerId\":\"0b589cca-33e4-11e8-94c3-00163e0a279b\"}")
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .addHeader("Authorization", JSConfig.token)
                .addHeader("Accept", "application/json ")
                .addHeader("Content-Type", "application/json")
                .build()
                .execute(GetURLCallback())
    }

    inner class GetURLCallback : JSStringCallback() {
        override fun onResponse(response: String?, id: Int) {
            val gson = Gson()
            val jsonType = object : TypeToken<ApiResult<ClientUrl>>() {}.type
            var result = gson.fromJson<ApiResult<ClientUrl>>(response, jsonType)
            if (result.retCode=="0000"){
                publishURL = result.data!!.videoUrl
                PushVideo()
                GetCall(result.data!!.audioUrl)
                sendMessage("calling")
                mTimerTask.cancel()
                mTimerTask = object : TimerTask() {
                    override fun run() {
                        keepAlive()
                    }
                }
                mTimer.scheduleAtFixedRate(mTimerTask, 0, 60000)
            }

        }
    }

    //发送推送通知
    fun sendMessage(op: String) {
        var msg = """{"id":"${JSConfig.userId}","url":"$publishURL","op":"$op"}"""
        object : Thread() {
            override fun run() {
                var conx = ConnectionFactory()
                conx.host = "114.115.129.48"
                conx.port - 5672
                conx.username = "video_push"
                conx.password = "trosent@2018151951420"
                conx.virtualHost = "/video_push"
                val connection = conx.newConnection()
                val channel = connection.createChannel()
                val declareOk = channel?.exchangeDeclare("video.push.direct", "direct", false)
                channel.basicPublish("video.push.direct", "video.push", null, msg.toByteArray())
                println("published")
                connection?.close()
            }
        }.start()

    }

    //实时通话
    fun GetCall(url: String) {
        playerHandle = libPlayer.SmartPlayerOpen(this.applicationContext)
        libPlayer.SetSmartPlayerEventCallbackV2(playerHandle,
                EventHandePlayerV2())

        libPlayer.SmartPlayerSetSurface(playerHandle,  NTRenderer.CreateRenderer(this, false))

        libPlayer.SmartPlayerSetAudioOutputType(playerHandle, 0)

        libPlayer.SmartPlayerSetBuffer(playerHandle, 200) //设置缓存区

        libPlayer.SmartPlayerSetFastStartup(playerHandle, 1) //是否秒开
        libPlayer.SmartPlayerSetMute(playerHandle, 0)  //是否静音

        libPlayer.SmartPlayerSetUrl(playerHandle, url)
        libPlayer.SmartPlayerStartPlay(playerHandle)

    }

    //保持推送链接正常
    fun keepAlive() {
        OkHttpUtils
                .postString()
                .url(JSConfig.GetVideoUrl + "/device/videoAuth/keepAliveClient")
                .content("{}")
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .addHeader("Authorization", JSConfig.token)
                .addHeader("Accept", "application/json ")
                .addHeader("Content-Type", "application/json")
                .build()
                .execute(KeepAliveCallback())
    }

    inner class KeepAliveCallback : JSStringCallback() {
        override fun onResponse(response: String?, id: Int) {
        }
    }

    override fun initData() {
        mTimerTask = object : TimerTask() {
            override fun run() {
                keepAlive()
            }
        }
        initTimerTaskGetTime()

    }

    private fun initTimerTaskGetTime() {
        mTimerTaskGetTime = object : TimerTask() {
            override fun run() {
                pushTime = pushTime + 1

                var msg = Message()
                msg.what = 123
                mhandler.sendMessage(msg) //发生监听事件
            }
        }
    }

    //创建监听类
    inner class Myhandler : Handler() {
        override fun handleMessage(msg: Message) {
            var h = if (pushTime / 3600 > 9) (pushTime / 3600).toString() else {
                "0" + (pushTime / 3600).toString()
            }
            var m = if (pushTime % 3600 / 60 > 9) (pushTime % 3600 / 60).toString() else {
                "0" + (pushTime % 3600 / 60).toString()
            }
            var s = if (pushTime % 3600 % 60 > 9) (pushTime % 3600 % 60).toString() else {
                "0" + (pushTime % 3600 % 60).toString()
            }
            btnStartPush.text = ("$h:$m:$s")

        }
    }

    override fun initTitle(): String {
        return "单兵执法"
    }

    override fun initContentView(): Int {
        return R.layout.activity_camera
    }

    companion object {
        private val TAG = "SmartPublisher"
        private val FRONT = 1        //前置摄像头标记
        private val BACK = 2        //后置摄像头标记
        private val PORTRAIT = 1    //竖屏
        private val LANDSCAPE = 2    //横屏
        var count = 0

        init {
            System.loadLibrary("SmartPublisher")
            System.loadLibrary("SmartPlayer")
        }

        /**
         * 根据目录创建文件夹
         * @param context
         * @param cacheDir
         * @return
         */
        fun getOwnCacheDirectory(context: Context?, cacheDir: String): File {
            var appCacheDir: File? = null
            //判断sd卡正常挂载并且拥有权限的时候创建文件
            if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState() && hasExternalStoragePermission(context)) {
                appCacheDir = File(Environment.getExternalStorageDirectory(), cacheDir)
                Log.i(TAG, "appCacheDir: " + appCacheDir)
            }
            if (appCacheDir == null || !appCacheDir.exists() && !appCacheDir.mkdirs()) {
                appCacheDir = context!!.cacheDir
            }
            return appCacheDir!!
        }

        /**
         * 检查是否有权限
         * @param context
         * @return
         */
        private fun hasExternalStoragePermission(context: Context?): Boolean {
            val perm = context!!.checkCallingOrSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE")
            return perm == 0
        }
    }

}
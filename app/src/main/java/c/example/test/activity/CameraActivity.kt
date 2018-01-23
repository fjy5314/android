package c.example.test.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.graphics.ImageFormat
import android.graphics.PixelFormat
import android.hardware.Camera
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.*
import android.view.View.OnClickListener
import android.widget.*
import  c.example.test.R
import com.daniulive.smartpublisher.SmartPublisherJni
import com.daniulive.smartpublisher.SmartPublisherJni.WATERMARK
import com.eventhandle.SmartEventCallback
import com.voiceengine.NTAudioRecord
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by flny on 2018/1/15.
 */
class CameraActivity : BaseActivity(), SurfaceHolder.Callback, Camera.PreviewCallback {
    internal var audioRecord_: NTAudioRecord? = null    //for audio capture

    private var textCurURL: TextView? = null

    private lateinit var libPublisher: SmartPublisherJni

    /* 推送类型选择
	 * 0: 音视频
	 * 1: 纯音频
	 * 2: 纯视频
	 * */
    private var pushTypeSelector: Spinner? = null
    private var pushType = 0

    /* 水印类型选择
	 * 0: 图片水印
	 * 1: 全部水印
	 * 2: 文字水印
	 * 3: 不加水印
	 * */
    private var watermarkSelctor: Spinner? = null
    private var watemarkType = 0

    /* 推流分辨率选择
	 * 0: 640*480
	 * 1: 320*240
	 * 2: 176*144
	 * 3: 1280*720
	 * */
    private var resolutionSelector: Spinner? = null

    /* video软编码profile设置
     * 1: baseline profile
     * 2: main profile
     * 3: high profile
	 * */
    private var swVideoEncoderProfileSelector: Spinner? = null

    private var sw_video_encoder_profile = 1    //default with baseline profile

    private var recorderSelector: Spinner? = null

    private var btnRecoderMgr: Button? = null
    private var btnNoiseSuppression: Button? = null
    private var btnAGC: Button? = null
    private var btnSpeex: Button? = null
    private var btnMute: Button? = null
    private var btnMirror: Button? = null

    private var swVideoEncoderSpeedSelector: Spinner? = null

    private var btnHWencoder: Button? = null
    private var imgSwitchCamera: ImageView? = null
    private var btnInputPushUrl: Button? = null
    private var btnStartStop: Button? = null

    private var btnStartPush: Button? = null
    private var btnStartRecorder: Button? = null
    private var btnCaptureImage: Button? = null

    private lateinit var mSurfaceView: SurfaceView
    private lateinit var mSurfaceHolder: SurfaceHolder

    private var mCamera: Camera? = null
    private var myAutoFocusCallback: Camera.AutoFocusCallback? = null

    private var mPreviewRunning = false

    private var isStart = false

    private var isPushing = false
    private var isRecording = false

    private val logoPath = "/sdcard/daniulivelogo.png"
    private var isWritelogoFileSuccess = false

    private var publishURL: String? = null
    private val baseURL = "rtmp://114.115.129.48:1935/hls/kima"
    private var inputPushURL: String? = ""

    private var printText = "URL:"
    private var txt = "当前状态"
    private var currentCameraType = BACK    //当前打开的摄像头标记
    private var currentOrigentation = PORTRAIT
    private var curCameraIndex = -1

    private var videoWidth = 640
    private var videoHight = 480

    private var frameCount = 0

    private val recDir = "/sdcard/daniulive/rec"    //for recorder path

    private var is_need_local_recorder = false        // do not enable recorder in default

    private var is_noise_suppression = true

    private var is_agc = false

    private var is_speex = false

    private var is_mute = false

    private var is_mirror = false

    private var sw_video_encoder_speed = 6

    private var is_hardware_encoder = false

    private var myContext: Context? = null

    private var imageSavePath: String? = null


    @Throws(IOException::class)
    private fun ReadAssetFileDataToByte(ins: InputStream): ByteArray? {
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
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);    //屏幕常亮
        myContext = this.applicationContext
        libPublisher = SmartPublisherJni()
        btnStartPush = findViewById<View>(R.id.button_start_push) as Button
        btnStartPush!!.setOnClickListener(ButtonStartPushListener())
        mSurfaceView = findViewById<View>(R.id.surface) as SurfaceView
        mSurfaceHolder = mSurfaceView.holder
        mSurfaceHolder.addCallback(this)
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)

        myAutoFocusCallback = Camera.AutoFocusCallback { success, camera ->
            if (success)
            //success表示对焦成功
            {
                Log.i("直播测试", "onAutoFocus succeed...")
            } else {
                Log.i("直播测试", "onAutoFocus failed...")
            }
        }
        ButtonStartListener()
        ButtonStartPushListener()
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

    internal fun SwitchResolution(position: Int) {
        Log.i(TAG, "Current Resolution position: " + position)

        when (position) {
            0 -> {
                videoWidth = 640
                videoHight = 480
            }
            1 -> {
                videoWidth = 320
                videoHight = 240
            }
            2 -> {
                videoWidth = 176
                videoHight = 144
            }
            3 -> {
                videoWidth = 1280
                videoHight = 720
            }
            else -> {
                videoWidth = 640
                videoHight = 480
            }
        }

        mCamera!!.stopPreview()
        initCamera(mSurfaceHolder)
    }

    internal fun CheckInitAudioRecorder() {
        if (audioRecord_ == null) {
            audioRecord_ = NTAudioRecord(this, 1)
        }

        if (audioRecord_ != null) {
            Log.i(TAG, "onCreate, call executeAudioRecordMethod..")
            // auido_ret: 0 ok, other failed
            val auido_ret = audioRecord_!!.executeAudioRecordMethod()
            Log.i(TAG, "onCreate, call executeAudioRecordMethod.. auido_ret=" + auido_ret)
        }
    }

    //Configure recorder related function.
    internal fun ConfigRecorderFuntion(isNeedLocalRecorder: Boolean) {
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
//            intent.setClass(this@CameraPublishActivity, RecorderManager::class.java)
//            intent.putExtra("RecoderDir", recDir)
//            startActivity(intent)
        }
    }

    internal inner class ButtonNoiseSuppressionListener : OnClickListener {
        override fun onClick(v: View) {
            is_noise_suppression = !is_noise_suppression

            if (is_noise_suppression)
                btnNoiseSuppression!!.text = "停用噪音抑制"
            else
                btnNoiseSuppression!!.text = "启用噪音抑制"
        }
    }

    internal inner class ButtonAGCListener : OnClickListener {
        override fun onClick(v: View) {
            is_agc = !is_agc

            if (is_agc)
                btnAGC!!.text = "停用AGC"
            else
                btnAGC!!.text = "启用AGC"
        }
    }

    internal inner class ButtonSpeexListener : OnClickListener {
        override fun onClick(v: View) {
            is_speex = !is_speex

            if (is_speex)
                btnSpeex!!.text = "不使用Speex"
            else
                btnSpeex!!.text = "使用Speex"
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

    internal inner class ButtonMirrorListener : OnClickListener {
        override fun onClick(v: View) {
            is_mirror = !is_mirror

            if (is_mirror)
                btnMirror!!.text = "关镜像"
            else
                btnMirror!!.text = "开镜像"

            if (libPublisher != null)
                libPublisher!!.SmartPublisherSetMirror(if (is_mirror) 1 else 0)
        }
    }

    internal inner class ButtonHardwareEncoderListener : OnClickListener {
        override fun onClick(v: View) {
            is_hardware_encoder = !is_hardware_encoder

            if (is_hardware_encoder)
                btnHWencoder!!.text = "当前硬解码"
            else
                btnHWencoder!!.text = "当前软解码"
        }
    }

    internal inner class EventHande : SmartEventCallback {
        override fun onCallback(code: Int, param1: Long, param2: Long, param3: String, param4: String, param5: Any) {
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

    private fun SaveInputUrl(url: String?) {
        inputPushURL = ""

        if (url == null)
            return

        // rtmp://
        if (url.length < 8) {
            Log.e(TAG, "Input publish url error:" + url)
            return
        }

        if (!url.startsWith("rtmp://")) {
            Log.e(TAG, "Input publish url error:" + url)
            return
        }

        inputPushURL = url

        Log.i(TAG, "Input publish url:" + url)
    }


    internal inner class ButtonStartListener : OnClickListener {
        override fun onClick(v: View) {
            if (isPushing || isRecording) {
                return
            }

            if (isStart) {
                stop()
                btnRecoderMgr!!.isEnabled = true
                btnHWencoder!!.isEnabled = true

                btnNoiseSuppression!!.isEnabled = true
                btnAGC!!.isEnabled = true
                btnSpeex!!.isEnabled = true

                return
            }

            isStart = true
            btnStartStop!!.text = " 停止推流 "
            Log.i(TAG, "onClick start..")

            if (libPublisher != null) {
                if (inputPushURL != null && inputPushURL!!.length > 1) {
                    publishURL = inputPushURL
                    Log.i(TAG, "start, input publish url:" + publishURL!!)
                } else {
                    publishURL = baseURL + (System.currentTimeMillis() % 1000000).toInt().toString()
                    Log.i(TAG, "start, generate random url:" + publishURL!!)

                }

                printText = "URL:" + publishURL!!

                Log.i(TAG, printText)

//                textCurURL = findViewById(R.id.txtCurURL) as TextView
//                textCurURL!!.text = printText

                ConfigRecorderFuntion(is_need_local_recorder)

                Log.i(TAG, "videoWidth: $videoWidth videoHight: $videoHight pushType:$pushType")

                var audio_opt = 1
                var video_opt = 1

                if (pushType == 1) {
                    video_opt = 0
                } else if (pushType == 2) {
                    audio_opt = 0
                }

                libPublisher.SmartPublisherInit(myContext as Any, audio_opt, video_opt, videoWidth, videoHight)

                if (is_hardware_encoder) {
                    val hwHWKbps = setHardwareEncoderKbps(videoWidth, videoHight)

                    Log.i(TAG, "hwHWKbps: " + hwHWKbps)

                    val isSupportHWEncoder = libPublisher!!.SetSmartPublisherVideoHWEncoder(hwHWKbps)

                    if (isSupportHWEncoder == 0) {
                        Log.i(TAG, "Great, it supports hardware encoder!")
                    }
                }

                libPublisher!!.SetSmartPublisherEventCallback(EventHande())

                //如果想和时间显示在同一行，请去掉'\n'
                val watermarkText = "大牛直播(daniulive)\n\n"

                val path = logoPath

                if (watemarkType == 0) {
                    if (isWritelogoFileSuccess)
                        libPublisher!!.SmartPublisherSetPictureWatermark(path, WATERMARK.WATERMARK_POSITION_TOPRIGHT, 160, 160, 10, 10)
                } else if (watemarkType == 1) {
                    if (isWritelogoFileSuccess)
                        libPublisher!!.SmartPublisherSetPictureWatermark(path, WATERMARK.WATERMARK_POSITION_TOPRIGHT, 160, 160, 10, 10)

                    libPublisher!!.SmartPublisherSetTextWatermark(watermarkText, 1, WATERMARK.WATERMARK_FONTSIZE_BIG, WATERMARK.WATERMARK_POSITION_BOTTOMRIGHT, 10, 10)

                    //libPublisher.SmartPublisherSetTextWatermarkFontFileName("/system/fonts/DroidSansFallback.ttf");

                    //libPublisher.SmartPublisherSetTextWatermarkFontFileName("/sdcard/DroidSansFallback.ttf");
                } else if (watemarkType == 2) {
                    libPublisher!!.SmartPublisherSetTextWatermark(watermarkText, 1, WATERMARK.WATERMARK_FONTSIZE_BIG, WATERMARK.WATERMARK_POSITION_BOTTOMRIGHT, 10, 10)

                    //libPublisher.SmartPublisherSetTextWatermarkFontFileName("/system/fonts/DroidSansFallback.ttf");
                } else {
                    Log.i(TAG, "no watermark settings..")
                }
                //end


                if (!is_speex) {
                    // set AAC encoder
                    libPublisher!!.SmartPublisherSetAudioCodecType(1)
                } else {
                    // set Speex encoder
                    libPublisher!!.SmartPublisherSetAudioCodecType(2)
                    libPublisher!!.SmartPublisherSetSpeexEncoderQuality(8)
                }

                libPublisher!!.SmartPublisherSetNoiseSuppression(if (is_noise_suppression) 1 else 0)

                libPublisher!!.SmartPublisherSetAGC(if (is_agc) 1 else 0)

                //libPublisher.SmartPublisherSetClippingMode(0);

                libPublisher!!.SmartPublisherSetSWVideoEncoderProfile(sw_video_encoder_profile)

                libPublisher!!.SmartPublisherSetSWVideoEncoderSpeed(sw_video_encoder_speed)

                libPublisher!!.SmartPublisherSaveImageFlag(1)

                //libPublisher.SetRtmpPublishingType(0);


                //libPublisher.SmartPublisherSetGopInterval(40);

                //libPublisher.SmartPublisherSetFPS(15);

                //libPublisher.SmartPublisherSetSWVideoBitRate(600, 1200);
                // IF not set url or url is empty, it will not publish stream
                // if ( libPublisher.SmartPublisherSetURL("") != 0 )
//                if (libPublisher!!.SmartPublisherSetURL(publishURL) != 0) {
//                    Log.e(TAG, "Failed to set publish stream URL..")
//                }

                val isStarted = libPublisher!!.SmartPublisherStart()
                if (isStarted != 0) {
                    Log.e(TAG, "Failed to publish stream..")
                } else {
                    btnRecoderMgr!!.isEnabled = false
                    btnHWencoder!!.isEnabled = false

                    btnNoiseSuppression!!.isEnabled = false
                    btnAGC!!.isEnabled = false
                    btnSpeex!!.isEnabled = false
                }
            }

            if (pushType == 0 || pushType == 1) {
                CheckInitAudioRecorder()    //enable pure video publisher..
            }
        }
    }

    internal inner class ButtonStopListener : OnClickListener {
        override fun onClick(v: View) {
            //onDestroy();
        }
    }


    private fun ConfigControlEnable(isEnable: Boolean) {
        btnRecoderMgr!!.isEnabled = isEnable
        btnHWencoder!!.isEnabled = isEnable

        btnNoiseSuppression!!.isEnabled = isEnable
        btnAGC!!.isEnabled = isEnable
        btnSpeex!!.isEnabled = isEnable
    }

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

        libPublisher!!.SmartPublisherInit(myContext as Any, audio_opt, video_opt,
                videoWidth, videoHight)

        if (is_hardware_encoder) {
            val hwHWKbps = setHardwareEncoderKbps(videoWidth, videoHight)

            Log.i(TAG, "hwHWKbps: " + hwHWKbps)

            val isSupportHWEncoder = libPublisher!!
                    .SetSmartPublisherVideoHWEncoder(hwHWKbps)

            if (isSupportHWEncoder == 0) {
                Log.i(TAG, "Great, it supports hardware encoder!")
            }
        }

        libPublisher!!.SetSmartPublisherEventCallback(EventHande())

        // 如果想和时间显示在同一行，请去掉'\n'
        val watermarkText = "大牛直播(daniulive)\n\n"

        val path = logoPath

        if (watemarkType == 0) {
            if (isWritelogoFileSuccess)
                libPublisher!!.SmartPublisherSetPictureWatermark(path,
                        WATERMARK.WATERMARK_POSITION_TOPRIGHT, 160,
                        160, 10, 10)

        } else if (watemarkType == 1) {
            if (isWritelogoFileSuccess)
                libPublisher!!.SmartPublisherSetPictureWatermark(path,
                        WATERMARK.WATERMARK_POSITION_TOPRIGHT, 160,
                        160, 10, 10)

            libPublisher!!.SmartPublisherSetTextWatermark(watermarkText, 1,
                    WATERMARK.WATERMARK_FONTSIZE_BIG,
                    WATERMARK.WATERMARK_POSITION_BOTTOMRIGHT, 10, 10)

            // libPublisher.SmartPublisherSetTextWatermarkFontFileName("/system/fonts/DroidSansFallback.ttf");

            // libPublisher.SmartPublisherSetTextWatermarkFontFileName("/sdcard/DroidSansFallback.ttf");
        } else if (watemarkType == 2) {
            libPublisher!!.SmartPublisherSetTextWatermark(watermarkText, 1,
                    WATERMARK.WATERMARK_FONTSIZE_BIG,
                    WATERMARK.WATERMARK_POSITION_BOTTOMRIGHT, 10, 10)

            // libPublisher.SmartPublisherSetTextWatermarkFontFileName("/system/fonts/DroidSansFallback.ttf");
        } else {
            Log.i(TAG, "no watermark settings..")
        }
        // end

        if (!is_speex) {
            // set AAC encoder
            libPublisher!!.SmartPublisherSetAudioCodecType(1)
        } else {
            // set Speex encoder
            libPublisher!!.SmartPublisherSetAudioCodecType(2)
            libPublisher!!.SmartPublisherSetSpeexEncoderQuality(8)
        }

        libPublisher!!.SmartPublisherSetNoiseSuppression(if (is_noise_suppression)
            1
        else
            0)

        libPublisher!!.SmartPublisherSetAGC(if (is_agc) 1 else 0)

        // libPublisher.SmartPublisherSetClippingMode(0);

        libPublisher!!.SmartPublisherSetSWVideoEncoderProfile(sw_video_encoder_profile)

        libPublisher!!.SmartPublisherSetSWVideoEncoderSpeed(sw_video_encoder_speed)

        // libPublisher.SetRtmpPublishingType(0);

        // libPublisher.SmartPublisherSetGopInterval(40);

        // libPublisher.SmartPublisherSetFPS(15);

        // libPublisher.SmartPublisherSetSWVideoBitRate(600, 1200);

        libPublisher!!.SmartPublisherSaveImageFlag(1)
    }


    internal inner class ButtonStartPushListener : OnClickListener {
        override fun onClick(v: View) {
            if (isStart) {
                return
            }

            if (isPushing) {
                stopPush()

                if (!isRecording) {
                    ConfigControlEnable(true)
                }

                btnStartPush!!.text = " 推送"

                isPushing = false

                return
            }


            Log.i(TAG, "onClick start push..")

            if (libPublisher == null)
                return

            isPushing = true

            if (!isRecording) {
                InitAndSetConfig()
            }

            if (inputPushURL != null && inputPushURL!!.length > 1) {
                publishURL = inputPushURL
                Log.i(TAG, "start, input publish url:" + publishURL!!)
            } else {
                publishURL = baseURL + (System.currentTimeMillis() % 1000000).toInt().toString()
                Log.i(TAG, "start, generate random url:" + publishURL!!)
            }

            printText = "URL:" + publishURL!!

            Log.i(TAG, printText)

//            if (libPublisher!!.SmartPublisherSetURL(publishURL) != 0) {
//                Log.e(TAG, "Failed to set publish stream URL..")
//            }

            val startRet = libPublisher!!.SmartPublisherStartPublisher()
            if (startRet != 0) {
                isPushing = false

                Log.e(TAG, "Failed to start push stream..")
                return
            }

            if (!isRecording) {
                if (pushType == 0 || pushType == 1) {
                    CheckInitAudioRecorder()    //enable pure video publisher..
                }
            }

            if (!isRecording) {
                ConfigControlEnable(false)
            }

//            textCurURL = findViewById(R.id.txtCurURL) as TextView
//            textCurURL!!.text = printText
//
//            btnStartPush!!.text = " 停止推送 "
        }

    }

    internal inner class ButtonStartRecorderListener : View.OnClickListener {
        override fun onClick(v: View) {
            if (isStart) {
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

            ConfigRecorderFuntion(true)

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

    internal inner class ButtonCaptureImageListener : View.OnClickListener {
        @SuppressLint("SimpleDateFormat")
        override fun onClick(v: View) {
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            val imageFileName = "dn_" + timeStamp    //创建以时间命名的文件名称

            val imagePath = "$imageSavePath/$imageFileName.png"

            Log.i(TAG, "imagePath:" + imagePath)

            libPublisher!!.SmartPublisherSaveCurImage(imagePath)
        }
    }

    private fun stop() {
        Log.i(TAG, "onClick stop..")
        StopPublish()
        isStart = false
        btnStartStop!!.text = " 开始推流 "
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

    override fun onDestroy() {
        Log.i(TAG, "activity destory!")

        if (isStart) {
            isStart = false
            StopPublish()
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
        finish()
        System.exit(0)
    }

    private fun SetCameraFPS(parameters: Camera.Parameters?) {
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
    private fun initCamera(holder: SurfaceHolder) {
        Log.i(TAG, "initCamera..")

        if (mPreviewRunning)
            mCamera!!.stopPreview()

        val parameters: Camera.Parameters
//        try {
        parameters = mCamera!!.parameters
//        } catch (e: Exception) {
//            // TODO Auto-generated catch block
//            e.printStackTrace()
//            return
//        }
        parameters.setPreviewSize(videoWidth, videoHight)
        parameters.pictureFormat = PixelFormat.JPEG
        parameters.previewFormat = PixelFormat.YCbCr_420_SP

        SetCameraFPS(parameters)

        setCameraDisplayOrientation(this, curCameraIndex, mCamera)

        mCamera!!.parameters = parameters

        val bufferSize = ((videoWidth or 0xf) + 1) * videoHight * ImageFormat.getBitsPerPixel(parameters.previewFormat) / 8

        mCamera!!.addCallbackBuffer(ByteArray(bufferSize))

        mCamera!!.setPreviewCallbackWithBuffer(this)
        try {
            mCamera!!.setPreviewDisplay(holder)
        } catch (ex: Exception) {
            // TODO Auto-generated catch block
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
        // TODO Auto-generated method stub
        Log.i(TAG, "Surface Destroyed")
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        try {
            super.onConfigurationChanged(newConfig)
            Log.i(TAG, "onConfigurationChanged, start:" + isStart)
            if (this.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                if (!isStart && !isPushing && !isRecording) {
                    currentOrigentation = LANDSCAPE
                }
            } else if (this.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                if (!isStart && !isPushing && !isRecording) {
                    currentOrigentation = PORTRAIT
                }
            }
        } catch (ex: Exception) {
        }

    }

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
            if (isStart || isPushing || isRecording) {
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

        val info = Camera.CameraInfo()
        for (cameraIndex in 0 until cameraCount) {
            Camera.getCameraInfo(cameraIndex, info)

            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                frontIndex = cameraIndex
            } else if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
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
        return Camera.open(backIndex)
    }

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

    private fun setHardwareEncoderKbps(width: Int, height: Int): Int {
        var hwEncoderKpbs = 0

        when (width) {
            176 -> hwEncoderKpbs = 300
            320 -> hwEncoderKpbs = 500
            640 -> hwEncoderKpbs = 1000
            1280 -> hwEncoderKpbs = 1700
            else -> hwEncoderKpbs = 1000
        }

        return hwEncoderKpbs
    }


    companion object {
        private val TAG = "直播测试"

        private val FRONT = 1        //前置摄像头标记
        private val BACK = 2        //后置摄像头标记
        private val PORTRAIT = 1    //竖屏
        private val LANDSCAPE = 2    //横屏

        init {
            System.loadLibrary("SmartPublisher")
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

    override fun initTitle(): String {
        return "单兵执法"
    }

    override fun initContentView(): Int {
        return R.layout.activity_camera
    }
}
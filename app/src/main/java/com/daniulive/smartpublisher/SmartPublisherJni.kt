package com.daniulive.smartpublisher

/**
 * Created by flny on 2018/1/22.
 */

import java.nio.ByteBuffer
import com.eventhandle.SmartEventCallback

class SmartPublisherJni {

    object WATERMARK {
        val WATERMARK_FONTSIZE_MEDIUM = 0
        val WATERMARK_FONTSIZE_SMALL = 1
        val WATERMARK_FONTSIZE_BIG = 2

        val WATERMARK_POSITION_TOPLEFT = 0
        val WATERMARK_POSITION_TOPRIGHT = 1
        val WATERMARK_POSITION_BOTTOMLEFT = 2
        val WATERMARK_POSITION_BOTTOMRIGHT = 3
    }

    /**
     * Initialized publisher.
     *
     * @param ctx: get by this.getApplicationContext()
     *
     * @param audio_opt: if with 0: it does not publish audio; if with 1, it publish audio; if with 2, it publish external encoded audio, only support aac.
     *
     * @param video_opt: if with 0: it does not publish video; if with 1, it publish video; if with 2, it publish external encoded video, only support h264, data:0000000167....
     *
     * @param width: capture width; height: capture height.
     *
     * <pre>This function must be called firstly.</pre>
     *
     * @return {0} if successful
     */
    external fun SmartPublisherInit(ctx: Any, audio_opt: Int, video_opt: Int, width: Int, height: Int): Int

    /**
     * Set callback event
     *
     * @param callback function
     *
     * @return {0} if successful
     */
    external fun SetSmartPublisherEventCallback(callback: SmartEventCallback): Int

    /**
     * Set Video HW Encoder, if support HW encoder, it will return 0
     *
     * @param kbps: the kbps of different resolution(25 fps).
     *
     * @return {0} if successful
     */
    external fun SetSmartPublisherVideoHWEncoder(kbps: Int): Int

    /**
     * Set Text water-mark
     *
     * @param fontSize: it should be "MEDIUM", "SMALL", "BIG"
     *
     * @param waterPostion: it should be "TOPLEFT", "TOPRIGHT", "BOTTOMLEFT", "BOTTOMRIGHT".
     *
     * @param xPading, yPading: the distance of the original picture.
     *
     * <pre> The interface is only used for setting font water-mark when publishing stream. </pre>
     *
     * @return {0} if successful
     */
    external fun SmartPublisherSetTextWatermark(waterText: String, isAppendTime: Int, fontSize: Int, waterPostion: Int, xPading: Int, yPading: Int): Int


    /**
     * Set Text water-mark font file name
     * @param fontFileName:  font full file name,  e.g: /system/fonts/DroidSansFallback.ttf
     *
     * @return {0} if successful
     */
    external fun SmartPublisherSetTextWatermarkFontFileName(fontFileName: String): Int

    /**
     * Set picture water-mark
     *
     * @param picPath: the picture working path, e.g: /sdcard/logo.png
     *
     * @param waterPostion: it should be "TOPLEFT", "TOPRIGHT", "BOTTOMLEFT", "BOTTOMRIGHT".
     *
     * @param picWidth, picHeight: picture width & height
     *
     * @param xPading, yPading: the distance of the original picture.
     *
     * <pre> The interface is only used for setting picture(logo) water-mark when publishing stream, with "*.png" format </pre>
     *
     * @return {0} if successful
     */
    external fun SmartPublisherSetPictureWatermark(picPath: String, waterPostion: Int, picWidth: Int, picHeight: Int, xPading: Int, yPading: Int): Int

    /**
     * Set gop interval.
     *
     * <pre>please set before SmartPublisherStart while after SmartPublisherInit.</pre>
     *
     * gopInterval: encode I frame interval, the value always > 0
     *
     * @return {0} if successful
     */
    external fun SmartPublisherSetGopInterval(gopInterval: Int): Int

    /**
     * Set software encode video bit-rate.
     *
     * <pre>please set before SmartPublisherStart while after SmartPublisherInit.</pre>
     *
     * avgBitRate: average encode bit-rate(kbps)
     *
     * maxBitRate: max encode bit-rate(kbps)
     *
     * @return {0} if successful
     */
    external fun SmartPublisherSetSWVideoBitRate(avgBitRate: Int, maxBitRate: Int): Int

    /**
     * Set fps.
     *
     * <pre>please set before SmartPublisherStart while after SmartPublisherInit.</pre>
     *
     * fps: the fps of video, range with (1,25).
     *
     * @return {0} if successful
     */
    external fun SmartPublisherSetFPS(fps: Int): Int

    /**
     * Set software video encoder profile.
     *
     * <pre>please set before SmartPublisherStart while after SmartPublisherInit.</pre>
     *
     * profile: the software video encoder profile, range with (1,3).
     *
     * 1: baseline profile
     * 2: main profile
     * 3: high profile
     *
     * @return {0} if successful
     */
    external fun SmartPublisherSetSWVideoEncoderProfile(profile: Int): Int


    /**
     *
     * Set software video encoder speed.
     *
     * <pre>please set before SmartPublisherStart while after SmartPublisherInit.</pre>
     *
     * @param speed: range with(1, 6), the default speed is 6.
     *
     * if with 1, CPU is lowest.
     * if with 6, CPU is highest.
     *
     * @return {0} if successful
     */
    external fun SmartPublisherSetSWVideoEncoderSpeed(speed: Int): Int

    /**
     * Set Clipping Mode: 设置裁剪模式(仅用于640*480分辨率, 裁剪主要用于移动端宽高适配)
     *
     * <pre>please set before SmartPublisherStart while after SmartPublisherInit.</pre>
     *
     * @param mode: 0: 非裁剪模式 1:裁剪模式(如不设置, 默认裁剪模式)
     *
     * @return {0} if successful
     */
    external fun SmartPublisherSetClippingMode(mode: Int): Int

    /**
     * Set audio encoder type
     *
     * @param type: if with 1:AAC, if with 2: SPEEX
     *
     * @return {0} if successful
     */
    external fun SmartPublisherSetAudioCodecType(type: Int): Int

    /**
     * Set speex encoder quality
     *
     * @param quality: range with (0, 10), default value is 8
     *
     * @return {0} if successful
     */
    external fun SmartPublisherSetSpeexEncoderQuality(quality: Int): Int


    /**
     * Set Audio Noise Suppression
     *
     * @param isNS: if with 1:suppress, if with 0: does not suppress
     *
     * @return {0} if successful
     */
    external fun SmartPublisherSetNoiseSuppression(isNS: Int): Int


    /**
     * Set Audio AGC
     *
     * @param isNS: if with 1:AGC, if with 0: does not AGC
     *
     * @return {0} if successful
     */
    external fun SmartPublisherSetAGC(isAGC: Int): Int


    /**
     * Set Audio Echo Cancellation
     *
     * @param isCancel: if with 1:Echo Cancellation, if with 0: does not cancel
     * @param delay: echo delay(ms), if with 0, SDK will automatically estimate the delay.
     *
     * @return {0} if successful
     */
    external fun SmartPublisherSetEchoCancellation(isCancel: Int, delay: Int): Int


    /**
     * Set mute or not during publish stream
     *
     * @param isMute: if with 1:mute, if with 0: does not mute
     *
     * @return {0} if successful
     */
    external fun SmartPublisherSetMute(isMute: Int): Int

    /**
     * Set mirror
     *
     * @param isMirror: if with 1:mirror mode, if with 0: normal mode
     *
     * Please note when with "mirror mode", the publisher and player with the same echo direction
     *
     * @return {0} if successful
     */
    external fun SmartPublisherSetMirror(isMirror: Int): Int

    /**
     * Set if recorder the stream to local file.
     *
     * @param isRecorder: (0: do not recorder; 1: recorder)
     *
     * <pre> NOTE: If set isRecorder with 1: Please make sure before call SmartPublisherStartPublish(), set a valid path via SmartPublisherCreateFileDirectory(). </pre>
     *
     * @return {0} if successful
     */
    external fun SmartPublisherSetRecorder(isRecorder: Int): Int

    /**
     * Create file directory
     *
     * @param path,  E.g: /sdcard/daniulive/rec
     *
     * <pre> The interface is only used for recording the stream data to local side. </pre>
     *
     * @return {0} if successful
     */
    external fun SmartPublisherCreateFileDirectory(path: String): Int

    /**
     * Set recorder directory.
     *
     * @param path: the directory of recorder file.
     *
     * <pre> NOTE: make sure the path should be existed, or else the setting failed. </pre>
     *
     * @return {0} if successful
     */
    external fun SmartPublisherSetRecorderDirectory(path: String): Int

    /**
     * Set the size of every recorded file.
     *
     * @param size: (MB), (5M~500M), if not in this range, set default size with 200MB.
     *
     * @return {0} if successful
     */
    external fun SmartPublisherSetRecorderFileMaxSize(size: Int): Int

    /**
     * Set if needs to save image during publishing stream
     *
     * @param is_save_image: if with 1, it will save current image via the interface of SmartPlayerSaveImage(), if with 0: does not it
     *
     * @return {0} if successful
     */
    external fun SmartPublisherSaveImageFlag(is_save_image: Int): Int

    /**
     * Save current image during publishing stream
     *
     * @param imageName: image name, which including fully path, "/sdcard/daniuliveimage/daniu.png", etc.
     *
     * @return {0} if successful
     */
    external fun SmartPublisherSaveCurImage(imageName: String): Int

    /**
     * Set rtmp PublishingType
     *
     * @param type: 0:live, 1:record. please refer to rtmp specification Page 46
     *
     * @return {0} if successful
     */
    external fun SetRtmpPublishingType(type: Int): Int

    /**
     * Set publish stream url.
     *
     * if not set url or url is empty, it will not publish stream
     *
     * @param url: publish url.
     *
     * @return {0} if successful
     */
    external fun SmartPublisherSetURL(url: String): Int

    /**
     * Start publish stream
     *
     * @return {0} if successful
     */
    external fun SmartPublisherStart(): Int

    /**
     * Set live video data(no encoded data).
     *
     * @param cameraType: CAMERA_FACING_BACK with 0, CAMERA_FACING_FRONT with 1
     *
     * @param curOrg: LANDSCAPE with 0, PORTRAIT 1
     *
     * @return {0} if successful
     */
    external fun SmartPublisherOnCaptureVideoData(data: ByteArray, len: Int, cameraType: Int, curOrg: Int): Int

    /**
     * Set live video data(no encoded data).
     *
     * @param data: I420 data
     *
     * @param len: I420 data length
     *
     * @param yStride: y stride
     *
     * @param uStride: u stride
     *
     * @param vStride: v stride
     *
     * @return {0} if successful
     */
    external fun SmartPublisherOnCaptureVideoI420Data(data: ByteArray, len: Int, yStride: Int, uStride: Int, vStride: Int): Int


    /**
     * Set live video data(no encoded data).
     *
     * @param data: RGBA data
     *
     * @param rowStride: stride information
     *
     * @param width: width
     *
     * @param height: height
     *
     * @return {0} if successful
     */
    external fun SmartPublisherOnCaptureVideoRGBAData(data: ByteBuffer, rowStride: Int, width: Int, height: Int): Int

    /**
     * Set live video data(no encoded data).
     *
     * @param data: ABGR flip vertical(垂直翻转) data
     *
     * @param rowStride: stride information
     *
     * @param width: width
     *
     * @param height: height
     *
     * @return {0} if successful
     */
    external fun SmartPublisherOnCaptureVideoABGRFlipVerticalData(data: ByteBuffer, rowStride: Int, width: Int, height: Int): Int


    /**
     * Set far end pcm data
     *
     * @param pcmdata : 16bit pcm data
     * @param sampleRate: audio sample rate
     * @param channel: auido channel
     * @param per_channel_sample_number: per channel sample numbers
     * @param is_low_latency: if with 0, it is not low_latency, if with 1, it is low_latency
     * @return {0} if successful
     */
    external fun SmartPublisherOnFarEndPCMData(pcmdata: ByteBuffer, sampleRate: Int, channel: Int, per_channel_sample_number: Int, is_low_latency: Int): Int


    /**
     * Set encoded video data.
     *
     * @param buffer: encoded video data
     *
     * @param len: data length
     *
     * @param isKeyFrame: if with key frame, please set 1, otherwise, set 0.
     *
     * @param timeStamp: video timestamp
     *
     * @return {0} if successful
     */
    external fun SmartPublisherOnReceivingVideoEncodedData(buffer: ByteArray, len: Int, isKeyFrame: Int, timeStamp: Long): Int


    /**
     * set audio specific configure.
     *
     * @param buffer: audio specific settings.
     *
     * For example:
     *
     * sample rate with 44100, channel: 2, profile: LC
     *
     * audioConfig set as below:
     *
     * byte[] audioConfig = new byte[2];
     * audioConfig[0] = 0x12;
     * audioConfig[1] = 0x10;
     *
     * @param len: buffer length
     *
     * @return {0} if successful
     */
    external fun SmartPublisherSetAudioSpecificConfig(buffer: ByteArray, len: Int): Int

    /**
     * Set encoded audio data.
     *
     * @param data: encoded audio data
     *
     * @param len: data length
     *
     * @param isKeyFrame: 1
     *
     * @param timeStamp: audio timestamp
     *
     * @return {0} if successful
     */
    external fun SmartPublisherOnReceivingAACData(buffer: ByteArray, len: Int, isKeyFrame: Int, timeStamp: Long): Int

    /**
     * Stop publish stream
     *
     * @return {0} if successful
     */
    external fun SmartPublisherStop(): Int


    /*********增加新的接口 ++  */
    /* 增加新接口是为了把推送和录像分离, 老的接口依然可用(SmartPublisherStart, SmartPublisherStop),
     * 但是不要老接口和新接口混着用，这样结果是未定义的
    */

    /**
     * Start publish stream
     *
     * @return {0} if successful
     */
    external fun SmartPublisherStartPublisher(): Int

    /**
     * Stop publish stream
     *
     * @return {0} if successful
     */
    external fun SmartPublisherStopPublisher(): Int

    /**
     * Start recorder
     *
     * @return {0} if successful
     */
    external fun SmartPublisherStartRecorder(): Int

    /**
     * Stop recorder
     *
     * @return {0} if successful
     */
    external fun SmartPublisherStopRecorder(): Int


    /*********增加新的接口  --  */
}
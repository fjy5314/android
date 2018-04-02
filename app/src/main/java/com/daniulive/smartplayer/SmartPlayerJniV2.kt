

package com.daniulive.smartplayer

import com.eventhandle.NTSmartEventCallbackV2

class SmartPlayerJniV2 {
    /**
     * Initialize Player.
     *
     * @param ctx: get by this.getApplicationContext()
     *
     * <pre>This function must be called firstly.</pre>
     *
     * @return player handle if successful, if return 0, which means init failed.
     */

    external fun SmartPlayerOpen(ctx: Any): Long

    /**
     * Set callbackv2 event
     *
     * @param callback function
     *
     * @return {0} if successful
     */
    external fun SetSmartPlayerEventCallbackV2(handle: Long, callbackv2: NTSmartEventCallbackV2): Int

    /**
     * Set Video HW decoder, if support HW decoder, it will return 0
     *
     * @param isHWDecoder: 0: software decoder; 1: hardware decoder.
     *
     * @return {0} if successful
     */
    external fun SetSmartPlayerVideoHWDecoder(handle: Long, isHWDecoder: Int): Int

    /**
     * Set Surface view.
     *
     * @param handle: return value from SmartPlayerOpen()
     *
     * @param glSurface: surface view
     *
     * <pre> NOTE: if not set or set surface with null, it will playback audio only. </pre>
     *
     * @return {0} if successful
     */
    external fun SmartPlayerSetSurface(handle: Long, surface: Any): Int


    /**
     * Set External Render.
     *
     * @param handle: return value from SmartPlayerOpen()
     *
     * @param external_render:  External Render
     *
     * @return {0} if successful
     */
    external fun SmartPlayerSetExternalRender(handle: Long, external_render: Any): Int

    /**
     * Set External Audio Output.
     *
     * @param handle: return value from SmartPlayerOpen()
     *
     * @param external_audio_output:  External Audio Output
     *
     * @return {0} if successful
     */
    external fun SmartPlayerSetExternalAudioOutput(handle: Long, external_audio_output: Any): Int

    /**
     * Set AudioOutput Type
     *
     * @param handle: return value from SmartPlayerOpen()
     *
     * @param use_audiotrack:
     *
     * <pre> NOTE: if use_audiotrack with 0: it will use auto-select output devices; if with 1: will use audiotrack mode. </pre>
     *
     * @return {0} if successful
     */
    external fun SmartPlayerSetAudioOutputType(handle: Long, use_audiotrack: Int): Int


    /**
     * Set buffer
     *
     * @param handle: return value from SmartPlayerOpen()
     *
     * @param buffer:
     *
     * <pre> NOTE: Unit is millisecond, range is 0-5000 ms </pre>
     *
     * @return {0} if successful
     */
    external fun SmartPlayerSetBuffer(handle: Long, buffer: Int): Int


    /**
     * Set mute or not
     *
     * @param is_mute: if with 1:mute, if with 0: does not mute
     *
     * @return {0} if successful
     */
    external fun SmartPlayerSetMute(handle: Long, is_mute: Int): Int


    /**
     * It's only used when playback RTSP stream
     *
     * Default with UDP mode
     *
     * @param isUsingTCP: if with 1, it will via TCP mode, while 0 with UDP mode
     *
     * @return {0} if successful
     */
    external fun SmartPlayerSetRTSPTcpMode(handle: Long, is_using_tcp: Int): Int


    /**
     * Set fast startup
     *
     * @param is_fast_startup: if with 1, it will second play back, if with 0: does not it
     *
     * @return {0} if successful
     */
    external fun SmartPlayerSetFastStartup(handle: Long, is_fast_startup: Int): Int


    /**
     * Set low latency mode
     *
     * @param mode: if with 1, low latency mode, if with 0: normal mode
     *
     * @return {0} if successful
     */
    external fun SmartPlayerSetLowLatencyMode(handle: Long, mode: Int): Int


    /**
     * 设置顺时针旋转, 注意除了0度之外， 其他角度都会额外消耗性能
     * @param handle
     * @param degress： 当前支持 0度，90度, 180度, 270度 旋转
     * @return {0} if successful
     */
    external fun SmartPlayerSetRotation(handle: Long, degress: Int): Int


    /**
     * Set report download speed
     *
     * @param handle
     * @param is_report: if with 1, it will report download speed, it with 0: does not it.
     * @param report_interval: report interval, unit is second, it must be greater than 0.
     * @return
     */
    external fun SmartPlayerSetReportDownloadSpeed(handle: Long, is_report: Int, report_interval: Int): Int

    /**
     * Set playback orientation.
     *
     * @param handle: return value from SmartPlayerOpen()
     *
     * @param surOrg: current orientation,  PORTRAIT 1, LANDSCAPE with 2
     *
     * @return {0} if successful
     */
    external fun SmartPlayerSetOrientation(handle: Long, surOrg: Int): Int

    /**
     * Set if needs to save image during playback stream
     *
     * @param handle: return value from SmartPlayerOpen()
     *
     * @param is_save_image: if with 1, it will save current image via the interface of SmartPlayerSaveCurImage(), if with 0: does not it
     *
     * @return {0} if successful
     */
    external fun SmartPlayerSaveImageFlag(handle: Long, is_save_image: Int): Int

    /**
     * Save current image during playback stream
     *
     * @param handle: return value from SmartPlayerOpen()
     *
     * @param imageName: image name, which including fully path, "/sdcard/daniuliveimage/daniu.png", etc.
     *
     * @return {0} if successful
     */
    external fun SmartPlayerSaveCurImage(handle: Long, imageName: String): Int

    /**
     * Switch playback url
     *
     * @param handle: return value from SmartPlayerOpen()
     *
     * @param uri: the new playback uri
     *
     * @return {0} if successful
     */
    external fun SmartPlayerSwitchPlaybackUrl(handle: Long, uri: String): Int

    /**
     * Create file directory
     *
     * @param path,  E.g: /sdcard/daniulive/rec
     *
     * <pre> The interface is only used for recording the stream data to local side. </pre>
     *
     * @return {0} if successful
     */
    external fun SmartPlayerCreateFileDirectory(path: String): Int

    /**
     * Set recorder directory.
     *
     * @param path: the directory of recorder file.
     *
     * <pre> NOTE: make sure the path should be existed, or else the setting failed. </pre>
     *
     * @return {0} if successful
     */
    external fun SmartPlayerSetRecorderDirectory(handle: Long, path: String): Int

    /**
     * Set the size of every recorded file.
     *
     * @param size: (MB), (5M~500M), if not in this range, set default size with 200MB.
     *
     * @return {0} if successful
     */
    external fun SmartPlayerSetRecorderFileMaxSize(handle: Long, size: Int): Int

    /**
     * Set playback/recorder stream url
     *
     * @param handle: return value from SmartPlayerOpen()
     *
     * @param uri: playback/recorder uri
     *
     * @return {0} if successful
     */
    external fun SmartPlayerSetUrl(handle: Long, uri: String): Int

    /**
     * Start playback stream
     *
     * @param handle: return value from SmartPlayerOpen()
     *
     * @return {0} if successful
     */
    external fun SmartPlayerStartPlay(handle: Long): Int

    /**
     * Stop playback stream
     *
     * @param handle: return value from SmartPlayerOpen()
     *
     * @return {0} if successful
     */
    external fun SmartPlayerStopPlay(handle: Long): Int

    /**
     * Start recorder stream
     *
     * @param handle: return value from SmartPlayerOpen()
     *
     * @return {0} if successful
     */
    external fun SmartPlayerStartRecorder(handle: Long): Int

    /**
     * Stop recorder stream
     *
     * @param handle: return value from SmartPlayerOpen()
     *
     * @return {0} if successful
     */
    external fun SmartPlayerStopRecorder(handle: Long): Int

    /**
     * Close player instance.
     *
     * @param handle: return value from SmartPlayerOpen()
     *
     * <pre> NOTE: it could not use player handle after call this function. </pre>
     *
     * @return {0} if successful
     */
    external fun SmartPlayerClose(handle: Long): Int
}

package xyz.thewhitedog9487.freeunlock.Xposed

import android.Manifest
import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.admin.DevicePolicyManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Binder
import android.os.Build
import android.os.SystemClock
import androidx.core.content.ContextCompat
import com.highcapable.kavaref.KavaRef.Companion.asResolver
import com.highcapable.kavaref.KavaRef.Companion.resolve
import com.highcapable.kavaref.extension.classOf
import com.highcapable.yukihookapi.annotation.xposed.YukiHookLibXposedEntry
import com.highcapable.yukihookapi.extension.kavaref.hook
import com.highcapable.yukihookapi.extension.kavaref.intercept
import com.highcapable.yukihookapi.extension.kavaref.resolver.AndroidHiddenApiBypassResolver
import com.highcapable.yukihookapi.extension.kavaref.toClass
import com.highcapable.yukihookapi.hook.factory.configure
import com.highcapable.yukihookapi.hook.factory.encase
import com.highcapable.yukihookapi.hook.log.YLog
import com.highcapable.yukihookapi.hook.xposed.YukiHookXposedModule
import xyz.thewhitedog9487.freeunlock.BuildConfig

@SuppressLint("StaticFieldLeak")
@YukiHookLibXposedEntry(
    targetApiVersion = 102,
//    hotReload = YukiHookLibXposedEntry.HotReload.AUTO,
    scope = ["system"] )
object HookMain : YukiHookXposedModule{
    const val LogTag = "FreeUnlock"
    const val TargetClassName = "com.android.server.locksettings.LockSettingsStrongAuth"
    const val TargetMethodName = "rescheduleStrongAuthTimeoutAlarm"
    const val LegacyTargetClassName = "com.android.server.LockSettingsStrongAuth"
    const val LegacyTargetMethodName = "handleScheduleStrongAuthTimeout"
    val HiddenApiBypassResolver = AndroidHiddenApiBypassResolver()
    val HookClassName = if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1 )
        TargetClassName
        else LegacyTargetClassName
    val HookMethodName = if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.R )
        TargetMethodName
        else LegacyTargetMethodName
    val HookMethodParametersList = if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.R )
        listOf(Long::class, Int::class)
        else listOf(Int::class)
    lateinit var SystemServerContext: Context
    lateinit var TargetClassInstance: Any
    val TestActionFilter = IntentFilter("xyz.thewhitedog9487.freeunlock.action.TEST")
    @Volatile var IsEnabled = true
    @Volatile var IsHookInstalled = false
    @Volatile var SkipNext = false
    val CurrentUserId: Int? get() {
        return classOf<ActivityManager>()
            .resolve()
            .processor(HiddenApiBypassResolver)
            .firstMethod {
                name = "getCurrentUser"
                emptyParameters() }
            .invoke() as? Int }
        fun GetAlarmTimeoutForTargetUser(UserId: Int): Long {
            val Token = Binder.clearCallingIdentity()
            return try {
                SystemServerContext
                    .getSystemService(classOf<DevicePolicyManager>())
                    .asResolver()
                    .processor(HiddenApiBypassResolver)
                    .firstMethod {
                        name = "getRequiredStrongAuthTimeout"
                        parameters( ComponentName::class, Int::class ) }
                    .invoke(null, UserId) as Long }
            finally {
                Binder.restoreCallingIdentity(Token) } }

    /**
     * 临时放行一次强认证调度请求，用来让系统立刻要求一次强认证
     * @return 是否成功发起了调度请求
     */
    fun ForceStrongAuth(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            YLog.error("Android 11以下的版本不支持设置自定义到期时间的闹钟")
            return false }
        YLog.info("正在尝试触发强认证调度请求")
        val UserID = CurrentUserId ?: run {
            YLog.error("无法获取当前用户ID，已取消本次强认证调度请求")
            return false }
        val Succeeded = runCatching {
            val ExpiredTime = SystemClock.elapsedRealtime() - GetAlarmTimeoutForTargetUser(UserID) - 1_000L
            SkipNext = true
            YLog.info("已设置SkipNext属性为true，本次强认证调度请求将被放行。")
            TargetClassInstance
                .asResolver()
                .firstMethod {
                    name = HookMethodName
                    parameters( *HookMethodParametersList.toTypedArray() ) }
                .invoke( ExpiredTime, UserID ) }
            .onFailure {
                YLog.error("触发强认证调度请求失败", it)
                SkipNext = false
                YLog.warn("由于出现灾难性错误，已经将SkipNext重置为false") }
            .isSuccess
        if (Succeeded) YLog.info("已成功触发强认证调度请求")
        return Succeeded }

    val TestActionReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            try {
                when (val Command = intent.getStringExtra("command")) {
                    "enable" -> {
                        IsEnabled = true
                        YLog.info("已启用FreeUnlock。") }
                    "disable" -> {
                        IsEnabled = false
                        YLog.info("已禁用FreeUnlock。") }
                    "test" -> {
                        YLog.warn("收到测试命令")
                        ForceStrongAuth() }
                    else -> {
                        YLog.error("收到未知命令：$Command") } }
            } catch (ExceptionInstance: Exception) {
                YLog.error("处理广播接收器时发生未被处理的异常", ExceptionInstance) } } }

    @Volatile var TestActionReceiverRegistered = false

    override fun onInit() = configure {
        logging {
            recording = true
            tag = LogTag }
        debug = BuildConfig.DEBUG }

//        registerModuleLifecycle {
//            onDispose {
//                YLog.info("正在清理模块持有的资源")
//                try {
//                    if (TestActionReceiverRegistered) {
//                        SystemServerContext.unregisterReceiver(TestActionReceiver)
//                        TestActionReceiverRegistered = false
//                        YLog.info("已注销调试用广播接收器。") } }
//                catch (ExceptionInstance: Exception) {
//                    YLog.error("注销调试用广播接收器时发生未被处理的异常", ExceptionInstance) }
//                YLog.info("清理完成") } }

    override fun onHook() = encase {
        loadSystem {
//            https://github.com/ReChronoRain/HyperCeiler/blob/4100e668e4deb56eda59f7dc6c1ffbbd1211cad7/library/hook/src/main/java/com/sevtinge/hyperceiler/hook/module/rules/systemframework/DisablePinVerifyPer72h.java
//            ↑ 注入位置参考自HyperCeiler的实现

/*
系统版本        API编号   API代码   Build.VERSION_CODES类对应常量   类名                                                      函数签名
Android 8.0    26       Oreo     O                              com.android.server.LockSettingsStrongAuth                private void handleScheduleStrongAuthTimeout(int userId)
Android 8.1    27       Oreo     O_MR1                          com.android.server.locksettings.LockSettingsStrongAuth   ↑
Android 9.0    28       P        P                              ↑                                                        ↑
Android 10.0   29       Q        Q                              ↑                                                        ↑
Android 11.0   30       R        R                              ↑                                                        private void rescheduleStrongAuthTimeoutAlarm(long strongAuthTime, int userId)
Android 11+                                                     ↑                                                        ↑
*/
            YLog.info("成功注入系统框架")
            dataChannel.with {
                wait(DataChannelKeys.Query_HookStatus) {
                    YLog.info("收到App发出的查询Hook注入状态请求")
                    put(DataChannelKeys.Response_HookSuccess, IsHookInstalled)
                    put(DataChannelKeys.Response_HookEnabled, IsEnabled) }
                wait(DataChannelKeys.Set_HookEnabled) { NewValue ->
                    YLog.info("收到App发出的更改模块功能启用状态请求，新的工作状态为 $NewValue")
                    IsEnabled = NewValue
                    YLog.info("根据App请求，模块工作状态已调整为 ${IsEnabled}") }
                wait(DataChannelKeys.Request_ClearLogs) {
                    YLog.clear()
                    YLog.info("收到App发出的清除日志请求，已完成清理") }
                wait(DataChannelKeys.Request_ForceStrongAuth) {
                    YLog.info("收到App发出的强认证调度请求")
                    put(DataChannelKeys.Response_StrongAuthAlarmScheduleSuccess, ForceStrongAuth()) } }
            HookClassName
                .toClass()
                .resolve()
                .firstMethod {
                    name = HookMethodName
                    parameters( *HookMethodParametersList.toTypedArray() ) }
                .intercept {
                    if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.R ) {
                        YLog.info("收到强认证调度请求。strongAuthTime=${args[0]} userId=${args[1]}") }
                    else {
                        YLog.info("收到强认证调度请求。userId=${args[0]}") }
                    if (SkipNext) {
                        YLog.warn("由于SkipNext属性为true，放行本次强认证调度请求。")
                        SkipNext = false
                        return@intercept proceed() }
                    if (IsEnabled) {
                        YLog.info("成功拦截强认证调度请求。")
                        return@intercept null }
                    else {
                        YLog.warn("由于IsEnabled属性为false，放行本次强认证调度请求。")
                        return@intercept proceed() } }
            YLog.info("成功注入${HookClassName}.${HookMethodName}函数")
            HookClassName
                .toClass()
                .resolve()
                .firstConstructor {
                    parameters ( Context::class ) }
                .hook {
                    after {
                        TargetClassInstance = instance
                        SystemServerContext = args[0] as Context
                        YLog.info("成功获取LockSettingsStrongAuth实例和SystemServerContext")
                        if ( TestActionReceiverRegistered == false ){
                            try {
                                ContextCompat.registerReceiver(
                                    SystemServerContext,
                                    TestActionReceiver,
                                    TestActionFilter,
                                    Manifest.permission.DUMP,
                                    null,
                                    ContextCompat.RECEIVER_EXPORTED)
                                YLog.info("成功注册调试用广播接收器。") }
                            catch (ExceptionInstance: Exception) {
                                YLog.error("注册调试用广播接收器时发生未被处理的异常", ExceptionInstance)
                                return@after }
                            TestActionReceiverRegistered = true } } }
            YLog.info("成功注入${HookClassName}构造函数")
            // 两处Hook都安装成功之后才向App报告注入成功
            IsHookInstalled = true
            YLog.info("Hook注入完成") } } }
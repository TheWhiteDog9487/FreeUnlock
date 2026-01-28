package xyz.thewhitedog9487.freeunlock.xposed

import com.highcapable.kavaref.KavaRef.Companion.resolve
import com.highcapable.yukihookapi.YukiHookAPI
import com.highcapable.yukihookapi.annotation.xposed.InjectYukiHookWithXposed
import com.highcapable.yukihookapi.hook.factory.configs
import com.highcapable.yukihookapi.hook.log.YLog
import com.highcapable.yukihookapi.hook.xposed.proxy.IYukiHookXposedInit
import xyz.thewhitedog9487.freeunlock.BuildConfig

const val TargetClassName = "com.android.server.locksettings.LockSettingsStrongAuth"
const val TargetMethodName = "rescheduleStrongAuthTimeoutAlarm"

@InjectYukiHookWithXposed
class HookMain : IYukiHookXposedInit{
    override fun onHook() = YukiHookAPI.encase {
        loadSystem {
//            https://github.com/ReChronoRain/HyperCeiler/blob/4100e668e4deb56eda59f7dc6c1ffbbd1211cad7/library/hook/src/main/java/com/sevtinge/hyperceiler/hook/module/rules/systemframework/DisablePinVerifyPer72h.java
//            ↑ 注入位置参考自HyperCeiler的实现
//            XposedHelpers.findAndHookMethod(TargetClassName.toClass(), TargetMethodName, Long::class, Int::class, XC_MethodReplacement.DO_NOTHING)
            TargetClassName
                .toClass()
                .resolve()
                .firstMethod {
                    name = TargetMethodName
                    parameters(Long::class, Int::class) }
                .hook{
                    replaceUnit{
                        YLog.info("成功拦截强认证调度请求。") } }
                .result {
                    onAllFailure {
                        it.printStackTrace()
                        YLog.error(it) } } } }

    override fun onInit() = configs {
        YLog.Configs.tag = "FreeUnlock"
        isDebug = BuildConfig.DEBUG } }
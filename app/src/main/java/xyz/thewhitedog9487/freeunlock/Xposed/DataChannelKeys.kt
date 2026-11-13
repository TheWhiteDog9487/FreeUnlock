package xyz.thewhitedog9487.freeunlock.Xposed

import com.highcapable.yukihookapi.hook.xposed.channel.data.ChannelData

/**
 * 模块管理器App和被注入代码之间使用YukiHook通信桥传递的数据的类别定义
 */
object DataChannelKeys {
    /**
     * 由App发出，查询Hook注入的代码的状态
     * 返回 [Response_HookSuccess] 和 [Response_HookEnabled]
     */
    const val Query_HookStatus = "Query_HookStatus"

    /**
     * Hook注入代码响应App的查询请求
     * 值为代码注入是否成功
     */
    val Response_HookSuccess = ChannelData<Boolean>("Response_HookSuccess")

    /**
     * Hook注入代码响应App的查询请求
     * 值为模块功能当前是否启用
     */
    val Response_HookEnabled = ChannelData<Boolean>("Response_HookEnabled")

    /**
     * 由App发出，更改模块功能启用状态
     */
    val Set_HookEnabled = ChannelData<Boolean>("Set_HookEnabled")

    /**
     * 由App发出，清除Hook注入代码中的内存日志
     */
    const val Request_ClearLogs = "Request_ClearLogs"

    /**
     * 由App发出，要求注入代码立刻调度一个过期的强认证闹钟
     */
    const val Request_ForceStrongAuth = "Request_ForceStrongAuth"

    /**
     * Hook注入代码向App返回 [Request_ForceStrongAuth] 是否成功
     * 值为闹钟是否成功调度
     */
    val Response_StrongAuthAlarmScheduleSuccess = ChannelData<Boolean>("Response_StrongAuthAlarmScheduleSuccess") }
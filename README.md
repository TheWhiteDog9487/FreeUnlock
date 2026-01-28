<h1 align="center"> 自由解锁 </h1>

<p align="center"> 让屏幕解锁再次迅速 </p>

## 功能
禁用Android中每72小时必须使用密码解锁屏幕的限制

## 使用方式
1. 安装一个Xposed框架
2. 在[Github Release](https://github.com/TheWhiteDog9487/FreeUnlock/releases/latest)中下载最新的Release版本APK
3. 安装APK
4. 打开您使用的Xposed管理器，启用模块并**勾选系统框架(包名应当为android)作为作用域**
5. 重启设备
6. 没了

您应当能够在重启设备之后第一次使用密码解锁设备后在Xposed管理器的日志内看到模块打印的一条日志

## 支持的Android版本
不明确，具体情况请自行测试  
在我本人的设备上通过测试，确认功能有效的有：
- Redmi K30 5G Picasso( Project Infinity X v3.5 | Android 16 Baklava | 由ManukaTM提供的非官方构建 )
- Realme GT8 RMX6699( realme ui 7.0 16.0.2.473 | Android 16 Baklava | 国行官方系统 )

## 实现机制
模块会拦截系统尝试设置强认证计时器的行为，没有计时器就不会触发强认证，没有强认证自然就安静了  
具体请参考[源代码](https://github.com/TheWhiteDog9487/FreeUnlock/blob/%E4%B8%BB%E8%A6%81/app/src/main/java/xyz/thewhitedog9487/freeunlock/xposed/HookMain.kt)

## 感谢
- [HyperCeiler](https://github.com/ReChronoRain/HyperCeiler) 提供的注入点参考
- [YukiHookAPI](https://github.com/HighCapable/YukiHookAPI) 提供的好用的代码注入API
- [KavaRef](https://github.com/HighCapable/KavaRef) 提供的非常好用的基于Kotlin Lambda作为DSL的反射相关API

## 开源许可
WTFPL
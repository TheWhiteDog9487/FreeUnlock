package xyz.thewhitedog9487.freeunlock

import android.os.Build
import com.highcapable.kavaref.KavaRef.Companion.resolve
import com.highcapable.kavaref.condition.type.Modifiers
import com.highcapable.kavaref.extension.classOf
import kotlin.collections.mapValues
import kotlin.getValue

private val SdkVersionToCodeName: Map<Int, String> by lazy {
    classOf<Build.VERSION_CODES>()
        .resolve()
        .field {
            modifiers(Modifiers.STATIC, Modifiers.FINAL)
            type(classOf<Int>()) }
        .mapNotNull { Field ->
            Field.getQuietly<Int>()?.let { value -> value to Field.self.name } }
        .groupBy({ it.first }, { it.second })
        .mapValues { (_, names) -> names.maxBy { it.length } } }

val Int.SdkVersionName: String
    get() = (SdkVersionToCodeName[this] ?: toString())
        .split("_")
        .joinToString(" ") { Part ->
            Part.lowercase().replaceFirstChar { it.uppercase() } }
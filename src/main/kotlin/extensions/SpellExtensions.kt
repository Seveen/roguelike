package com.necroworld.extensions

import com.necroworld.spells.Spell
import kotlin.reflect.full.isSubclassOf

inline fun <reified T : Spell> Spell.whenTypeIs(fn: (T) -> Unit) {
    if (this::class.isSubclassOf(T::class)) {
        fn(this as T)
    }
}
package com.necroworld.extensions

import com.necroworld.attributes.types.Spellcaster
import com.necroworld.spells.Spell
import org.hexworks.zircon.api.data.Position
import kotlin.reflect.full.isSubclassOf

inline fun <reified T : Spell> Spell.whenTypeIs(fn: (T) -> Unit) {
    if (this::class.isSubclassOf(T::class)) {
        fn(this as T)
    }
}
package com.gabriel.ui.model

import kotlin.reflect.KProperty

/**
 * Add onPropertyChange on compile time with the correct type and notify
 *
 * @constructor Create empty Property change source
 */
abstract class PropertyChangeSource {
    @PublishedApi
    internal val listenersMap = mutableMapOf<KProperty<*>, MutableList<(Any) -> Unit>>()

    /**
     * Add onPropertyChange on compile time
     *
     * @param T
     * @param property
     * @param action
     * @receiver
     */
    @Suppress("UNCHECKED_CAST")
    inline fun <reified T> onPropertyChange(property: KProperty<T>, noinline action: (T) -> Unit) {
        val actions = listenersMap.getOrPut(property) { mutableListOf() } as MutableList<(T) -> Unit>
        actions += action
    }

    // Property change source working
    /*
        val model = GameModel()
        model.playerLife

        // view
        model.onPropertyChange(model::playerLife) { playerLife ->
            lifeBar.setValue(playerLife)
        }

        model.onPropertyChange(model::lootText) { text ->
            label.txt = text
        }
     */

    /**
     * Notify on property change
     *
     * @param property
     * @param value
     *///*
    fun notify(property: KProperty<*>, value: Any) {
        listenersMap[property]?.forEach { it(value) }
    }

}

/**
 * Property notifier Delegation Class
 *
 * @param T
 * @constructor
 *
 * @param initialValue
 */
class PropertyNotifier<T : Any>(initialValue: T) {
    private var _value: T = initialValue

    operator fun getValue(thisRef: PropertyChangeSource, property: KProperty<*>): T = _value

    operator fun setValue(thisRef: PropertyChangeSource, property: KProperty<*>, value: T) {
        _value = value
        thisRef.notify(property, value)
    }
}

inline fun <reified T : Any> propertyNotify(initialValue: T): PropertyNotifier<T> = PropertyNotifier(initialValue)

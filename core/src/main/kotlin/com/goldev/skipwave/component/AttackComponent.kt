package com.goldev.skipwave.component

/**
 *  AttackState is an enum class that contains all states of an attack
 */
enum class AttackState {
    READY, PREPARE, ATTACKING, DEAL_DAMAGE
}


/**
 *  It's a component that holds the data of an attack
 *
 *  @property doAttack store if entity do attack
 *  @property state the attack state of the entity
 *  @property damage the damage of the entity
 *  @property cooldown the cooldown attack of the entity
 *  @property maxCooldown the max cooldown attack of the entity
 *  @property extraRange the extra range for an attack
 *  @constructor Creates an AttackComponent with default values
 */
class AttackComponent(
    var doAttack: Boolean = false,
    var state: AttackState = AttackState.READY,
    var damage: Int = 0,
    var cooldown: Float = 0f,
    var maxCooldown: Float = 0f,
    var extraRange: Float = 0f,
) {
    /**
     *  It's a getter for the property isReady.
     *  @return If the state is ready.
     */
    val isReady: Boolean
        get() = state == AttackState.READY

    /**
     *  It's a getter for the property isPrepared.
     *  @return If the state is prepared.
     */
    val isPrepared: Boolean
        get() = state == AttackState.PREPARE

    /**
     *  It's a getter for the property isAttacking.
     *  @return If the state is attacking.
     */
    val isAttacking: Boolean
        get() = state == AttackState.ATTACKING

    /**
     *  It's a getter for the property isDealingDamage.
     *  @return If the state is deal damage.
     */
    val isDealingDamage: Boolean
        get() = state == AttackState.DEAL_DAMAGE

    /**
     * The function startAttack sets the value of the variable state to the value of the enum
     * constant PREPARE
     */
    fun startAttack() {
        state = AttackState.PREPARE
    }
}
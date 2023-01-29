package com.gabriel.component

enum class AttackState {
    READY, PREPARE, ATTACKING, DEAL_DAMAGE
}

class AttackComponent(
    var doAttack: Boolean = false,
    var state: AttackState = AttackState.READY,
    var damage: Int = 0,
    var cooldown: Float = 0f,
    var maxCooldown: Float = 0f,
    var extraRange: Float = 0f,
) {
    val isReady: Boolean
        get() = state == AttackState.READY

    val isPrepared: Boolean
        get() = state == AttackState.PREPARE

    val isAttacking: Boolean
        get() = state == AttackState.ATTACKING

    val isDealingDamage: Boolean
        get() = state == AttackState.DEAL_DAMAGE

    fun startAttack() {
        state = AttackState.PREPARE
    }
}
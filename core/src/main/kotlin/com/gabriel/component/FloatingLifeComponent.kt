//package com.gabriel.component
//
//import com.badlogic.gdx.math.Interpolation
//import com.badlogic.gdx.math.MathUtils
//import com.badlogic.gdx.scenes.scene2d.Stage
//import com.badlogic.gdx.scenes.scene2d.actions.Actions
//import com.badlogic.gdx.scenes.scene2d.ui.Image
//import com.badlogic.gdx.scenes.scene2d.ui.Label
//import com.badlogic.gdx.scenes.scene2d.ui.Skin
//import com.badlogic.gdx.scenes.scene2d.ui.Table
//import com.gabriel.ui.Drawables
//import com.gabriel.ui.get
//import com.gabriel.ui.model.HUDModel
//import com.gabriel.ui.view.HUDView
//import com.github.quillraven.fleks.ComponentListener
//import com.github.quillraven.fleks.Entity
//import com.github.quillraven.fleks.Qualifier
//import ktx.actors.plusAssign
//import ktx.math.vec2
//import ktx.scene2d.*
//
//class FloatingLifeComponent(
//    skin: Skin,
//) : Table(skin), KTable {
//    val txtLocation = vec2()
//    var txtTarget = vec2()
//    var lifeSpan = 0f
//    var time = 0f
//
//    private val background: Image = Image(skin[Drawables.LIFE_UNDER])
//    private val lifeBar: Image = Image(skin[Drawables.LIFE_BAR])
//
//    init {
//        this += background
//        this += lifeBar.apply { setPosition(0f, 0f) }
//    }
//
//    override fun getPrefWidth() = background.drawable.minWidth
//
//    override fun getPrefHeight() = background.drawable.minHeight
//
//    fun life(percentage: Float, duration: Float = 0.75f) {
//        lifeBar.clearActions()
//        lifeBar += Actions.scaleTo(MathUtils.clamp(percentage, 0f, 1f), 1f, duration)
//    }
//
//    companion object {
//        class FloatingTextComponentListener(
//            @Qualifier("uiStage") private val uiStage: Stage
//        ) : ComponentListener<FloatingTextComponent> {
//
//            override fun onComponentAdded(entity: Entity, component: FloatingTextComponent) {
//                uiStage.addActor(component.label)
//                component.label += Actions.fadeOut(component.lifeSpan, Interpolation.pow3OutInverse)
//                component.txtTarget.set(
//                    component.txtLocation.x + MathUtils.random(-1.5f, 1.5f),
//                    component.txtLocation.y + 1f
//                )
//            }
//
//            override fun onComponentRemoved(entity: Entity, component: FloatingTextComponent) {
//                uiStage.root.removeActor(component.label)
//            }
//        }
//    }
//}
//
//@Scene2dDsl
//fun <S> KWidget<S>.floatinglifecomponent(
//    skin: Skin = Scene2DSkin.defaultSkin,
//    init: FloatingLifeComponent.(S) -> Unit = {}
//): FloatingLifeComponent = actor(FloatingLifeComponent(skin), init)
//
//
//
//
//
//
//

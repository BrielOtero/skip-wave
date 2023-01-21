//package com.gabriel.ui.view
//
//import com.badlogic.gdx.scenes.scene2d.ui.Skin
//import com.badlogic.gdx.scenes.scene2d.ui.Table
//import ktx.scene2d.*
//
//class SkillUpgradeView(
//    private val model:SkillUpgradeModel,
//    skin: Skin
//) : KTable, Table(skin) {
//    init {
//        //UI
//        setFillParent(true)
//
//        table {
//
//        }
//        // data binding
//    }
//}
//
//@Scene2dDsl
//fun<S> KWidget<S>.skillUpgradeView(
//    model:SkillUpgradeModel,
//    skin:Skin=Scene2DSkin.defaultSkin,
//    init:SkillUpgradeView.(S)->Unit={}
//): SkillUpgradeView=actor(SkillUpgradeView(model,skin),init)
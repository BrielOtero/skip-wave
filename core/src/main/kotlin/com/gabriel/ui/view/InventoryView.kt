//package com.gabriel.ui.view
//
//import com.badlogic.gdx.scenes.scene2d.ui.Skin
//import com.badlogic.gdx.scenes.scene2d.ui.Table
//import com.gabriel.ui.model.InventoryModel
//import ktx.scene2d.*
//
//class InventoryView(
//    private val model: InventoryModel,
//    skin: Skin,
//) : KTable, Table(skin) {
//}
//
//@Scene2dDsl
//fun <S> KWidget<S>.inventoryView(
//    model: InventoryModel,
//    skin: Skin = Scene2DSkin.defaultSkin,
//    init: InventoryView.(S) -> Unit = {}
//): InventoryView = actor(InventoryView(model, skin), init)
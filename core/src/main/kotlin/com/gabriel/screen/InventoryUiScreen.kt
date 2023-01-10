//package com.gabriel.screen
//
//import com.badlogic.gdx.Gdx
//import com.badlogic.gdx.Input
//import com.badlogic.gdx.scenes.scene2d.Stage
//import com.badlogic.gdx.utils.viewport.ExtendViewport
//import com.gabriel.component.InventoryComponent
//import com.gabriel.component.LifeComponent
//import com.gabriel.component.PlayerComponent
//import com.gabriel.event.EntityDamageEvent
//import com.gabriel.event.fire
//import com.gabriel.ui.Drawables
//import com.gabriel.ui.model.GameModel
//import com.gabriel.ui.model.InventoryModel
//import com.gabriel.ui.view.GameView
//import com.gabriel.ui.view.InventoryView
//import com.gabriel.ui.view.gameView
//import com.gabriel.ui.view.inventoryView
//import com.github.quillraven.fleks.Entity
//import com.github.quillraven.fleks.world
//import ktx.app.KtxScreen
//import ktx.assets.disposeSafely
//import ktx.scene2d.actors
//
//class InventoryUiScreen : KtxScreen {
//    private val stage: Stage = Stage(ExtendViewport(320f, 180f))
//    private val eWorld = world { }
//    private val playerEntity: Entity
//    private val model = InventoryModel(eWorld, stage)
//    private lateinit var inventoryView: InventoryView
//
//    init {
//        playerEntity = eWorld.entity {
//            add<PlayerComponent>()
//            add<InventoryComponent>()
//        }
//    }
//
//    override fun resize(width: Int, height: Int) {
//        stage.viewport.update(width, height, true)
//    }
//
//    override fun show() {
//        stage.clear()
//        stage.addListener(model)
//        stage.actors {
//            inventoryView = inventoryView(model)
//        }
//        stage.isDebugAll = true
//
//    }
//
//    override fun render(delta: Float) {
//        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
//            hide()
//            show()
//        }
//
//        stage.act()
//        stage.draw()
//    }
//
//    override fun dispose() {
//        stage.disposeSafely()
//    }
//}
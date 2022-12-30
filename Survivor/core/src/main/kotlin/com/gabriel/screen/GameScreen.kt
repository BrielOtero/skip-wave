package com.gabriel.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.Scaling
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.gabriel.component.AnimationComponent
import com.gabriel.component.AnimationModel
import com.gabriel.component.AnimationType
import com.gabriel.component.ImageComponent
import com.gabriel.event.MapChangeEvent
import com.gabriel.event.fire
import com.gabriel.system.AnimationSystem
import com.gabriel.system.EntitySpawnSystem
import com.gabriel.system.RenderSystem
import com.github.quillraven.fleks.World
import ktx.app.KtxScreen
import ktx.assets.disposeSafely
import ktx.log.logger

class GameScreen : KtxScreen {
    private val stage: Stage = Stage(ExtendViewport(16f, 9f));
    private val textureAtlas = TextureAtlas(Gdx.files.internal("graphics/game.atlas"));
    private var currentMap : TiledMap? = null;

    private val world: World = World {
        inject(stage);
        inject(textureAtlas);

        componentListener<ImageComponent.Companion.ImageComponentListener>();

        system<EntitySpawnSystem>();
        system<AnimationSystem>();
        system<RenderSystem>();

    }

    override fun show() {
        log.debug { "GameScreen gets shown" };

        world.systems.forEach { system->
            if(system is EventListener){
                stage.addListener(system)
            }
        }

        currentMap = TmxMapLoader().load(Gdx.files.internal("maps/demo.tmx").path());
        stage.fire(MapChangeEvent(currentMap!!));

    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true);
    }

    override fun render(delta: Float) {
        world.update(delta);
    }

    override fun dispose() {
        stage.disposeSafely();
        textureAtlas.disposeSafely();
        world.dispose();
        currentMap?.disposeSafely();
    }

    companion object {
        private val log = logger<GameScreen>();
    }
}
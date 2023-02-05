package com.gabriel.system

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.gabriel.component.AnimationModel
import com.gabriel.event.*
import com.gabriel.preferences.GamePreferences
import com.gabriel.preferences.loadGamePreferences
import com.github.quillraven.fleks.IntervalSystem
import com.github.quillraven.fleks.World
import ktx.assets.disposeSafely
import ktx.log.logger
import ktx.tiled.propertyOrNull

class AudioSystem(
    private val gamePreferences: GamePreferences
) : EventListener, IntervalSystem() {

    private val musicCache = mutableMapOf<String, Music>()
    private val soundCache = mutableMapOf<String, Sound>()
    private val soundRequests = mutableMapOf<String, Sound>()

    override fun onTick() {
        if (soundRequests.isEmpty()) {
            return
        }

        soundRequests.values.forEach { it.play(1f) }
        soundRequests.clear()
    }

    override fun handle(event: Event): Boolean {
        when (event) {
            is MapChangeEvent -> {
                event.map.propertyOrNull<String>("music")?.let { path ->
                    log.debug { "Changing music to $path" }
                    val music = musicCache.getOrPut(path) {
                        Gdx.audio.newMusic(Gdx.files.internal(path)).apply {
                            isLooping = true
                            volume = gamePreferences.settings.musicVolume
//                            volume = 0.75f
//                            volume=0f
                        }
                    }
                    music.play()
                }
                return true
            }

            is EntityAttackEvent -> {
                if (event.model != AnimationModel.PLAYER && event.model != AnimationModel.SLASH_LEFT && event.model != AnimationModel.SLASH_RIGHT) {
                    queueSound("audio/enemy_attack.wav")
                } else {
                    queueSound("audio/${event.model.atlasKey}_attack.wav")
                }

            }

            is EntityDeathEvent -> {
                if (event.model != AnimationModel.PLAYER && event.model != AnimationModel.SLASH_LEFT && event.model != AnimationModel.SLASH_RIGHT) {
                    queueSound("audio/enemy_death.wav")
                } else {
                    queueSound("audio/${event.model.atlasKey}_death.wav")

                }
            }

            is EntityLootEvent -> queueSound("audio/${event.model.atlasKey}_open.wav")
            is EntityLevelEvent -> queueSound("audio/level_up.wav")
        }

        return false
    }

    private fun queueSound(soundPath: String) {
//        log.debug { "Queuing sound $soundPath" }
        if (soundPath in soundRequests) {
            // already queued -> do nothing
            return
        }

        val sound = soundCache.getOrPut(soundPath) {
            Gdx.audio.newSound(Gdx.files.internal(soundPath))
        }
        soundRequests[soundPath] = sound
    }

    override fun onDispose() {
        musicCache.values.forEach { it.disposeSafely() }
        soundCache.values.forEach { it.disposeSafely() }
    }

    companion object {
        private val log = logger<AudioSystem>()
    }
}
package com.goldev.skipwave.system

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.goldev.skipwave.component.AnimationModel
import com.goldev.skipwave.preferences.GamePreferences
import com.github.quillraven.fleks.IntervalSystem
import com.goldev.skipwave.event.*
import ktx.assets.disposeSafely
import ktx.log.logger
import ktx.tiled.propertyOrNull

/**
 * System that takes care of the audio of the game.
 *
 * @property gamePreferences The preferences of the game.
 * @constructor Create empty Audio system.
 */
class AudioSystem(
    private val gamePreferences: GamePreferences

) : EventListener, IntervalSystem() {

    /**
     *  Mutable map of music cache.
     */
    private val musicCache = mutableMapOf<String, Music>()

    /**
     *  Mutable map of sound cache.
     */
    private val soundCache = mutableMapOf<String, Sound>()

    /**
     *  Mutable map of sound requests.
     */
    private val soundRequests = mutableMapOf<String, Sound>()

    /**
     * If there are any sound requests, play them all at once
     */
    override fun onTick() {
        if (soundRequests.isEmpty()) {
            return
        }
        soundRequests.values.forEach { it.play(gamePreferences.settings.effectsVolume) }
        soundRequests.clear()
    }

    /**
     * It handles events
     *
     * @param event The event to handle.
     * @return If true, the event is consumed by the method and not sent to the next one.
     */
    override fun handle(event: Event): Boolean {
        when (event) {
            is HideTutorialViewEvent -> {
                musicCache.values.forEach { music ->
                    music.stop()
                }
                setMainMusic("audio/mars_intro.ogg")
                return true
            }

            is SavePreferencesEvent -> {
                log.debug { "CHANGE AUDIO" }
                musicCache.forEach { musicSong ->
                    musicSong.value.volume = gamePreferences.settings.musicVolume
                }
            }

            is ShowMainMenuViewEvent -> {
                val path = "audio/main_menu.ogg"

                log.debug { "Changing music to $path" }
                val music = musicCache.getOrPut(path) {
                    Gdx.audio.newMusic(Gdx.files.internal(path)).apply {
                        isLooping = true
                        volume = gamePreferences.settings.musicVolume
                    }
                }
                music.play()
                return true
            }

            is MapChangeEvent -> {
                if (!gamePreferences.game.tutorialComplete) {
                    tutorialMusic()
                } else {
                    event.map.propertyOrNull<String>("music")?.let { path ->
                        setMainMusic(path)
                    }
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

            is ButtonPressedEvent -> queueSound("audio/button.wav")

            is EntityLootEvent -> queueSound("audio/${event.model.atlasKey}_open.wav")
            is EntityLevelEvent -> queueSound("audio/level_up.wav")
            is ShakeEvent -> queueSound("audio/hellou.wav")
        }

        return false
    }

    /**
     * If the sound is already queued, do nothing. Otherwise, load the sound from the cache or from the
     * file system and add it to the queue
     *
     * @param soundPath The path to the sound file.
     */
    private fun queueSound(soundPath: String) {
        log.debug { "Queuing sound $soundPath" }
        if (soundPath in soundRequests) {
            // already queued -> do nothing
            return
        }

        val sound = soundCache.getOrPut(soundPath) {
            Gdx.audio.newSound(Gdx.files.internal(soundPath))
        }
        soundRequests[soundPath] = sound
    }

    /**
     * Play the music of the tutorial.
     */
    private fun tutorialMusic() {
        val path = "audio/tutorial.ogg"
        log.debug { "Changing music to $path" }
        val music = musicCache.getOrPut(path) {
            Gdx.audio.newMusic(Gdx.files.internal(path)).apply {
                isLooping = true
                volume = gamePreferences.settings.musicVolume
            }
        }
        music.play()
    }

    /**
     * It loads a music file from the assets folder, sets the volume to the user's preference, and
     * plays it
     *
     * @param path The path to the music file.
     */
    private fun setMainMusic(path: String) {
        log.debug { "Changing music to $path" }
        val music = musicCache.getOrPut(path) {
            Gdx.audio.newMusic(Gdx.files.internal(path)).apply {
                volume = gamePreferences.settings.musicVolume
                setOnCompletionListener {
                    val newPath = path.replace("intro", "loop")
                    val musicLoop = musicCache.getOrPut(newPath) {
                        Gdx.audio.newMusic(Gdx.files.internal(newPath)).apply {
                            isLooping = true
                            volume = gamePreferences.settings.musicVolume
                        }
                    }
                    musicLoop.play()
                    log.debug { "Changing music to $newPath" }
                }
            }
        }
        music.play()
    }

    /**
     * Dispose of all the music and sound effects.
     */
    override fun onDispose() {
        musicCache.values.forEach { it.disposeSafely() }
        soundCache.values.forEach { it.disposeSafely() }
    }

    companion object {
        /**
         *  It's a logger that logs the class.
         */
        private val log = logger<AudioSystem>()
    }
}
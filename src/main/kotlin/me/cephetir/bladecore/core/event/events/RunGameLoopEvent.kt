package me.cephetir.bladecore.core.event.events

import me.cephetir.bladecore.core.event.Event

abstract class RunGameLoopEvent : Event {
    class Start : RunGameLoopEvent()
    abstract class Tick : RunGameLoopEvent() {
        class Pre : Tick()
        class Post : Tick()
    }
    class Render : RunGameLoopEvent()
    class End : RunGameLoopEvent()
}
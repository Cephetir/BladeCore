package me.cephetir.bladecore.core.event.events

import me.cephetir.bladecore.core.event.Cancellable
import me.cephetir.bladecore.core.event.Event
import me.cephetir.bladecore.core.event.ICancellable

data class SendChatMessageEvent(val message: String, val addToChat: Boolean) : Event, ICancellable by Cancellable()
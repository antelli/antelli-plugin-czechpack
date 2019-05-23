package io.antelli.plugin.idnes.tv

import java.util.ArrayList
import java.util.HashMap

/**
 * Handcrafted by Štěpán Šonský on 21.11.2017.
 */

class IdnesTvChannels {

    private val channelsList = ArrayList<IdnesTvChannel>()
    private val channels = HashMap<Int, IdnesTvChannel>()

    fun addChannel(channel: IdnesTvChannel) {
        channelsList.add(channel)
        channels[channel.id] = channel
    }

    fun getChannel(id: Int): IdnesTvChannel? {
        return channels[id]
    }

    fun getChannelsList(): List<IdnesTvChannel> {
        return channelsList
    }


}

package net.technotalks.rekolink
import io.papermc.paper.event.player.AsyncChatEvent
import net.kyori.adventure.text.TextComponent
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class Events(private val socketServer: SocketServer) : Listener {
    @EventHandler
    fun onChatMessage(e: AsyncChatEvent) {
        val msgComponent= e.message() as TextComponent
        val message = msgComponent.content()
        val playerName = e.player.name
        socketServer.sendMessage("{\"type\": \"chat\", \"player\": \"$playerName\", \"msg\": \"$message\" }")
    }

}
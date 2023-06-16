package net.technotalks.rekolink
import org.bukkit.Bukkit.getPluginManager
import org.bukkit.plugin.java.JavaPlugin


class RekoLink : JavaPlugin() {
    private lateinit var socketServer: SocketServer
    override fun onEnable() {
        val port = this.config.getInt("port")
        socketServer = SocketServer(port, this)
        socketServer.start()
        //socketServer.sendMessage("Plugin has initialized")
        getPluginManager().registerEvents(Events(socketServer, this), this)
        this.config.options().copyDefaults()
        saveDefaultConfig()
    }

    override fun onDisable() {
        socketServer.stop()
    }
}

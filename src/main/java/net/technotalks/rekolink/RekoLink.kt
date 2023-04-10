package net.technotalks.rekolink
import org.bukkit.Bukkit.getPluginManager
import org.bukkit.plugin.java.JavaPlugin


class RekoLink : JavaPlugin() {
    private lateinit var socketServer: SocketServer
    override fun onEnable() {
        socketServer = SocketServer(8080, this)
        socketServer.start()
        logger.info("RekoLink has loaded")
        socketServer.sendMessage("Plugin has initialized")
        getPluginManager().registerEvents(Events(socketServer), this)
        this.config.options().copyDefaults()
        saveDefaultConfig()
    }

    override fun onDisable() {
        socketServer.stop()
        logger.info("RekoLink is shutting down")
    }
}

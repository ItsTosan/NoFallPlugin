package Nine.Studio.test;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

public final class NoFallPlugin extends JavaPlugin implements Listener, CommandExecutor {

    // Map to store locations for each world
    private Map<String, Location> voidLocations = new HashMap<>();

    @Override
    public void onEnable() {
        // Save the default config file
        saveDefaultConfig();

        // Load locations from config.yml
        for (String worldName : getConfig().getConfigurationSection("void-locations").getKeys(false)) {
            double x = getConfig().getDouble("void-locations." + worldName + ".x");
            double y = getConfig().getDouble("void-locations." + worldName + ".y");
            double z = getConfig().getDouble("void-locations." + worldName + ".z");
            World world = Bukkit.getWorld(worldName);
            if (world != null) {
                voidLocations.put(worldName, new Location(world, x, y, z));
            }
        }

        // Register events and commands
        Bukkit.getPluginManager().registerEvents(this, this);
        this.getCommand("voidLocation").setExecutor(this);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        String worldName = player.getWorld().getName();

        // If the player is below a certain height (e.g., Y=0), teleport them to the appropriate spawn location
        if (player.getLocation().getY() < 0 && voidLocations.containsKey(worldName)) {
            player.teleport(voidLocations.get(worldName));
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        // Prevent fall damage
        if (event.getEntity() instanceof Player && event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            event.setCancelled(true);
        }
    }

    // Command to set the void location for a specific world
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("voidLocation")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                String worldName = player.getWorld().getName();
                Location location = player.getLocation();

                voidLocations.put(worldName, location);

                getConfig().set("void-locations." + worldName + ".x", location.getX());
                getConfig().set("void-locations." + worldName + ".y", location.getY());
                getConfig().set("void-locations." + worldName + ".z", location.getZ());
                saveConfig();

                player.sendMessage("\u00a7aVoid location set successfully for world: " + worldName);
                return true;
            }
        }
        return false;
    }


}

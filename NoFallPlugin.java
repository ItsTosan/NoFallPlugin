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

    // Map to store spawn locations with custom names for each world
    private Map<String, Location> spawnLocations = new HashMap<>();
    private double voidYLevel;

    @Override
    public void onEnable() {
        // Save default config and load the config values
        saveDefaultConfig();
        loadConfigValues();

        // Register events and commands
        Bukkit.getPluginManager().registerEvents(this, this);
        this.getCommand("voidLocation").setExecutor(this);
    }

    // Load config values
    private void loadConfigValues() {
        voidYLevel = getConfig().getDouble("void-y-level", 0);

        if (getConfig().contains("spawn-locations")) {
            for (String key : getConfig().getConfigurationSection("spawn-locations").getKeys(false)) {
                String worldName = getConfig().getString("spawn-locations." + key + ".world");
                double x = getConfig().getDouble("spawn-locations." + key + ".x");
                double y = getConfig().getDouble("spawn-locations." + key + ".y");
                double z = getConfig().getDouble("spawn-locations." + key + ".z");

                World world = Bukkit.getWorld(worldName);
                if (world != null) {
                    spawnLocations.put(key, new Location(world, x, y, z));
                }
            }
        }
    }

    // Event to check if the player falls below the specified Y-level
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        String worldName = player.getWorld().getName();

        if (player.getLocation().getY() < voidYLevel) {
            for (Map.Entry<String, Location> entry : spawnLocations.entrySet()) {
                if (entry.getValue().getWorld().getName().equals(worldName)) {
                    player.teleport(entry.getValue());
                    return;
                }
            }
        }
    }

    // Command to set, remove, or list void locations
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("voidLocation")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("This command can only be run by a player.");
                return true;
            }

            Player player = (Player) sender;

            // Command: /voidLocation <spawnName>
            if (args.length == 1) {
                String spawnName = args[0];
                Location location = player.getLocation();

                spawnLocations.put(spawnName, location);

                getConfig().set("spawn-locations." + spawnName + ".world", location.getWorld().getName());
                getConfig().set("spawn-locations." + spawnName + ".x", location.getX());
                getConfig().set("spawn-locations." + spawnName + ".y", location.getY());
                getConfig().set("spawn-locations." + spawnName + ".z", location.getZ());
                saveConfig();

                player.sendMessage("\u00a7aVoid location \"" + spawnName + "\" set successfully for world: " + location.getWorld().getName());
                return true;
            }

            // Command: /voidLocation remove <spawnName>
            if (args.length == 2 && args[0].equalsIgnoreCase("remove")) {
                String spawnName = args[1];

                if (spawnLocations.containsKey(spawnName)) {
                    spawnLocations.remove(spawnName);
                    getConfig().set("spawn-locations." + spawnName, null);
                    saveConfig();

                    player.sendMessage("\u00a7aVoid location \"" + spawnName + "\" removed successfully.");
                } else {
                    player.sendMessage("\u00a7cNo void location found with the name: " + spawnName);
                }
                return true;
            }

            // Command: /voidLocation list
            if (args.length == 1 && args[0].equalsIgnoreCase("list")) {
                if (spawnLocations.isEmpty()) {
                    player.sendMessage("\u00a7cNo void locations have been set.");
                } else {
                    player.sendMessage("\u00a7aVoid Locations:");
                    for (String spawnName : spawnLocations.keySet()) {
                        player.sendMessage("\u00a7e- " + spawnName);
                    }
                }
                return true;
            }

            // Invalid usage
            player.sendMessage("\u00a7cUsage:");
            player.sendMessage("\u00a7e/voidLocation <spawnName>");
            player.sendMessage("\u00a7e/voidLocation remove <spawnName>");
            player.sendMessage("\u00a7e/voidLocation list");
            return true;
        }
        return false;
    }
}

# NoFallPlugin

A Minecraft Spigot plugin that prevents players from taking fall damage and teleports them to predefined spawn points if they fall below a certain Y-level. Supports multiple worlds and customizable spawn locations.

## Features

- **No Fall Damage**: Automatically cancels fall damage for players.
- **Void Teleport**: Teleports players back to a configured spawn point when they fall below a specified Y-level.
- **Multiple Worlds Support**: Set unique spawn points for different worlds, including worlds created with plugins like Multiverse.
- **Configurable**: Easily set and remove spawn locations using in-game commands.
- **List Spawn Locations**: See all configured void locations.

## Commands

### Set a Void Location

Set the spawn location for the current world.

```plaintext
/voidLocation <spawnName>
```

**Example**:  
```plaintext
/voidLocation HubSpawn
```

### Remove a Void Location

Remove a previously set void location.

```plaintext
/voidLocation remove <spawnName>
```

**Example**:  
```plaintext
/voidLocation remove HubSpawn
```

### List All Void Locations

List all configured void locations.

```plaintext
/voidLocation list
```

## Installation

1. **Download the Plugin**:  
   Download the latest version of `NoFallPlugin.jar` from the [Releases](#) page.

2. **Place in Plugins Folder**:  
   Copy the `.jar` file into your server's `plugins` folder.

3. **Restart the Server**:  
   Restart your server or use the `/reload` command.

## Configuration

After running the plugin for the first time, a `config.yml` file will be created in the `plugins/NoFallPlugin` directory.

### Sample `config.yml`

```yaml
void-y-level: 0

spawn-locations:
  HubSpawn:
    world: Hub
    x: 0.0
    y: 64.0
    z: 0.0
  worldSpawn:
    world: world
    x: 100.0
    y: 80.0
    z: 100.0
```

- **`void-y-level`**: The Y-level below which players are teleported to the nearest spawn location.
- **`spawn-locations`**: A list of configured spawn locations with coordinates and world names.

## Permissions

| Permission           | Description                          |
|----------------------|--------------------------------------|
| `nofallplugin.use`   | Allows the use of all plugin commands. |

## Credits

This plugin was developed by **Tosan**.

### Enjoy No More Falling Damage! 🚀



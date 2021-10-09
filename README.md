# Halloween Heist plugin
#### _Heists are dumb_

This Halloween Heist plugin creates a minigame based on the tv series Brooklyn Nine-Nine. The main goal of the minigame is to possess the Medal of Valor at exactly midnight.  The player that has the item at midnight will be titled an amazing player / genius.  
The item will be randomly spawned in the world in a chest. At 13.00 the location of the item will be sent in the chat and updated on the sidebar. But this location is not exactly accurate, the item is in 10 blocks of these coordinates. From then on, the location of the item will be sent in the chat every hour, but that still won't be the exact location.  
In order to possess the Medal of Valor at the end of the day, players can survival to get resources and create plans to capture the Medal. If you die or disconnect from the server, the Medal will be dropped.  
And the time will correspond with the time in real life, the later it gets, more zombies will spawn...
To speed up the game, you can claim a kit with /kit, this can only be used once every life.  

####It's heist time!

## Requirements

Halloween Heist is created for Spigot-based servers running Minecraft version 1.16.5. Other versions are not tested and behaviour can be unpredictable.
Before installation of the plugin, make sure you have your server properly set up. For a guide to set up a Spigot server, see [Spigot Installation - A Guide](https://www.spigotmc.org/wiki/spigot-installation/).

## Installation

Once you have your server properly installed, download the latest version of the Halloween Heist plugin's .jar file. Upload this file to your server's `plugins` directory. If you now (re)start your server, two files are created in the directory `plugins/HalloweenHeistPlugin`:
- `config.yml`  
  This file contains the configuration for you plugin. For more info, see Configuration.
- `heistState.yml`  
  This file contains the current state of the Heist. Do not modify this file, unless you really know what you're doing.

## Configuration
- `worldName`  
  *Default `99th_Precinct`*  
  The name of the Minecraft world you will be playing at. This needs to match `level-name` in your `server.properties`.
- `timezone`  
  *Default `Europe/Amsterdam`*  
  The name of the timezone you will be playing at.
- `worldDimensions`  
  *Default `500`*  
  The dimensions of the gamearea, assuming a center point of (0,0). The Heist Object will spawn somewhere between `-worldDimensions` and `worldDimensions` in both X and Z direction.
- `itemOffset`  
  *Default `25`*  
  The offset from (0,0) in which the Heist Object will not spawn.
- `gameStart`  
  *Default `'2021-10-31T00:00:00'`*  
  The start time of the game, after which the approximate location will be announced every hour. Note that this value has to be surrounded by quotes and be in ISO-8601 format.
- `gameEnd`  
  *Default `'2021-11-01T00:00:00'`*  
  The end time of the game, at which the winner will be announced. Note that this value has to be surrounded by quotes and be in ISO-8601 format.
- `realTimeCycle`  
  Lets the minecraft day/night cycle correspond to your real life day/night cycle.  
  -- `active`  
  *Default `true`*  
  Whether RealTimeCycle is activated  
  -- `sunrise`  
  *Default `7:30`*  
  Real life time of desired sunrise.  
  -- `sunset`  
  *Default `17:07`*  
  Real life time of desired sunset.
- `allowEnderChest`  
  *Default `false`*  
  Whether enderchests are allowed. If `false`, placed enderchests will be converted to regular chests.
- `season`  
  *Default `1`*  
  Which Brooklyn Nine-Nine season's Halloween Heist you will be playing. Currently supported: `1`.
- `kit`  
  The `/kit` command gives a basis kit to start with. Can be used once every player's lifetime, with a configurable cooldown.  
  -- `enabled`  
  *Default `true`*  
  Whether the `/kit` command can be used  
  -- `cooldown`  
  *Default `300`*  
  Cooldown of the `/kit` command in seconds.  
- `antiGriefing`  
  *Default `20`*  
  The distance from (0,0) which will be protected against griefing.

## Development
Want to contribute? Great!

You are always welcome to submit issues and/or pull requests for further improvements!

## License

MIT
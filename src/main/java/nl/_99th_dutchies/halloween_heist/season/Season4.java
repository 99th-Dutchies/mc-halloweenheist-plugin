package nl._99th_dutchies.halloween_heist.season;

import nl._99th_dutchies.halloween_heist.HalloweenHeistPlugin;
import nl._99th_dutchies.halloween_heist.team.Team;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.text.MessageFormat;
import java.util.UUID;

public class Season4 extends ASeason {
    public Season4(HalloweenHeistPlugin plugin) {
        super(plugin);
    }

    @Override
    public Material getHeistObjectSpawncontainer() {
        return Material.TRAPPED_CHEST;
    }

    @Override
    public Material getHeistObjectMaterial() {
        return Material.CRIMSON_SIGN;
    }

    @Override
    public String getHeistObjectName() {
        return MessageFormat.format("{0}{1}The Plaque{2}", ChatColor.GOLD, ChatColor.BOLD, ChatColor.RESET);
    }

    @Override
    public void spawnHeistObject(boolean isRespawn) {
        Location dropLocation = this.generateLocation();

        if (!isRespawn) {
            dropLocation = new Location(this.plugin.mainWorld, -10, 71, 27);
        }

        // Get container
        Block dropBlock = dropLocation.getBlock();
        dropBlock.setType(this.getHeistObjectSpawncontainer());
        Chest dropChest = (Chest)dropBlock.getState();
        dropChest.setCustomName("Caboodle");
        Inventory dropChestInventory = dropChest.getBlockInventory();

        // Generate item stack
        ItemStack dropItemStack = new ItemStack(this.getHeistObjectMaterial(), 1);
        ItemMeta dropItemMeta = dropItemStack.getItemMeta();
        dropItemMeta.getPersistentDataContainer().set(
                new NamespacedKey(plugin, "isHeistObject"),
                PersistentDataType.INTEGER,
                1
        );
        dropItemMeta.addAttributeModifier(
                Attribute.GENERIC_MAX_HEALTH,
                new AttributeModifier(
                        UUID.randomUUID(),
                        "Plaque Boost",
                        4.0,
                        AttributeModifier.Operation.ADD_NUMBER,
                        EquipmentSlot.HAND
                )
        );
        dropItemMeta.addAttributeModifier(
                Attribute.GENERIC_MAX_HEALTH,
                new AttributeModifier(
                        UUID.randomUUID(),
                        "Plaque Boost",
                        4.0,
                        AttributeModifier.Operation.ADD_NUMBER,
                        EquipmentSlot.OFF_HAND
                )
        );
        dropItemMeta.setDisplayName(this.getHeistObjectName());
        dropItemStack.setItemMeta(dropItemMeta);

        // Place item stack in container
        dropChestInventory.setItem(13, dropItemStack);

        // Update heistState
        this.heistObjectSpawned(dropChest.getBlockInventory().getHolder(), dropLocation);
    }

    @Override
    public void sendWinnerMessage(Player winner) {
        if(winner == null) {
            super.sendWinnerMessage(null);
            return;
        }

        Team winningTeam = this.plugin.teamManager.getTeamForPlayer(winner);

        if (winningTeam == null) {
            super.sendWinnerMessage(winner);
            return;
        }

        int count = 0;
        StringBuilder winners = new StringBuilder();
        for (Player player : winningTeam.getPlayers()) {
            if (count == 0) {
                winners.append(player.getDisplayName());
            } else if (count == winningTeam.getPlayers().size() - 1) {
                winners.append(" and ").append(player.getDisplayName());
            } else {
                winners.append(", ").append(player.getDisplayName());
            }

            count++;
        }

        for(Player p : this.plugin.getServer().getOnlinePlayers()) {
            p.sendTitle(
                    MessageFormat.format("{0}Happy Halloween!", ChatColor.GOLD),
                    MessageFormat.format("{0}{1}{2} are amazing players / geniuses.", ChatColor.BLUE, ChatColor.BOLD, winners.toString()),
                    10,
                    100,
                    20);
        }

        Bukkit.broadcastMessage(MessageFormat.format("{0}{1}ATTENTION, EVERYONE!", ChatColor.BLUE, ChatColor.BOLD));
        Bukkit.broadcastMessage(MessageFormat.format("{0}{1}{2} are amazing players / geniuses.", ChatColor.BLUE, ChatColor.BOLD, winners.toString()));
    }
}

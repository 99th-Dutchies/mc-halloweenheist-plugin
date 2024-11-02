package nl._99th_dutchies.halloween_heist.season;

import nl._99th_dutchies.halloween_heist.HalloweenHeistPlugin;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Pillager;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.text.MessageFormat;

public class Season2 extends ASeason {
    public Season2(HalloweenHeistPlugin plugin) {
        super(plugin);
    }

    @Override
    public EntityType getHeistObjectSpawnentity() {
        return EntityType.PILLAGER;
    }

    @Override
    public Material getHeistObjectMaterial() {
        return Material.CLOCK;
    }

    @Override
    public String getHeistObjectName() {
        return MessageFormat.format("{0}{1}Captain Holts Watch{2}", ChatColor.DARK_GREEN, ChatColor.BOLD, ChatColor.RESET);
    }

    @Override
    public void spawnHeistObject(boolean isRespawn) {
        Location dropLocation = this.generateLocation(false, true);

        // Generate entity
        Pillager entity = (Pillager) this.plugin.mainWorld.spawnEntity(dropLocation, this.getHeistObjectSpawnentity());
        entity.setCustomName("Captain Holt");
        entity.setCustomNameVisible(true);
        entity.setPersistent(true);
        entity.setRemoveWhenFarAway(false);
        System.out.println("HeistObject Entity has UUID " + entity.getUniqueId());

        // Generate item stack
        ItemStack dropItemStack = new ItemStack(this.getHeistObjectMaterial(), 1);
        ItemMeta dropItemMeta = dropItemStack.getItemMeta();
        dropItemMeta.getPersistentDataContainer().set(
                new NamespacedKey(plugin, "isHeistObject"),
                PersistentDataType.INTEGER,
                1
        );
        dropItemMeta.setDisplayName(this.getHeistObjectName());
        dropItemStack.setItemMeta(dropItemMeta);

        // Place item stack in entityEquipment
        EntityEquipment entityEquipment = entity.getEquipment();
        entityEquipment.setItemInMainHandDropChance(1.0F);
        entityEquipment.setItemInMainHand(dropItemStack, true);

        // Update heistState
        this.heistObjectSpawned(entity, dropLocation);
    }
}

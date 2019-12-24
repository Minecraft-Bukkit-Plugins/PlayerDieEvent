package me.franco28.playereventdie;

import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class die extends JavaPlugin implements Listener {

	public void onEnable() {
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Plugin Cofre Activado!");
		getServer().getPluginManager().registerEvents(this, this);
	}
    
	public void onDisable() {
    	getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Plugin Cofre Desactivado!");
    }

	@EventHandler(priority = EventPriority.HIGH)
    public void PlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        int x = player.getLocation().getBlockX();
        int y = player.getLocation().getBlockY();
        int z = player.getLocation().getBlockZ();
        World w = player.getWorld();
        Location loc = new Location(w, x, y, z);
        loc.getBlock().setType(Material.CHEST);
        ItemStack[] drops = toItemStack(event.getDrops());
        Chest c = (Chest) loc.getBlock().getState();
        c.getBlockInventory().setContents((ItemStack[])drops);
        c.setLock(player.getName());        
        player.sendMessage(ChatColor.RED + "Se coloco un cofre con tus objetos en: " + "X: " + x + " Y: " + y + " Z: " + z);
        player.sendMessage(ChatColor.RED + "Apurate! Cualquier jugador puede robarte tus objetos!");
        
        int count = 0;
         
        for(ItemStack i : drops) {
        if (i.getItemMeta() == null) {
        count++;
        }      
        event.getDrops().remove(count);
        }
    }
    
    public ItemStack[] toItemStack(List<ItemStack> list) {
    	ItemStack[] items = new ItemStack[list.size()];
    	int index = 0;
    	for (ItemStack i : list) {
    	items[index] = i;
    	index++;
    	}
		return items;		
    	}
    
    public void onPlayerKilled(PlayerDeathEvent event) {
	    Player p = event.getEntity();

	   if(p.isDead()) {
            p.getKiller();
            if(p.getKiller() instanceof Player) {
                p.sendMessage(ChatColor.RED + "Mataste a " + p.getName() + "!");
			    Bukkit.broadcastMessage(ChatColor.RED + p.getName() + "Murió por " + p.getKiller() );
            }
        }
    }
           
    @EventHandler(priority=EventPriority.LOW)
    public void onPlayerDeath(PlayerDeathEvent event)
    {
        Player player = event.getEntity();
        player.getLocation().getWorld().playEffect(player.getLocation(), Effect.SMOKE, 1);
    }
}
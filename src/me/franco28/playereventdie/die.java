package me.franco28.playereventdie;

import java.util.Arrays;
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
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
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
        
        int count = 0;
         
        for(ItemStack i : drops) {
        if (i.getItemMeta() == null) {
        count++;
        }      
        event.getDrops().remove(count);
        }        
        
 	   if(player.isDead()) {
 		  player.getKiller();
           if(player.getKiller() instanceof Player) {
               player.sendMessage(ChatColor.RED + "Mataste a " + player.getName() + "!");
			    Bukkit.broadcastMessage(ChatColor.RED + player.getName() + "Murió por " + player.getKiller() );
           }
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

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = (Player) event.getPlayer();      
        int x = player.getLocation().getBlockX();
        int y = player.getLocation().getBlockY();
        int z = player.getLocation().getBlockZ();
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta bm = (BookMeta) book.getItemMeta();
        bm.setPages(Arrays.asList(player.getName() + ". Has muerto, por lo cual tus ultimos objetos estan aqui: " + "X: " + x + " Y: " + y + " Z: " + z + ". Apresurate! ya que tu cofre puede ser robado :( "));
      bm.setAuthor("Server by TatinSystem & sims03");
      bm.setTitle("Coordenadas de tu ultima muerte!");
      book.setItemMeta(bm);
      player.getInventory().addItem(book);
    }
               
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDeath(PlayerDeathEvent event)
    {
        Player player = event.getEntity();
        player.getLocation().getWorld().playEffect(player.getLocation(), Effect.SMOKE, 1);
    }
}
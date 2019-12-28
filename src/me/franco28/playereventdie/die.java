package me.franco28.playereventdie;

import java.util.ArrayList;
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
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.fusesource.jansi.Ansi;

public class die extends JavaPlugin implements Listener {

    ArrayList<String> chesty = new ArrayList<String>();
    List<String> chests = getConfig().getStringList("chests");
    final static List<Material> types = Arrays.asList(Material.CHEST, Material.DISPENSER, Material.TRAPPED_CHEST, Material.FURNACE,
            Material.DROPPER, Material.ENCHANTING_TABLE, Material.HOPPER);
    
	public void onEnable() {
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Plugin Cofre Activado!");
		getServer().getPluginManager().registerEvents(this, this);
		
	        getConfig().set("chests", chests);
	        saveConfig();
	        for (String str : chests) {
	            String[] words = str.split(",");
	            String aWorld = words[0];
	            String aX = words[3];
	            double aX1 = Double.parseDouble(aX);
	            String aY = words[4];
	            double aY1 = Double.parseDouble(aY);
	            String aZ = words[5];
	            double aZ1 = Double.parseDouble(aZ);
	            Location chestLoc = new Location (Bukkit.getWorld(aWorld), aX1, aY1, aZ1);
	           
	            if (chestLoc.getBlock().getType() != Material.CHEST) {
	                chestLoc.getBlock().setType(Material.CHEST);
	                   ItemStack lc = new ItemStack(Material.CHEST);
	                   ItemMeta meta = lc.getItemMeta();
	                   meta.setDisplayName(ChatColor.GOLD + ("Cofre de la Muerte"));
	                   lc.setItemMeta(meta);
	              
	            } else {
	                getLogger().info(Ansi.ansi().fg(Ansi.Color.YELLOW) + "Cofre de la Muerte ya esta en la misma ubicacion " + aX + "," + aY + "," + aZ + "!" + Ansi.ansi().fg(Ansi.Color.DEFAULT));
	            }
	        }
	}
    
	public void onDisable() {
    	getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Plugin Cofre Desactivado!");
    
    	List<String> chests = getConfig().getStringList("chests");
        chesty.addAll(chests);
        getConfig().set("chests", chesty);
        saveConfig();
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
        c.setCustomName(ChatColor.GOLD + "" + ChatColor.BOLD + "Cofre de Muerte de: " + player.getName());

        int count = 0;
       
        for(ItemStack i : drops) {
        if (i.getItemMeta() == null) {
        count++;
        }      
        event.getDrops().remove(count);
        }        
         	   
       player.getLocation().getWorld().playEffect(player.getLocation(), Effect.SMOKE, 12);
       player.getLocation().getWorld().playEffect(player.getLocation(), Effect.POTION_BREAK, 12);
       
       event.setKeepLevel(true);
       event.setDroppedExp(0); 
       player.setTotalExperience(player.getTotalExperience() - (int)((player.getTotalExperience() - event.getNewTotalExp())* 0.75));
	
       ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
       BookMeta bm = (BookMeta) book.getItemMeta();
       bm.setPages(Arrays.asList("Este libro se a encontrado dentro el cofre del jugador " + player.getName() + " por lo cual, si los objectos de este cofre no estan, reportar a un admin." ));
       bm.setAuthor(player.getName());
       bm.setTitle("Este cofre pertenece a: " + player.getName());
       book.setItemMeta(bm);
       c.getInventory().addItem(book);
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
        ItemStack apple = new ItemStack(Material.ENCHANTED_GOLDEN_APPLE);
        BookMeta bm = (BookMeta) book.getItemMeta();
        bm.setPages(Arrays.asList(player.getName() + ". Has muerto, por lo cual tus ultimos objetos estan aqui: " + "X: " + x + " Y: " + y + " Z: " + z + ". Apresurate! ya que tu cofre puede ser robado :( ", "El server te regala una Manzana Dorada Encantada!" + " PD: El Server"));
        bm.setAuthor("Server");
        bm.setTitle("Coordenadas de tu ultima muerte!");
        book.setItemMeta(bm);
        player.getInventory().addItem(book);
        player.getInventory().addItem(apple);
        player.sendMessage(ChatColor.BOLD + "Bienvenido de la muerte! "+ player.getName());
    }    
}
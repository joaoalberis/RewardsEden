package com.redeeden.commands;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.server.v1_7_R4.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.redeeden.utils.Api;
import com.redeeden.utils.ENV;

public class Reward implements CommandExecutor, Listener {
    private final Map<Player, Long> lastClaimTimes = new HashMap<>();
    private final int timeExpired = Api.getConfig("Config.yml", "RewardsEden", "timeExpired");
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ENV.PREFIX + ChatColor.RED + "Este comando só pode ser executado por jogadores.");
            return true;
        }

        Player player = (Player) sender;

        LocalDateTime now = LocalDateTime.now();

        String timeExpiredString = Api.getConfigs("data.yml", "RewardsEden", player.getName());
        LocalDateTime timeExpired = LocalDateTime.parse(timeExpiredString, formatter);

        Duration duracao = Duration.between(timeExpired, now);

        if (duracao.toMinutes() >= 1){
            player.sendMessage(ENV.PREFIX + ChatColor.RED + "ja passou 1 minuto");
        }else {
            player.sendMessage(ENV.PREFIX + ChatColor.RED + "não passou!");
            return true;
        }

        Inventory inv = createRewardInventory();
        player.openInventory(inv);
        player.sendMessage(ENV.PREFIX + ChatColor.GREEN + "Você abriu o menu de recompensas!");

        return true;
    }

    private Inventory createRewardInventory() {
        Inventory inv = Bukkit.createInventory(null, 9, "§6Recompensas");

        ItemStack resgatarItem = new ItemStack(Material.EMERALD);
        ItemStack recusarItem = new ItemStack(Material.REDSTONE);
        ItemMeta resgatarMeta = resgatarItem.getItemMeta();
        ItemMeta recusarMeta = recusarItem.getItemMeta();

        resgatarMeta.setDisplayName(ChatColor.GREEN + "Resgatar");
        recusarMeta.setDisplayName(ChatColor.DARK_RED + "Recusar");

        resgatarItem.setItemMeta(resgatarMeta);
        recusarItem.setItemMeta(recusarMeta);

        inv.setItem(3, resgatarItem);
        inv.setItem(5, recusarItem);

        return inv;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        Integer tp = Api.getConfig("Config.yml", "RewardsEden", "tp");
        Integer money = Api.getConfig("Config.yml", "RewardsEden", "money");

        String fragmentsTitle = Api.getConfigs("Config.yml", "RewardsEden", "fragmentsTitle");
        String fragmentsLore = Api.getConfigs("Config.yml", "RewardsEden", "fragmentsLore");
        List<String> loreList = new ArrayList<>();
        Integer fragmentsID = Api.getConfig("Config.yml", "RewardsEden", "fragmentsID");
        Integer fragmentsQnt = Api.getConfig("Config.yml", "RewardsEden", "fragmentsQnt");

        if (e.getInventory().getTitle().equalsIgnoreCase("§6Recompensas")) {
            if (e.getCurrentItem().hasItemMeta()) {
                String nome = e.getCurrentItem().getItemMeta().getDisplayName();
                if (nome.equalsIgnoreCase(ChatColor.GREEN + "Resgatar")) {
                    ENV.COMANDO(Bukkit.getConsoleSender(), "jrmctp " + tp + " " + p.getName());
                    ENV.COMANDO(Bukkit.getConsoleSender(), "eco give " + p.getName() + " " + money);

                    ItemStack fragmentsItem = new ItemStack(Material.getMaterial(fragmentsID));
                    ItemMeta fragmentsMeta = fragmentsItem.getItemMeta();

                    fragmentsMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', fragmentsTitle));
                    loreList.add(ChatColor.translateAlternateColorCodes('&', fragmentsLore));
                    fragmentsMeta.setLore(loreList);

                    fragmentsItem.setItemMeta(fragmentsMeta);
                    fragmentsItem.setAmount(fragmentsQnt);
                    p.getInventory().addItem(fragmentsItem);

                    Api.setConfig("data.yml", "RewardsEden", p.getName(), LocalDateTime.now().toString());

                    p.closeInventory();
                } else if (nome.equalsIgnoreCase(ChatColor.DARK_RED + "Recusar")) {
                    p.closeInventory();
                }
                p.closeInventory();
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (!p.hasPlayedBefore()){
            if (!Api.isExist("data.yml", "RewardsEden", p.getName())){
                String date = LocalDateTime.now().minusDays(1).format(formatter);
                Api.createConfig("data.yml", "RewardsEden", p.getName());
                Api.setConfig("data.yml", "RewardsEden", p.getName(), date);
            }
        }
    }
}

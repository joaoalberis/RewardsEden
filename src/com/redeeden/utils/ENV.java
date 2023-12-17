package com.redeeden.utils;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public abstract class ENV {
    public static String PREFIX = "§l§6DBC§f§lEden §7| ";
    public static void COMANDO(CommandSender sender, String comando){
        Bukkit.dispatchCommand(sender, comando);
    }
}

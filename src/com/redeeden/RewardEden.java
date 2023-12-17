package com.redeeden;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.redeeden.commands.Reward;
import com.redeeden.utils.ENV;

public class RewardEden extends JavaPlugin {
    private File configFile;
    private FileConfiguration config;

    @Override
    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage("");
        Bukkit.getConsoleSender().sendMessage(ENV.PREFIX + "§a[RewardsEden] iniciado com sucesso!");
        Bukkit.getConsoleSender().sendMessage("");

        createConfig("config.yml");
        createConfig("data.yml");
        getCommand("reward").setExecutor(new Reward());
        getServer().getPluginManager().registerEvents(new Reward(), this);
    }

    public void createConfig(String archiveName) {
        configFile = new File(getDataFolder(), archiveName);
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            saveResource(archiveName, false);
        }
        config = new YamlConfiguration();
        try {
            config.load(configFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage("");
        Bukkit.getConsoleSender().sendMessage(ENV.PREFIX + "§4[RewardsEden] desligando!");
        Bukkit.getConsoleSender().sendMessage("");
    }
}

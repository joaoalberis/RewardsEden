package com.redeeden.utils;


import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Api extends JavaPlugin {
    public static int getConfig(String arquivos, String pasta, String option){
        File config = new File("plugins/"+pasta+"/"+arquivos);
        YamlConfiguration configA = YamlConfiguration.loadConfiguration(config);
        return configA.getInt(option);
    }

    public static String getConfigs(String arquivos, String pasta, String option){
        File config = new File("plugins/"+pasta+"/"+arquivos);
        YamlConfiguration configA = YamlConfiguration.loadConfiguration(config);
        return configA.getString(option);
    }

    public static void setConfig(String arquivo, String pasta, String option, String value){
        File config = new File("plugins/"+pasta+"/"+arquivo);
        YamlConfiguration configA = YamlConfiguration.loadConfiguration(config);

        configA.set(option, value);
        try {
            configA.save(config);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isExist(String arquivo, String pasta, String option){
        File config = new File("plugins/"+pasta+"/"+arquivo);
        YamlConfiguration configA = YamlConfiguration.loadConfiguration(config);

        return configA.contains(option);
    }

    public static  void createConfig(String arquivo, String pasta, String option){
        File config = new File("plugins/"+pasta+"/"+arquivo);
        YamlConfiguration configA = YamlConfiguration.loadConfiguration(config);

        configA.createSection(option);
    }
}


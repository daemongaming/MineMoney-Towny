package com.kraken.minemoneytowny;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.kraken.minemoney.MineMoney;

public class MineMoneyTowny extends JavaPlugin {

	private static MineMoneyTowny plugin;
	private MineMoney mm;
	private static String PLUGIN_NAME = "MineMoney-Towny";
	
    @Override
    public void onEnable() {
    	
    	//Plugin start-up
    	plugin = this;
    	
		//Copies the default config.yml from within the .jar if "plugins/<name>/config.yml" does not exist
		getConfig().options().copyDefaults(true);
		saveConfig();
    	
        //Get the main plugin instance of MineMoney
    	this.mm = (MineMoney) Bukkit.getPluginManager().getPlugin("MineMoney");
    	
    }
    
    @Override
    public void onDisable() {
        getLogger().info("MineMoney-Towny has been stopped.");  
    }
    
    //Command handling
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		return new Commands(this).onCommand(sender, cmd, label, args);
    }
	
	//Get a FileConfiguration from file name
	public FileConfiguration getFileConfig(String fileName) {
	    File f = new File("plugins/" + PLUGIN_NAME, fileName + ".yml");
	    FileConfiguration fc = YamlConfiguration.loadConfiguration(f);
	    return fc;
	}
	
	//Get the MineMoney plugin instance
	public MineMoney getMainPlugin() {
		return mm;
	}
	
	//Get the MineMoneyTowny plugin instance
	public MineMoneyTowny getPlugin() {
		return plugin;
	}
	
	//Get this plugin's name
	public String getPluginName() {
		return PLUGIN_NAME;
	}
    
}

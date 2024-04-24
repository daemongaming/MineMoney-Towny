package com.kraken.minemoneytowny;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.kraken.minemoney.MineMoney;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Town;

import org.bukkit.configuration.file.FileConfiguration;

public class Commands {
	
	//Get the main instance
	private MineMoneyTowny plugin;
	
	//Constructor
	public Commands(MineMoneyTowny plugin) {
		this.plugin = plugin;
	}
	
    //Commands
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		String command = cmd.getName();
    	Player player = Bukkit.getServer().getPlayerExact("krakenmyboy");
		
		//Player commands
        if (!(sender instanceof Player)) {
        	//Player-only command message
        	return true;
        }

        //MineMoney info
        MineMoney mm = plugin.getMainPlugin();
		FileConfiguration balConfig = mm.getFileConfig("balance");
		
		//Player info
		String pId = player.getUniqueId().toString();
    	player = (Player) sender;
		
		switch ( command.toLowerCase() ) {
		
			case "mmt":
					
				if (args.length == 2) {
					
					switch(args[0]) {
					
						//Buy town plots w/ balance
						case "buy":
							
							//Try to get the number of plots to buy
							int bonuses = 0;
							
							try {
								bonuses = Integer.parseInt(args[1]);
							} catch (NumberFormatException e) {
								//Command format error
								player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6[$M] &7| Command format error. Try entering \"/mmt buy <#>\""));
								e.printStackTrace();
								break;
							}
							
							//Hook into Towny
							Town town = TownyAPI.getInstance().getTown(player);

							//Town not found error
							if (town == null) {
								player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6[$M] &7| Town not found, or you are not part of a town."));
								break;
							}
							
							//Get the town size for the town size multiplier
							int townSize = town.getMaxTownBlocks();
							
							//Check if player has enough balance to buy
							Integer plots = plugin.getConfig().getInt("plots_per_bonus");
							Double bal = balConfig.getDouble(pId + ".balance");
							Double cost = plugin.getConfig().getDouble("cost_per_bonus");
							Double multiplier = plugin.getConfig().getDouble("town_size_multiplier");
							Double totalCost = 0.0;
							for (int b=0; b<bonuses; b++) {
								totalCost += cost + multiplier * townSize;
								townSize++;
							}
							String currencySymbol = mm.getConfig().getString("currency_symbol");
							boolean enough = bal >= totalCost;
							
							//Not enough balance error
							if (!enough) {
								player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6[$M] &7| You do not have enough balance to buy " + bonuses + 
																							" bonuses at the cost of " + currencySymbol + totalCost));
								break;
							}
								
							//Charge the player
							mm.getEconomy().add(player, (totalCost*-1));
							
							//Add Towny bonus blocks
							town.addBonusBlocks(bonuses*plots);
							
							//Send confirmation message
							player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6[$M] &7| You bought " + bonuses + " claim bonuses for " + 
																						currencySymbol + totalCost + ", awarding " + bonuses*plots + " Towny plot claims."));
								
							break;
							
						default:
							//Command not recognized message
							player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYour command was not recognized, or you have insufficient permissions."));
							break;
					
					}
				
				} else {
					//Command not recognized message
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYour command was not recognized, or you have insufficient permissions."));
				}
				
				return true;
				
			default:	
				//Command not recognized message
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYour command was not recognized, or you have insufficient permissions."));
				return true;
				
		}
        
    }
	
}
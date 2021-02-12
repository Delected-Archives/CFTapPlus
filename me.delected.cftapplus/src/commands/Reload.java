package commands;

import Storage.Cards;
import Storage.Companies;
import main.CFTapPlus;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Reload implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player){
            if (!sender.hasPermission("cftp.reload")){
                sender.sendMessage(ChatColor.RED + "You do not have permission to reload CFTP!" + ChatColor.ITALIC + " (made by Delected with <3 for Clifton City)");
                return false;
            }
            CFTapPlus.getPlugin().reloadConfig();
            Cards.reload();
            Companies.reload();
            sender.sendMessage(ChatColor.GREEN + "Successfully reloaded CFTP!");
            return false;
        }
        return false;
    }
}

package commands;

import Storage.Companies;
import main.CFTapPlus;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class CompaniesManager implements CommandExecutor {
    JavaPlugin plugin = CFTapPlus.getPlugin();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // Check if it's player or console first || PERMS
        String cmdSender;
        if (sender instanceof Player) {
            // is a player
            if (!sender.hasPermission("cftp.companies")) {
                sender.sendMessage(ChatColor.RED + "Sorry, but you do not have permission to run /company.");
                return false;
            }
            cmdSender = "player";
        } else {
            cmdSender = "console";
        }
        // We know who the sender is, and we know if they have perms or not. || ARGS MANAGER
        if (args.length == 0) {
            sender.sendMessage("A list of available sub-commands are: create, delete, setprice, getprice, and ban.");
            return false;
        }
        switch (args[0]) {
            case "create":
                if (cmdSender.equals("player")) {
                    // player
                    if (!sender.hasPermission("cftp.companies.create")) {
                        sender.sendMessage(ChatColor.RED + "Sorry, but you do not have permission to create a company.");
                        return false;
                    } else {
                        // player has perms
                        if (args.length != 2) {
                            sender.sendMessage(ChatColor.RED + "Incorrect Usage! Please use '/company create (name)'");
                            return false;
                        }
                        // args are correct
                        if (Companies.get().get("Companies." + args[1].toUpperCase()) != null) {
                            sender.sendMessage(ChatColor.RED + "This company already exists!");
                            return false;
                        }
                        Companies.get().set("Companies." + args[1].toUpperCase() + ".Owner", ((Player) sender).getUniqueId().toString());
                        Companies.save();
                        sender.sendMessage(ChatColor.GRAY + "You have successfully created a company called " + ChatColor.BLUE + args[1] + ChatColor.GRAY + ". Run /company for more info!");
                        return false;
                    }
                } else {
                    if (args.length != 2) {
                        sender.sendMessage("Incorrect Usage! Please use '/company create (name)'");
                        return false;
                    }
                    if (Companies.get().get("Companies." + args[1].toUpperCase()) != null) {
                        sender.sendMessage("This company already exists!");
                        return false;
                    }
                    Companies.get().set("Companies." + args[1].toUpperCase() + ".Owner", "console (please change this!)");
                    Companies.save();
                    sender.sendMessage("You have successfully created a company called " + args[1] + ". Since you created this company from console, its owned is undefined and must be edited through the companies.yml file.");
                    return false;

                }

            case "delete":
                if (cmdSender.equals("player")) {
                    if (!sender.hasPermission("cftp.companies.delete")) {
                        sender.sendMessage(ChatColor.RED + "Sorry, but you do not have permission to delete a company.");
                        return false;
                    }
                    if (args.length != 2) {
                        sender.sendMessage(ChatColor.RED + "Incorrect Usage! Please use '/company delete (name)'");
                        return false;
                    }
                    // args are correct
                    if (Companies.get().get("Companies." + args[1].toUpperCase()) == null) {
                        sender.sendMessage(ChatColor.RED + "This company doesn't exist!");
                        return false;
                    }
                    sender.sendMessage(ChatColor.GRAY + "You have successfully deleted " + ChatColor.BLUE + args[1] + ChatColor.GRAY + ".");
                    Companies.get().set("Companies." + args[1].toUpperCase(), null);
                    Companies.save();
                    return false;
                } else {
                    if (args.length != 2) {
                        sender.sendMessage("Incorrect Usage! Please use '/company delete (name)'");
                        return false;
                    }
                    // args are correct
                    if (Companies.get().get("Companies." + args[1].toUpperCase()) == null) {
                        sender.sendMessage("This company doesn't exist!");
                        return false;
                    }
                    sender.sendMessage("You have successfully deleted " + args[1] + ".");
                    Companies.get().set("Companies." + args[1].toUpperCase(), null);
                    Companies.save();
                    return false;
                }
            case "setprice":
                if (cmdSender.equals("player")) {
                    if (!sender.hasPermission("cftp.companies.setprice")) {
                        sender.sendMessage(ChatColor.RED + "Sorry, but you do not have permission to set the price of a company.");
                        return false;
                    }
                    if (args.length != 3) {
                        sender.sendMessage(ChatColor.RED + "Incorrect Usage! Please use '/company setprice (company name) (price)'");
                        return false;
                    }
                    if (Companies.get().get("Companies." + args[1].toUpperCase()) == null) {
                        sender.sendMessage(ChatColor.RED + "This company doesn't exist!");
                        return false;
                    }
                    // arg 2 is correct
                    try {
                        double dub = Double.parseDouble(args[2]);
                    } catch (NumberFormatException nfe) {
                        sender.sendMessage(ChatColor.RED + "The price is invalid. An example is '1000'.");
                        return false;
                    }
                    // arg 3 is correct
                    Companies.get().set("Companies." + args[1].toUpperCase() + ".Price", args[2]);
                    sender.sendMessage(ChatColor.GRAY + "You have successfully set the price of " + ChatColor.BLUE + args[1] + ChatColor.GRAY + " to: " + ChatColor.GREEN + args[2]);
                    Companies.save();
                    return false;
                } else {
                    // sender is console
                    if (args.length != 3) {
                        sender.sendMessage("Incorrect Usage! Please use '/company setprice (company name) (price)'");
                        return false;
                    }
                    if (Companies.get().get("Companies." + args[1].toUpperCase()) == null) {
                        sender.sendMessage("This company doesn't exist!");
                        return false;
                    }
                    // arg 2 is correct
                    try {
                        double dub = Double.parseDouble(args[2]);
                    } catch (NumberFormatException nfe) {
                        sender.sendMessage("The price is invalid. An example is '1000'.");
                        return false;
                    }
                    // arg 3 is correct
                    Companies.get().set("Companies." + args[1].toUpperCase() + ".Price", args[2]);
                    sender.sendMessage("You have successfully set the price of " + args[1] + " to: " + args[2]);
                    Companies.save();
                    return false;
                }
                // DONE !!! ^^^^
            case "getprice":
                if (cmdSender.equals("player")) {
                    if (!sender.hasPermission("cftp.companies.getprice")) {
                        sender.sendMessage(ChatColor.RED + "Sorry, but you do not have permission to get the price of a company.");
                        return false;
                    }
                    if (args.length != 2) {
                        sender.sendMessage(ChatColor.RED + "Incorrect Usage! Please use '/company getprice (company name)'");
                        return false;
                    }
                    if (Companies.get().get("Companies." + args[1].toUpperCase()) == null) {
                        sender.sendMessage(ChatColor.RED + "This company doesn't exist!");
                        return false;
                    }
                    Object price2 = Companies.get().get("Companies." + args[1].toUpperCase() + ".Price").toString();
                    sender.sendMessage(ChatColor.GRAY + "The price of this company is: " + ChatColor.GREEN + price2);
                    return false;
                } else {
                    // sender is console
                    if (args.length != 2) {
                        sender.sendMessage("Incorrect Usage! Please use '/company getprice (company name)'");
                        return false;
                    }
                    if (Companies.get().get("Companies." + args[1].toUpperCase()) == null) {
                        sender.sendMessage("This company doesn't exist!");
                        return false;
                    }
                    Object price2 = Companies.get().get("Companies." + args[1].toUpperCase() + ".Price").toString();
                    sender.sendMessage("The price of this company is: " + price2);
                    return false;
                }
            case "ban":
                // you can't do this until cards.json is setup, then, any person who taps in with the UUID saved here won't be given access.
                break;
            default:
                sender.sendMessage(ChatColor.RED + "Incorrect Usage! Type /company for a list of possible sub-commands.");
                break;
        }

        return false;
    }
}

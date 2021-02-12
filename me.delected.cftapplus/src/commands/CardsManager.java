package commands;

import Storage.Cards;
import Storage.Companies;
import main.CFTapPlus;
import net.minecraft.server.v1_15_R1.ChunkGeneratorAbstract;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class CardsManager implements CommandExecutor {
    JavaPlugin plugin = CFTapPlus.getPlugin();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // create, delete, companies that it can interact with, type of card (top up/permanent card, single use (tap in/out once and it's gone), Name and Lore in game, discount off the company (in percentage)
        String cmdSender;
        if (sender instanceof Player) {
            // is a player
            if (!sender.hasPermission("cftp.cards")) {
                sender.sendMessage(ChatColor.RED + "Sorry, but you do not have permission to run /card.");
                return false;
            }
            if (args.length == 0) {
                sender.sendMessage("A list of available sub-commands are: create, delete, addcompany, removecompany, setdisplayname, setdescription, adddiscount, removediscount, and listdiscounts.");
                return false;
            }
            cmdSender = "player";
        } else {
            cmdSender = "console";
        }
        switch (args[0]) {
            case "create":
                if (cmdSender.equals("player")) {
                    // player
                    if (!sender.hasPermission("cftp.cards.create")) {
                        sender.sendMessage(ChatColor.RED + "Sorry, but you do not have permission to create a card.");
                        return false;
                    } else {
                        // player has perms
                        if (args.length != 3) {
                            sender.sendMessage(ChatColor.RED + "Incorrect Usage! Please use '/card create (name) (type [single-use, top-up])'");
                            return false;
                        }
                        // args # is correct
                        if (Cards.get().get("Cards." + args[1].toUpperCase()) != null) {
                            sender.sendMessage(ChatColor.RED + "A card with this name already exists!");
                            return false;
                        }

                        if (args[2].equalsIgnoreCase("top-up")) {
                            Cards.get().set("Cards." + args[1].toUpperCase() + ".Type", "top-up");
                            Cards.save();
                            sender.sendMessage(ChatColor.GRAY + "You have successfully created a card called " + ChatColor.BLUE + args[1] + ChatColor.GRAY + " with the type 'top-up'. Run /card for more info!");
                            return false;
                        } else if (args[2].equalsIgnoreCase("single-use")) {
                            Cards.get().set("Cards." + args[1].toUpperCase() + ".Type", "single-use");
                            Cards.save();
                            sender.sendMessage(ChatColor.GRAY + "You have successfully created a card called " + ChatColor.BLUE + args[1] + ChatColor.GRAY + " with the type 'single-use'. Run /card for more info!");
                            return false;
                        } else {
                            sender.sendMessage(ChatColor.RED + "The type is incorrect! Please either use 'singe-use' or 'top-up'.");
                            return false;
                        }

                    }
                } else {
                    if (args.length != 3) {
                        sender.sendMessage("Incorrect Usage! Please use '/card create (name) (type [single-use, top-up])'");
                        return false;
                    }
                    // args # is correct
                    if (Cards.get().get("Cards." + args[1].toUpperCase()) != null) {
                        sender.sendMessage("A card with this name already exists!");
                        return false;
                    }

                    if (args[2].equalsIgnoreCase("top-up")) {
                        Cards.get().set("Cards." + args[1].toUpperCase() + ".Type", "top-up");
                        Cards.save();
                        sender.sendMessage("You have successfully created a card called " + args[1] + " with the type 'top-up'. Run /card for more info!");
                        return false;
                    } else if (args[2].equalsIgnoreCase("single-use")) {
                        Cards.get().set("Cards." + args[1].toUpperCase() + ".Type", "single-use");
                        Cards.save();
                        sender.sendMessage("You have successfully created a card called " + args[1] + " with the type 'single-use'. Run /card for more info!");
                        return false;
                    } else {
                        sender.sendMessage(ChatColor.RED + "The type is incorrect! Please either use 'singe-use' or 'top-up'.");
                        return false;
                    }
                }
            case "delete":
                if (cmdSender.equals("player")) {
                    if (!sender.hasPermission("cftp.cards.delete")) {
                        sender.sendMessage(ChatColor.RED + "Sorry, but you do not have permission to delete a card.");
                        return false;
                    } else {
                        // player has perms
                        if (args.length != 2) {
                            sender.sendMessage(ChatColor.RED + "Incorrect Usage! Please use '/card delete (name)");
                            return false;
                        }
                        // args # is correct
                        if (Cards.get().get("Cards." + args[1].toUpperCase()) == null) {
                            sender.sendMessage(ChatColor.RED + "A card with this name doesn't exist!");
                            return false;
                        }
                        Cards.get().set("Cards." + args[1].toUpperCase(), null);
                        Cards.save();
                        sender.sendMessage(ChatColor.GREEN + "Successfully deleted the card!");
                        return false;
                    }
                }
                else {
                    // sender = console
                    if (args.length != 2) {
                        sender.sendMessage("Incorrect Usage! Please use '/card delete (name)");
                        return false;
                    }
                    // args # is correct
                    if (Cards.get().get("Cards." + args[1].toUpperCase()) == null) {
                        sender.sendMessage("A card with this name doesn't exist!");
                        return false;
                    }
                    Cards.get().set("Cards." + args[1].toUpperCase(), null);
                    Cards.save();
                    sender.sendMessage("Successfully deleted the card!");
                    return false;
                }
            case "addcompany":
                if (cmdSender.equals("player")) {
                    if (!sender.hasPermission("cftp.cards.addcompany")) {
                        sender.sendMessage(ChatColor.RED + "Sorry, but you do not have permission to add a company to a card.");
                        return false;
                    } else {
                        // player has perms
                        if (args.length != 3) {
                            sender.sendMessage(ChatColor.RED + "Incorrect Usage! Please use '/card addcompany (name of card) (name of company)");
                            return false;
                        }
                        // args # is correct
                        if (Cards.get().get("Cards." + args[1].toUpperCase()) == null) {
                            sender.sendMessage(ChatColor.RED + "A card with this name doesn't exist!");
                            return false;
                        }
                        if (Companies.get().get("Companies." + args[2].toUpperCase()) == null) {
                            sender.sendMessage(ChatColor.RED + "This company doesn't exist!");
                            return false;
                        }
                        List<String> preexist = Cards.get().getStringList("Cards." + args[1].toUpperCase() + ".Companies");
                        if (preexist.contains(args[2].toUpperCase())){
                            sender.sendMessage(ChatColor.RED + "This company is already added to that card!");
                            return false;
                        }
                        preexist.add(args[2].toUpperCase());
                        Cards.get().set("Cards." + args[1].toUpperCase() + ".Companies", preexist);
                        Cards.save();
                        sender.sendMessage(ChatColor.GREEN + "Successfully added this company to a card!");
                        return false;
                    }
                }
                else{
                    if (args.length != 3) {
                        sender.sendMessage("Incorrect Usage! Please use '/card addcompany (name of card) (name of company)");
                        return false;
                    }
                    // args # is correct
                    if (Cards.get().get("Cards." + args[1].toUpperCase()) == null) {
                        sender.sendMessage("A card with this name doesn't exist!");
                        return false;
                    }
                    if (Companies.get().get("Companies." + args[2].toUpperCase()) == null) {
                        sender.sendMessage("This company doesn't exist!");
                        return false;
                    }
                    List<String> preexist1 = Cards.get().getStringList("Cards." + args[1].toUpperCase() + ".Companies");
                    if (preexist1.contains(args[2].toUpperCase())){
                        sender.sendMessage("This company is already added to that card!");
                        return false;
                    }
                    preexist1.add(args[2].toUpperCase());
                    Cards.get().set("Cards." + args[1].toUpperCase() + ".Companies", preexist1);
                    Cards.save();
                    sender.sendMessage("Successfully added this company to a card!");
                    return false;
                }
            case "removecompany":
                if (cmdSender.equals("player")) {
                    if (!sender.hasPermission("cftp.cards.removecompany")) {
                        sender.sendMessage(ChatColor.RED + "Sorry, but you do not have permission to remove a company from a card.");
                        return false;
                    } else {
                        // player has perms
                        if (args.length != 3) {
                            sender.sendMessage(ChatColor.RED + "Incorrect Usage! Please use '/card removecompany (name of card) (name of company)");
                            return false;
                        }
                        // args # is correct
                        if (Cards.get().get("Cards." + args[1].toUpperCase()) == null) {
                            sender.sendMessage(ChatColor.RED + "A card with this name doesn't exist!");
                            return false;
                        }
                        List<String> preexist3 = Cards.get().getStringList("Cards." + args[1].toUpperCase() + ".Companies");
                        if (!preexist3.contains(args[2].toUpperCase())){
                            sender.sendMessage(ChatColor.RED + "The company specified is not added to this card!");
                            return false;
                        }
                        preexist3.remove(args[2].toUpperCase());
                        Cards.get().set("Cards." + args[1].toUpperCase() + ".Companies", preexist3);
                        Cards.save();
                        sender.sendMessage(ChatColor.GREEN + "Successfully removed this company from the card!");
                        return false;
                    }
                }
                else {
                    if (args.length != 3) {
                        sender.sendMessage("Incorrect Usage! Please use '/card removecompany (name of card) (name of company)");
                        return false;
                    }
                    // args # is correct
                    if (Cards.get().get("Cards." + args[1].toUpperCase()) == null) {
                        sender.sendMessage("A card with this name doesn't exist!");
                        return false;
                    }
                    List<String> preexist4 = Cards.get().getStringList("Cards." + args[1].toUpperCase() + ".Companies");
                    if (!preexist4.contains(args[2].toUpperCase())){
                        sender.sendMessage("The company specified is not added to this card!");
                        return false;
                    }
                    preexist4.remove(args[2].toUpperCase());
                    Cards.get().set("Cards." + args[1].toUpperCase() + ".Companies", preexist4);
                    Cards.save();
                    sender.sendMessage("Successfully removed this company from the card!");
                    return false;
                }
            case "setdisplayname":
                if (cmdSender.equals("player")) {
                    if (!sender.hasPermission("cftp.cards.setdisplayname")) {
                        sender.sendMessage(ChatColor.RED + "Sorry, but you do not have permission to set the displayname of a card.");
                        return false;
                    } else {
                        // player has perms
                        if (args.length != 2) {
                            sender.sendMessage(ChatColor.RED + "Incorrect Usage! Please use '/card setdisplayname (name of card) [An owner/sr. admin will have to edit it for you!]");
                            return false;
                        }
                        // args # is correct
                        if (Cards.get().get("Cards." + args[1].toUpperCase()) == null) {
                            sender.sendMessage(ChatColor.RED + "A card with this name doesn't exist!");
                            return false;
                        }

                        Cards.get().set("Cards." + args[1].toUpperCase() + ".Displayname", "This is your displayname. Edit me please!");
                        Cards.save();
                        sender.sendMessage(ChatColor.GREEN + "Success! You have set the displayname of this card. This can be edited by an Owner / Sr. Admin in the cards.yml file.");
                        return false;
                    }
                }
                else {
                    if (args.length != 2) {
                        sender.sendMessage("Incorrect Usage! Please use '/card setdisplayname (name of card) [You will have to edit this in cards.yml after.]");
                        return false;
                    }
                    // args # is correct
                    if (Cards.get().get("Cards." + args[1].toUpperCase()) == null) {
                        sender.sendMessage("A card with this name doesn't exist!");
                        return false;
                    }

                    Cards.get().set("Cards." + args[1].toUpperCase() + ".Displayname", "This is your displayname. Edit me please!");
                    Cards.save();
                    sender.sendMessage("Success! You have set the displayname of this card. This can be edited in the cards.yml file.");
                    return false;
                }
            case "setdescription":
                if (cmdSender.equals("player")) {
                    if (!sender.hasPermission("cftp.cards.setdescription")) {
                        sender.sendMessage(ChatColor.RED + "Sorry, but you do not have permission to set the description of a card.");
                        return false;
                    } else {
                        // player has perms
                        if (args.length != 2) {
                            sender.sendMessage(ChatColor.RED + "Incorrect Usage! Please use '/card setdescription (name of card) [An owner/sr. admin will have to edit it for you!]");
                            return false;
                        }
                        // args # is correct
                        if (Cards.get().get("Cards." + args[1].toUpperCase()) == null) {
                            sender.sendMessage("A card with this name doesn't exist!");
                            return false;
                        }

                        Cards.get().set("Cards." + args[1].toUpperCase() + ".Description", "This is your description. Edit me please!");
                        Cards.save();
                        sender.sendMessage(ChatColor.GREEN + "Success! You have set the description of this card. This can be edited by an Owner / Sr. Admin in the cards.yml file.");
                        return false;
                    }
                }
                else {
                    // console
                    if (args.length != 2) {
                        sender.sendMessage("Incorrect Usage! Please use '/card setdescription (name of card) [You will have to edit this in the cards.yml file.]");
                        return false;
                    }
                    // args # is correct
                    if (Cards.get().get("Cards." + args[1].toUpperCase()) == null) {
                        sender.sendMessage("A card with this name doesn't exist!");
                        return false;
                    }

                    Cards.get().set("Cards." + args[1].toUpperCase() + ".Description", "This is your description. Edit me please!");
                    Cards.save();
                    sender.sendMessage("Success! You have set the description of this card. This can be edited by an Owner / Sr. Admin in the cards.yml file.");
                    return false;
                }
            case "adddiscount":
                if (cmdSender.equals("player")) {
                    if (!sender.hasPermission("cftp.cards.adddiscount")) {
                        sender.sendMessage(ChatColor.RED + "Sorry, but you do not have permission to add a discount to a card.");
                        return false;
                    } else {
                        // player has perms
                        if (args.length != 4) {
                            sender.sendMessage(ChatColor.RED + "Incorrect Usage! Please use '/card adddiscount (name of card) (company to discount from) (percentage)");
                            return false;
                        }
                        // args # is correct
                        if (Cards.get().get("Cards." + args[1].toUpperCase()) == null) {
                            sender.sendMessage("A card with this name doesn't exist!");
                            return false;
                        }
                        if (Companies.get().get("Companies." + args[2].toUpperCase()) == null) {
                            sender.sendMessage(ChatColor.RED + "This company doesn't exist!");
                            return false;
                        }
                    }
                }
                break;
            case "listdiscounts":
                break;
            case "removediscount":
                break;
            default:
                break;
        }
        return false;
    }
}

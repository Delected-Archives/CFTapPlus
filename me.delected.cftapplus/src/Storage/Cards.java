package Storage;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class Cards {
    private static File file;
    private static FileConfiguration customFile;

    public static void setup() throws IOException {
        file = new File(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("CFTapPlus")).getDataFolder(), "cards.yml");

        if (!file.exists()) {
            file.createNewFile();
        }
        customFile = YamlConfiguration.loadConfiguration(file);

    }

    public static FileConfiguration get() {
        return customFile;
    }

    public static void save() {
        try {
            customFile.save(file);
        } catch (IOException e) {
            System.out.println("[CFTapPlus] Couldn't the save cards.yml!");
        }
    }

    public static void reload() {
        customFile = YamlConfiguration.loadConfiguration(file);
    }

}







package scheme.moded;

import arc.input.KeyBind;
import arc.input.KeyCode;
import scheme.Main;

/** Migrated to Mindustry V8 */
public class ModedBinding {
    
    // In V8, KeyBind is a class! We define custom keybinds by simply creating new KeyBind objects.
    // Format: new KeyBind(String name, KeyCode defaultKey, String category)
   public static final KeyBind 
    adminscfg = new KeyBind("adminscfg", KeyCode.f12, "mod") {},
    rendercfg = new KeyBind("rendercfg", KeyCode.y, "mod") {},
    schematic_shortcut = new KeyBind("schematic_shortcut", KeyCode.g, "mod") {},
    toggle_core_items = new KeyBind("toggle_core_items", KeyCode.f7, "mod") {},
    toggle_ai = new KeyBind("toggle_ai", KeyCode.i, "mod") {},
    manage_unit = new KeyBind("manage_unit", KeyCode.f2, "mod") {},
    manage_effect = new KeyBind("manage_effect", KeyCode.f3, "mod") {},
    manage_item = new KeyBind("manage_item", KeyCode.f4, "mod") {},
    manage_team = new KeyBind("manage_team", KeyCode.semicolon, "mod") {},
    place_core = new KeyBind("place_core", KeyCode.apostrophe, "mod") {},
    alternative = new KeyBind("alternative", KeyCode.altLeft, "mod") {};

    public static void load() {
        // You NO LONGER need to manually merge the game's keybind arrays in V8!
        // The game automatically detects the KeyBind objects you created above.
        Main.log("Mod keybinds loaded.");
    }
}
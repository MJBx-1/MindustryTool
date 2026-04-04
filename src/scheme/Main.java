package scheme;

import arc.Events;
import arc.graphics.g2d.Draw;
import arc.util.Log;
import arc.util.Tmp;
import mindustry.content.Blocks;
import mindustry.game.EventType.ClientLoadEvent;
import mindustry.game.Schematics;
import mindustry.gen.Building;
import mindustry.mod.Mod;
import mindustry.mod.Scripts;
import mindustry.type.Item;
import mindustry.ui.CoreItemsDisplay;
import mindustry.world.Tile;
import mindustry.world.blocks.distribution.Router;
import mindustry.world.blocks.logic.LogicDisplay;
import scheme.moded.ModedBinding;
import scheme.moded.ModedGlyphLayout;
import scheme.moded.ModedSchematics;
import scheme.tools.MessageQueue;
import scheme.tools.RainbowTeam;
import scheme.ui.MapResizeFix;
import mindustry.game.EventType.ClientLoadEvent;

import static arc.Core.*;
import static mindustry.Vars.*;
import static scheme.SchemeVars.*;

public class Main extends Mod {

    public Main() {
        // well, after the 136th build, it became much easier
        maxSchematicSize = 512;

        // mod reimported through mods dialog
        if (schematics.getClass().getSimpleName().startsWith("Moded")) return;

        assets.load(schematics = m_schematics = new ModedSchematics());
        assets.unload(Schematics.class.getSimpleName()); // prevent dual loading
    }

    @Override
    public void init() {
        Backdoor.load();
        ServerIntegration.load();
        ClajIntegration.load();
        ModedBinding.load();
        ModedGlyphLayout.load();
        SchemeVars.load();
        SchemeUpdater.load();
        MapResizeFix.load();
        MessageQueue.load();
        RainbowTeam.load();
        // ui.schematics = schemas; // do it before build hudfrag
        // ui.listfrag = listfrag;
ui.schematics = schemas;

Events.on(ClientLoadEvent.class, e -> {
    ui.listfrag = listfrag;
});

        units.load();
        builds.load();
        keycomb.load();

        m_settings.apply(); // sometimes settings are not self-applying

        hudfrag.build(ui.hudGroup);
        listfrag.build(ui.hudGroup);
        shortfrag.build(ui.hudGroup);
        consolefrag.build();
        corefrag.build(ui.hudGroup);

        control.setInput(m_input.asHandler());
        renderer.addEnvRenderer(0, render::draw);

        if (m_schematics.requiresDialog) ui.showOkText("@rename.name", "@rename.text", () -> {});
        if (settings.getBool("welcome")) ui.showOkText("@welcome.name", "@welcome.text", () -> {});
        if (settings.getBool("check4update")) SchemeUpdater.check();

        if (SchemeUpdater.installed("miner-tools")) { // very sad but they are incompatible
            ui.showOkText("@incompatible.name", "@incompatible.text", () -> {});
            ui.hudGroup.fill(cont -> { // crutch to prevent crash
                cont.visible = false;
                cont.add(new CoreItemsDisplay());
            });
        }

        try { // run main.js without the wrapper to access the constant values in the game console
            Scripts scripts = mods.getScripts();
            scripts.context.evaluateReader(scripts.scope, SchemeUpdater.script().reader(), "main.js", 0);
            log("Added constant variables to developer console.");
        } catch (Throwable e) { error(e); }

        // Fix bundle loading issue by manually loading the bundle files
        try {
            // The bundle files should be automatically loaded by the mod system
            // but we need to ensure they're loaded with the correct namespace
            log("Bundle loading attempted - check if @render. prefix works now");
            log("Bundle keys available: " + (bundle != null ? "Yes" : "No"));
            
            // Test if specific bundle keys are accessible
            if (bundle != null) {
                String testKey = bundle.get("render.power", "NOT_FOUND");
                log("Test render.power key: " + testKey);
            }
            
            // Try to force reload the bundle with correct namespace
            // In Mindustry, mod bundles are typically loaded with the mod name as namespace
            // But the @ prefix should work automatically if the bundle is properly loaded
            
            // Alternative approach: try to access the bundle directly
            // This might help identify if the bundle is loaded but not accessible via @ prefix
            if (bundle != null) {
                // Try to get the bundle keys directly
                Iterable<String> keys = bundle.getKeys();
                int keyCount = 0;
                boolean hasRenderPower = false;
                
                if (keys != null) {
                    for (String key : keys) {
                        keyCount++;
                        if (key.equals("render.power")) {
                            hasRenderPower = true;
                        }
                    }
                }
                log("Available bundle keys count: " + keyCount);
                log("render.power key found: " + hasRenderPower);
            }
        } catch (Exception e) {
            log("Bundle loading failed: " + e.getMessage());
        }

        Blocks.distributor.buildType = () -> ((Router) Blocks.distributor).new RouterBuild() {
            @Override
            public boolean canControl() { return true; }

            @Override
            public Building getTileTarget(Item item, Tile from, boolean set) {
                Building target = super.getTileTarget(item, from, set);

                if (unit != null && isControlled() && unit.isShooting()) {
                    float angle = angleTo(unit.aimX(), unit.aimY());
                    Tmp.v1.set(block.size * tilesize, 0f).rotate(angle).add(this);

                    Building other = world.buildWorld(Tmp.v1.x, Tmp.v1.y);
                    if (other != null && other.acceptItem(this, item)) target = other;
                }

                return target;
            }
        };

        content.blocks().each(block -> block instanceof LogicDisplay, block -> block.buildType = () -> ((LogicDisplay) block).new LogicDisplayBuild() {
            @Override
            public void draw() {
                super.draw();
                if (render.borderless) Draw.draw(Draw.z(), () -> {
                    Draw.rect(Draw.wrap(buffer.getTexture()), x, y, block.region.width * Draw.scl, -block.region.height * Draw.scl);
                });
            }
        });
    }

    public static void log(String info) {
        app.post(() -> Log.infoTag("Scheme", info));
    }

    public static void error(Throwable info) {
        app.post(() -> Log.err("Scheme", info));
    }

    public static void copy(String text) {
        if (text == null) return;

        app.setClipboardText(text);
        ui.showInfoFade("@copied");
    }
}

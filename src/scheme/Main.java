package scheme;

import arc.scene.event.Touchable;
import arc.util.Time;

import mindustry.gen.Icon;
import mindustry.mod.Mod;
import mindustry.ui.Styles;
import arc.Events;
import static arc.Core.*;
import static mindustry.Vars.*;

public class Main extends Mod {

    public static boolean locked = false;

    @Override
    public void init() {
        // hook into update loop without replacing input handler
        Events.run(arc.Events.class, () -> {});  
        
        ui.hudGroup.fill(cont -> {
            cont.name = "lockmovement";
            cont.top().left();
            cont.touchable = Touchable.childrenOnly;
            cont.visible(() -> ui.hudfrag.shown && !ui.minimapfrag.shown());
            cont.table(pad -> {
                pad.button(Icon.lock, Styles.clearNoneTogglei, () -> locked = !locked)
                   .checked(b -> locked)
                   .size(46f)
                   .padTop(mobile ? 69f : 0f);
            });
        });

        // intercept movement each frame via renderer
        renderer.addEnvRenderer(0, () -> {
            if (locked && player != null && player.unit() != null && !state.isMenu()) {
                player.unit().vel.setZero();
            }
        });
    }
}
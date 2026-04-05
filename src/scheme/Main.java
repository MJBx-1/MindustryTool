package scheme;

import arc.scene.event.Touchable;
import mindustry.gen.Icon;
import mindustry.mod.Mod;
import mindustry.ui.Styles;
import static arc.Core.*;
import static mindustry.Vars.*;

public class Main extends Mod {

    public static LockDesktopInput desktopInput;
    public static LockMobileInput mobileInput;

    @Override
    public void init() {
        ui.hudGroup.fill(cont -> {
            cont.name = "lockmovement";
            cont.top().left();
            cont.touchable = Touchable.childrenOnly;
            cont.visible(() -> ui.hudfrag.shown && !ui.minimapfrag.shown());
            cont.table(pad -> {
                if (mobile) {
                    mobileInput = new LockMobileInput();
                    control.setInput(mobileInput);
                    pad.button(Icon.lock, Styles.clearNoneTogglei, () -> mobileInput.toggleLock())
                       .checked(b -> mobileInput.locked).size(46f).padTop(69f);
                } else {
                    desktopInput = new LockDesktopInput();
                    control.setInput(desktopInput);
                    pad.button(Icon.lock, Styles.clearNoneTogglei, () -> desktopInput.toggleLock())
                       .checked(b -> desktopInput.locked).size(46f);
                }
            });
        });
    }
}
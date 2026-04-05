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
    // set input FIRST, outside of any fill/lambda
    if (mobile) {
        mobileInput = new LockMobileInput();
        control.setInput(mobileInput);
    } else {
        desktopInput = new LockDesktopInput();
        control.setInput(desktopInput);
    }

    // then build UI
    ui.hudGroup.fill(cont -> {
        cont.name = "lockmovement";
        cont.top().left();
        cont.touchable = Touchable.childrenOnly;
        cont.visible(() -> ui.hudfrag.shown && !ui.minimapfrag.shown());
        cont.table(pad -> {
            if (mobile) {
                pad.button(Icon.lock, Styles.clearNoneTogglei, () -> mobileInput.toggleLock())
                   .checked(b -> mobileInput.locked).size(46f).padTop(69f);
            } else {
                pad.button(Icon.lock, Styles.clearNoneTogglei, () -> desktopInput.toggleLock())
                   .checked(b -> desktopInput.locked).size(46f);
            }
        });
    });
}
}
// LockDesktopInput.java
package scheme;

import mindustry.gen.Unit;
import mindustry.input.DesktopInput;
import static arc.Core.*;
import static mindustry.Vars.*;

public class LockDesktopInput extends DesktopInput {
    public boolean locked = false;

    public void toggleLock() { locked = !locked; }

    @Override
    protected void updateMovement(Unit unit) {
        if (!locked) {
            super.updateMovement(unit);
        } else {
            panning = true; // allow camera to still move freely
        }
    }
}
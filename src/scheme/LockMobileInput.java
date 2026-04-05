// LockMobileInput.java
package scheme;

import mindustry.gen.Unit;
import mindustry.input.MobileInput;
import static arc.Core.*;
import static mindustry.Vars.*;

public class LockMobileInput extends MobileInput {
    public boolean locked = false;

    public void toggleLock() { locked = !locked; }

    @Override
    protected void updateMovement(Unit unit) {
        if (!locked) {
            super.updateMovement(unit);
        } else {
            camera.position.set(unit.x, unit.y); // keep camera on unit but allow touch panning
        }
    }
}
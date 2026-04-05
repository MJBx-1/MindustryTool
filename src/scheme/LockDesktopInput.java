package scheme;

import mindustry.gen.Unit;
import mindustry.input.DesktopInput;

public class LockDesktopInput extends DesktopInput {
    public boolean locked = false;

    public void toggleLock() { locked = !locked; }

    @Override
    protected void updateMovement(Unit unit) {
        if (!locked) super.updateMovement(unit);
    }
}
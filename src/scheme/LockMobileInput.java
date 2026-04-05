package scheme;

import mindustry.gen.Unit;
import mindustry.input.MobileInput;

public class LockMobileInput extends MobileInput {
    public boolean locked = false;

    public void toggleLock() { locked = !locked; }

    @Override
    protected void updateMovement(Unit unit) {
        if (!locked) super.updateMovement(unit);
    }
}
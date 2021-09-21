package fhannenheim.rerum.components;

import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.nbt.NbtCompound;

public class BoolComponent implements Component {
    private boolean value;

    public BoolComponent(boolean initialValue){
        super();
        value = initialValue;
    }

    public void set(boolean _value) {
        value = _value;
    }

    public boolean get() {
        return value;
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        value = tag.getBoolean("value");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putBoolean("value", value);
    }
}

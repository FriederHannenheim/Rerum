package fhannenheim.rerum.components;

import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.nbt.NbtCompound;

public class IntComponent implements Component {
    private int value;

    public IntComponent(int initialValue){
        super();
        value = initialValue;
    }

    public void set(int _value) {
        value = _value;
    }

    public int get() {
        return value;
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        value = tag.getInt("value");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putInt("value", value);
    }
}

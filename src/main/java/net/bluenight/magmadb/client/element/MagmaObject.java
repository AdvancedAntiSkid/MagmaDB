package net.bluenight.magmadb.client.element;

import com.google.gson.*;

import java.util.*;
import java.util.Collection;

public class MagmaObject extends MagmaElement {
    private final TreeMap<String, MagmaElement> members = new TreeMap<>();

    public void add(String key, MagmaElement value)
    {
        members.put(key, value != null ? value : MagmaNull.INSTANCE);
    }

    public MagmaElement remove(String key)
    {
        return members.remove(key);
    }

    public void addProperty(String key, String value) {
        add(key, value != null ? new MagmaPrimitive(value) : MagmaNull.INSTANCE);
    }

    public void addProperty(String key, Number value) {
        add(key, value != null ? new MagmaPrimitive(value) : MagmaNull.INSTANCE);
    }

    public void addProperty(String key, Boolean value) {
        add(key, value != null ? new MagmaPrimitive(value) : MagmaNull.INSTANCE);
    }

    public void addProperty(String key, Character value) {
        add(key, value != null ? new MagmaPrimitive(value) : MagmaNull.INSTANCE);
    }

    public Set<Map.Entry<String, MagmaElement>> entrySet() {
        return members.entrySet();
    }

    public Set<String> keySet() {
        return members.keySet();
    }

    public Collection<MagmaElement> values() {
        return members.values();
    }

    public int size() {
        return members.size();
    }

    public boolean contains(String key) {
        return members.containsKey(key);
    }

    public MagmaElement get(String key) {
        return members.get(key);
    }

    public boolean getBoolean(String key) {
        return get(key).getAsBoolean();
    }

    public Number getNumber(String key) {
        return get(key).getAsNumber();
    }

    public String getString(String key) {
        return get(key).getAsString();
    }

    public double getDouble(String key) {
        return get(key).getAsDouble();
    }

    public float getFloat(String key) {
        return get(key).getAsFloat();
    }

    public long getLong(String key) {
        return get(key).getAsLong();
    }

    public int getInt(String key) {
        return get(key).getAsInt();
    }

    public short getShort(String key) {
        return get(key).getAsShort();
    }

    public byte getByte(String key) {
        return get(key).getAsByte();
    }

    public char getChar(String key) {
        return get(key).getAsChar();
    }

    public MagmaArray getMagmaArray(String key) {
        return get(key).getAsMagmaArray();
    }

    public MagmaObject getMagmaObject(String key) {
        return get(key).getAsMagmaObject();
    }

    public MagmaPrimitive getMagmaPrimitive(String key) {
        return get(key).getAsMagmaPrimitive();
    }

    @Override
    public MagmaObject getAsMagmaObject() {
        return this;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("{");
        Iterator<Map.Entry<String, MagmaElement>> iterator = members.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, MagmaElement> entry = iterator.next();
            builder.append('"').append(entry.getKey()).append("\": ").append(entry.getValue());
            if (iterator.hasNext()) {
                builder.append(", ");
            }
        }
        return builder.append("}").toString();
    }

    @Override
    public MagmaObject deepCopy() {
        MagmaObject result = new MagmaObject();
        for (Map.Entry<String, MagmaElement> entry : members.entrySet()) {
            result.add(entry.getKey(), entry.getValue().deepCopy());
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        return (o == this) || (o instanceof MagmaObject
            && ((MagmaObject) o).members.equals(members));
    }

    @Override
    public int hashCode() {
        return members.hashCode();
    }

    public static MagmaObject fromJson(JsonObject json) {
        MagmaObject object = new MagmaObject();
        for (String key : json.keySet()) {
            JsonElement element = json.get(key);
            if (element instanceof JsonNull)
                object.add(key, MagmaNull.INSTANCE);
            else if (element instanceof JsonPrimitive)
                object.add(key, MagmaPrimitive.fromJson((JsonPrimitive) element));
            else if (element instanceof JsonObject)
                object.add(key, MagmaObject.fromJson((JsonObject) element));
            else if (element instanceof JsonArray)
                object.add(key, MagmaArray.fromJson((JsonArray) element));
        }
        return object;
    }
}

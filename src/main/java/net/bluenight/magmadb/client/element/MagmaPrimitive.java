package net.bluenight.magmadb.client.element;

import com.google.gson.JsonPrimitive;
import net.bluenight.magmadb.client.exception.MagmaDBException;
import org.apache.commons.text.StringEscapeUtils;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.Objects;

public class MagmaPrimitive extends MagmaElement {
    private final Object value;

    public MagmaPrimitive(Boolean value) {
        this.value = Objects.requireNonNull(value);
    }

    public MagmaPrimitive(Number value) {
        this.value = Objects.requireNonNull(value);
    }

    public MagmaPrimitive(String value) {
        this.value = Objects.requireNonNull(value);
    }

    public MagmaPrimitive(Character value) {
        this.value = Objects.requireNonNull(value);
    }

    @Override
    public boolean isBoolean() {
        return value instanceof Boolean;
    }

    @Override
    public boolean isNumber() {
        return value instanceof Number;
    }

    @Override
    public boolean isString() {
        return value instanceof String;
    }

    @Override
    public boolean isDouble() {
        return value instanceof Double;
    }

    @Override
    public boolean isFloat() {
        return value instanceof Float;
    }

    @Override
    public boolean isLong() {
        return value instanceof Long;
    }

    @Override
    public boolean isInt() {
        return value instanceof Integer;
    }

    @Override
    public boolean isShort() {
        return value instanceof Short;
    }

    @Override
    public boolean isByte() {
        return value instanceof Byte;
    }

    @Override
    public boolean isChar() {
        return value instanceof Character;
    }

    @Override
    public boolean getAsBoolean() {
        return isBoolean() ? (boolean) value : Boolean.parseBoolean(getAsString());
    }

    @Override
    public Number getAsNumber() {
        return isNumber() ? (Number) value : Double.parseDouble(getAsString());
    }

    @Override
    public String getAsString() {
        if (isNumber())
            return getAsNumber().toString();
        if (isBoolean())
            return ((Boolean) value).toString();
        return (String) value;
    }

    @Override
    public double getAsDouble() {
        return isNumber() ? getAsNumber().doubleValue() : Double.parseDouble(getAsString());
    }

    @Override
    public float getAsFloat()
    {
        return isNumber() ? getAsNumber().floatValue() : Float.parseFloat(getAsString());
    }

    @Override
    public long getAsLong() {
        return isNumber() ? getAsNumber().longValue() : Long.parseLong(getAsString());
    }

    @Override
    public int getAsInt() {
        return isNumber() ? getAsNumber().intValue() : Integer.parseInt(getAsString());
    }

    @Override
    public short getAsShort() {
        return isNumber() ? getAsNumber().shortValue() : Short.parseShort(getAsString());
    }

    @Override
    public byte getAsByte() {
        return isNumber() ? getAsNumber().byteValue() : Byte.parseByte(getAsString());
    }

    @Override
    public char getAsChar() {
        return isChar() ? (Character) value : getAsString().charAt(0);
    }

    @Override
    public MagmaElement deepCopy() {
        return this;
    }

    @Override
    public String toString() {
        if (isString() || isChar())
            return '"' + StringEscapeUtils.escapeJava(getAsString()) + '"';
        return String.valueOf(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MagmaPrimitive other = (MagmaPrimitive) o;
        if (value == null) return other.value == null;
        if (isIntegral(this) && isIntegral(other))
            return getAsNumber().longValue() == other.getAsNumber().longValue();
        if (value instanceof Number && other.value instanceof Number) {
            double a = getAsNumber().doubleValue();
            double b = other.getAsNumber().doubleValue();
            return a == b || (Double.isNaN(a) && Double.isNaN(b));
        }
        return value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    private static boolean isIntegral(MagmaPrimitive primitive) {
        if (!(primitive.value instanceof Number)) return false;
        Number number = (Number) primitive.value;
        return number instanceof BigInteger || number instanceof Long
            || number instanceof Integer || number instanceof Short || number instanceof Byte;
    }

    public static MagmaPrimitive fromJson(JsonPrimitive json) {
        if (json.isBoolean())
            return new MagmaPrimitive(json.getAsBoolean());
        else if (json.isNumber())
            return new MagmaPrimitive(json.getAsNumber());
        else if (json.isString())
            return new MagmaPrimitive(json.getAsString());
        throw new MagmaDBException("Unable to parse " + json + " to MagmaPrimitive");
    }
}

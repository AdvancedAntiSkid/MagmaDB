package net.bluenight.magmadb.client.element;

import net.bluenight.magmadb.client.ElementConvertException;

public abstract class MagmaElement
{
    public MagmaArray getAsMagmaArray()
    {
        if (isMagmaArray())
            return (MagmaArray) this;
        throw new ElementConvertException("Can not convert " + getClass() + " to " + MagmaArray.class);
    }

    public MagmaObject getAsMagmaObject()
    {
        if (isMagmaArray())
            return (MagmaObject) this;
        throw new ElementConvertException("Can not convert " + getClass() + " to " + MagmaObject.class);
    }

    public MagmaPrimitive getAsMagmaPrimitive()
    {
        if (isMagmaPrimitive())
            return (MagmaPrimitive) this;
        throw new ElementConvertException("Can not convert " + getClass() + " to " + MagmaPrimitive.class);
    }

    public MagmaNull getAsMagmaNull()
    {
        if (isMagmaNull())
            return (MagmaNull) this;
        throw new ElementConvertException("Can not convert " + getClass() + " to " + MagmaNull.class);
    }

    public boolean isMagmaArray()
    {
        return this instanceof MagmaArray;
    }

    public boolean isMagmaObject()
    {
        return this instanceof MagmaObject;
    }

    public boolean isMagmaPrimitive()
    {
        return this instanceof MagmaPrimitive;
    }

    public boolean isMagmaNull()
    {
        return this instanceof MagmaNull;
    }

    public boolean getAsBoolean()
    {
        throw new ElementConvertException("Can not convert " + getClass() + " to " + boolean.class);
    }

    public Number getAsNumber()
    {
        throw new ElementConvertException("Can not convert " + getClass() + " to " + Number.class);
    }

    public String getAsString()
    {
        throw new ElementConvertException("Can not convert " + getClass() + " to " + String.class);
    }

    public double getAsDouble()
    {
        throw new ElementConvertException("Can not convert " + getClass() + " to " + double.class);
    }

    public float getAsFloat()
    {
        throw new ElementConvertException("Can not convert " + getClass() + " to " + float.class);
    }

    public long getAsLong()
    {
        throw new ElementConvertException("Can not convert " + getClass() + " to " + long.class);
    }

    public int getAsInt()
    {
        throw new ElementConvertException("Can not convert " + getClass() + " to " + int.class);
    }

    public short getAsShort()
    {
        throw new ElementConvertException("Can not convert " + getClass() + " to " + short.class);
    }

    public byte getAsByte()
    {
        throw new ElementConvertException("Can not convert " + getClass() + " to " + byte.class);
    }

    public char getAsChar()
    {
        throw new ElementConvertException("Can not convert " + getClass() + " to " + char.class);
    }

    public boolean isBoolean()
    {
        return false;
    }

    public boolean isNumber()
    {
        return false;
    }

    public boolean isString()
    {
        return false;
    }

    public boolean isDouble()
    {
        return false;
    }

    public boolean isFloat()
    {
        return false;
    }

    public boolean isLong()
    {
        return false;
    }

    public boolean isInt()
    {
        return false;
    }

    public boolean isShort()
    {
        return false;
    }

    public boolean isByte()
    {
        return false;
    }

    public boolean isChar()
    {
        return false;
    }

    public abstract MagmaElement deepCopy();

    public abstract String toString();

    public abstract boolean equals(Object o);

    public abstract int hashCode();
}

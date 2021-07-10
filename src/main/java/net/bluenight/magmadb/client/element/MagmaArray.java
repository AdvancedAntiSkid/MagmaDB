package net.bluenight.magmadb.client.element;

import com.google.gson.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

public class MagmaArray extends MagmaElement implements Iterable<MagmaElement> {
    private final List<MagmaElement> elements;

    public MagmaArray() {
        elements = new ArrayList<>();
    }

    public MagmaArray(int capacity) {
        elements = new ArrayList<>(capacity);
    }

    public void add(Boolean value) {
        elements.add(value != null ? new MagmaPrimitive(value) : MagmaNull.INSTANCE);
    }

    public void add(Character value) {
        elements.add(value != null ? new MagmaPrimitive(value) : MagmaNull.INSTANCE);
    }

    public void add(Number value) {
        elements.add(value != null ? new MagmaPrimitive(value) : MagmaNull.INSTANCE);
    }

    public void add(String value) {
        elements.add(value != null ? new MagmaPrimitive(value) : MagmaNull.INSTANCE);
    }

    public void add(MagmaElement value) {
        elements.add(value != null ? value : MagmaNull.INSTANCE);
    }

    public void addAll(MagmaArray array) {
        elements.addAll(array.elements);
    }

    public MagmaElement set(int index, MagmaElement element) {
        return elements.set(index, element);
    }

    public boolean remove(MagmaElement element) {
        return elements.remove(element);
    }

    public MagmaElement remove(int index) {
        return elements.remove(index);
    }

    public boolean contains(MagmaElement element) {
        return elements.contains(element);
    }

    public int size() {
        return elements.size();
    }

    public Iterator<MagmaElement> iterator() {
        return this.elements.iterator();
    }

    @Override
    public void forEach(Consumer<? super MagmaElement> action) {
        elements.forEach(action);
    }

    @Override
    public Spliterator<MagmaElement> spliterator() {
        return elements.spliterator();
    }

    public MagmaElement get(int index) {
        return elements.get(index);
    }

    @Override
    public MagmaArray deepCopy() {
        if (elements.isEmpty())
            return new MagmaArray();

        MagmaArray result = new MagmaArray(elements.size());
        for (MagmaElement element : elements)
            result.add(element.deepCopy());

        return result;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("[");
        Iterator<MagmaElement> iterator = elements.iterator();

        while (iterator.hasNext()) {
            builder.append(iterator.next());
            if (iterator.hasNext()) {
                builder.append(", ");
            }
        }

        return builder.append("]").toString();
    }

    @Override
    public boolean equals(Object o) {
        return (o == this) || o instanceof MagmaArray
            && ((MagmaArray) o).elements.equals(elements);
    }

    @Override
    public int hashCode() {
        return elements.hashCode();
    }

    public static MagmaArray fromJson(JsonArray element) {
        MagmaArray array = new MagmaArray();
        for (JsonElement e : element) {
            if (e instanceof JsonNull)
                array.add(MagmaNull.INSTANCE);
            else if (e instanceof JsonPrimitive)
                array.add(MagmaPrimitive.fromJson((JsonPrimitive) e));
            else if (e instanceof JsonObject)
                array.add(MagmaObject.fromJson((JsonObject) e));
            else if (e instanceof JsonArray)
                array.add(MagmaArray.fromJson((JsonArray) e));
        }
        return array;
    }
}

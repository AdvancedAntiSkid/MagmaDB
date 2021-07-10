import net.bluenight.magmadb.client.element.MagmaArray;
import net.bluenight.magmadb.client.element.MagmaObject;

public class Element
{
    public static void main(String[] args)
    {
        MagmaObject object = new MagmaObject();
        object.addProperty("kek", true);
        object.addProperty("hello", "World\\");

        MagmaObject xd = new MagmaObject();
        xd.addProperty("x", 2);

        object.add("wtf", xd);

        MagmaArray array = new MagmaArray();
        array.add("mi");
        array.add(123);
        array.add(false);

        object.add("kekker", array);

        System.out.println(object);
    }
}

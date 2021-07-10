import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class StreamTest
{
    public static void main(String[] args) throws Exception
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        ByteArrayOutputStream tempOut = new ByteArrayOutputStream();
        ObjectOutputStream streamOut = new ObjectOutputStream(tempOut);
        streamOut.writeInt(0x1e1e);
        streamOut.writeBoolean(true);
        streamOut.writeUTF("HELLO");
        streamOut.flush();

        byte[] data = tempOut.toByteArray();

        System.out.println("length " + data.length);
        out.write(data.length);
        System.out.println("data " + Arrays.toString(data));
        out.write(data);

        out.flush();

        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());

        int length = in.read();
        byte[] bytes = new byte[length];


        System.out.println("length " + length);
        //byte[] bytes = new byte[in.read]
    }

    public static void mainWtf(String[] args) throws Exception
    {
        ByteArrayOutputStream streamByteOut = new ByteArrayOutputStream();
        ObjectOutputStream streamObjectOut = new ObjectOutputStream(streamByteOut);



        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream streamOut = new ObjectOutputStream(out);

        streamOut.writeInt(0x1e1e);
        streamOut.writeBoolean(true);
        streamOut.writeUTF("HELLO");

        ByteBuffer buffer = ByteBuffer.wrap(out.toByteArray());
        streamObjectOut.writeObject(buffer);


        ByteArrayInputStream streamByteIn = new ByteArrayInputStream(streamByteOut.toByteArray());
        ObjectInputStream streamObjectIn = new ObjectInputStream(streamByteIn);

        //streamByteIn.read()
    }

    public static void mains(String[] args) throws Exception
    {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);

        out.writeUTF("FASZOM");
        out.flush();

        byte[] data = byteOut.toByteArray();

        ByteArrayOutputStream streamOut = new ByteArrayOutputStream();
        streamOut.write(data);
        streamOut.write(data);

        ByteArrayInputStream byteIn = new ByteArrayInputStream(data);
        ObjectInputStream in = new ObjectInputStream(byteIn);

        System.out.println(in.readUTF());


        ByteArrayInputStream streamIn = new ByteArrayInputStream(streamOut.toByteArray());
        ObjectInputStream o = new ObjectInputStream(streamIn);

        System.out.println();

    }
}

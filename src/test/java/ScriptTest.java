import jdk.nashorn.api.scripting.NashornScriptEngineFactory;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import java.io.IOException;

public class ScriptTest {
    public static void maina(String[] args) throws Exception {
        NashornScriptEngineFactory factory = new NashornScriptEngineFactory();
        ScriptEngine engine = factory.getScriptEngine("--language=es6");

        String script = "length(element.name) == element.id";

        engine.eval("function length(x) { return x.length }");

        // engine.eval("const element = {id: 4, name: 'asdf'};");

        engine.eval("function test() { return " + script + " }");

        Object o = ((Invocable) engine).invokeFunction("test");
        System.out.println(o);
    }

    public static void maind(String[] args) throws Exception {
        NashornScriptEngineFactory factory = new NashornScriptEngineFactory();
        ScriptEngine engine = factory.getScriptEngine("--language=es6");
        engine.eval("function xd(e) { return e != null; }");
        engine.eval("element = 34;");
        engine.eval("element = ['oke', 3]");
        boolean o = (boolean) engine.eval("xd(element);");
        System.out.println(o);
    }

    public static void main(String[] args) throws Exception {
        NashornScriptEngineFactory factory = new NashornScriptEngineFactory();
        ScriptEngine engine = factory.getScriptEngine("--language=es6");
        engine.eval("a = {xd: 'lol'}");
        System.out.println(engine.eval("JSON.stringify(a)"));
    }
}

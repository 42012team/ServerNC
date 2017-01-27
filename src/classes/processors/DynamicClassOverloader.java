package classes.processors;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DynamicClassOverloader extends ClassLoader {

    public String classPath;
    private Map classesHash = new HashMap();

    public DynamicClassOverloader(String classPath) {
        this.classPath = classPath;
    }

    @Override
    protected synchronized Class loadClass(String name, boolean resolve) throws ClassNotFoundException {
        Class result = findClass(name);
        if (resolve) {
            resolveClass(result);
        }
        return result;
    }

    @Override
    protected Class findClass(String name) throws ClassNotFoundException {
        Class result = (Class) classesHash.get(name);

        File f = findFile(name);

        if (f == null) {

            return findSystemClass(name);
        }

        try {
            byte[] classBytes = loadFileAsBytes(f);
            result = defineClass(name, classBytes, 0, classBytes.length);
        } catch (IOException e) {
            throw new ClassNotFoundException();
        } catch (ClassFormatError e) {
            throw new ClassNotFoundException();
        }
        classesHash.put(name, result);
        return result;
    }

    private File findFile(String name) {

        File f = new File((new File(classPath)).getPath() + "//" + name + ".class");
        if (f.exists()) {
            return f;
        }
        return null;
    }

    private static byte[] loadFileAsBytes(File file) throws IOException {
        byte[] result = new byte[(int) file.length()];
        FileInputStream f = new FileInputStream(file);
        try {
            f.read(result, 0, result.length);
        } finally {
            try {
                f.close();
            } catch (Exception e) {
            }
        }
        return result;
    }

}

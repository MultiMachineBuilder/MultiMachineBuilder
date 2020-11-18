package mmb.MODS.loader;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import mmb.debug.Debugger;

//https://techitmore.com/java/java-custom-classloader-loading-classes-from-an-inputstream/
public class StreamClassLoader extends ClassLoader {
	private static final Debugger debug = new Debugger("ClassPath");
    private final Map<String, byte[]> classData;
 
    @SuppressWarnings("unchecked")
	public StreamClassLoader(JarInputStream jarInputStream) throws IOException {
        classData = new HashMap();
 
        JarEntry jarEntry = null;
        while ((jarEntry = jarInputStream.getNextJarEntry()) != null) {
            String entryName = jarEntry.getName();
            int entrySize = (int) jarEntry.getSize();
            debug.printl("entry size: "+entrySize);
            if(entrySize < 0) continue;
            byte[] entryData = new byte[entrySize];
            jarInputStream.read(entryData, 0, entrySize);
 
            if (entryName.endsWith(".class")) {
                String className = entryName.replace("/", ".").replace(".class", "");
                classData.put(className, entryData);
            }
        }
    }
 
    public String[] getAllClassNames() {
        Set<String> keyset = classData.keySet();
        return keyset.toArray(new String[keyset.size()]);
    }
 
    @SuppressWarnings("unchecked")
	@Override
    public Class loadClass(String name) throws ClassNotFoundException {
        // note that it is required to first try loading the class using parent loader
        try {
            return super.loadClass(name);
        } catch (ClassNotFoundException e) {
            return findClass(name);
        }
    }
 
    @SuppressWarnings("unchecked")
	@Override
    public Class findClass(String name) throws ClassNotFoundException {
        Class loadedClass = null;
 
        byte[] data = classData.getOrDefault(name, new byte[0]);
        if (data.length == 0) {
            throw new ClassNotFoundException();
        }
 
        loadedClass = defineClass(name, data, 0, data.length, null);
 
        return loadedClass;
    }
}
package mmb.data.files;

import java.io.File;
import java.io.FileFilter;

public class ListFiles {
    static public File[] findDirectories(File root) { 
        return root.listFiles(new FileFilter() {
            public boolean accept(File f) {
                return f.isDirectory();
            }});
    }

   static public File[] findFiles(File root) {
        return root.listFiles(new FileFilter() {
            public boolean accept(File f) {
                return f.isFile();
            }
        });
    }
}
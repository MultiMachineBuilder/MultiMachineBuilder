/**
 * 
 */
package mmb.testing;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import mmb.databuffer.DataBuffer;
import mmb.databuffer.Saver;
import mmb.databuffer.Savers;
import mmb.debug.Debugger;

/**
 * @author oskar
 *
 */
public class ConsoleTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Debugger debug = new Debugger("BINARY FILES");
		try {
			Saver<String> save = Savers.txtSaver;
			DataBuffer<String> buf = DataBuffer.<String>empty(save);
			File file = new File("worlds/test.bin");
			
			int A = buf.create("Hello");
			int B = buf.create("bad");
			int C = buf.create("world!");
			buf.destroy(B);
			buf.save(new DataOutputStream(new FileOutputStream(file)));
			
			//Now read the file
			DataBuffer<String> buf2 = DataBuffer.<String>load(new DataInputStream(new FileInputStream(file)), save);
			
			
			ArrayList<String> list = new ArrayList<String>();
			for (int i = 0; i < buf.size(); i++) {
				if(buf.inUse(i)) {
					list.add(buf2.get(i));
					list.add(" ");
				}
			}
			if(!list.isEmpty()) list.remove(list.size() - 1);
			StringBuilder sb = new StringBuilder();
			for(int i = 0 ;i < list.size(); i++) {
				sb.append(list.get(i));
			}
			debug.printl(sb.toString());
		} catch (Exception e) {
			debug.pstm(e, "Unable to present you numbers");
		}
	}

}

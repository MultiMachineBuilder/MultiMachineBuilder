/**
 * 
 */
package mmb.DATA.databuffer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Allows to store values, which preserve their indices when preceding values are deleted
 * 
 * 
 * @author oskar
 *	@param T data type
 *	@param U saver
 */
public class DataBuffer<T> {

	public Saver<T> saver;
	/**
	 * Use delete() to delete values, rather than setting them to null
	 */
	private final List<T> data;
	
	private final List<Integer> emptyValues;
	
	
	
	private DataBuffer(Saver<T> saver, List<T> data, List<Integer> emptyValues) {
		super();
		this.saver = saver;
		this.data = data;
		this.emptyValues = emptyValues;
	}



	/**
	 * Reads a DataBuffer from a DataInputStream. Requires both type parameters
	 * 
	 * @param T type: data type
	 * @param U type: saver
	 *
	 * @param dis data source
	 * @param saver saver used to read and save data
	 * @return read data
	 * @throws IOException 
	 */
	@SuppressWarnings("unchecked")
	public static <T> DataBuffer<T> load(DataInputStream dis, Saver<T> saver) throws IOException{
	/*type parameters  output type  name          inputs                              exception*/
		int length = dis.readInt(); //load positions of empty values
		Integer[] ev = new Integer[length];
		for(int i = 0; i < length; i++) ev[i] = dis.readInt();
		Arrays.sort(ev);
		
		
		length = dis.readInt(); 
		Object[] d = new Object[length];
		
		int i = 0;
		int j = 0; //determine if current position is empty
		
		loop1:
		while(true) {
			if(j == ev.length) {
				break loop1;
			}
			if(ev[j] == i) {
				d[i] = null;
				j++;
			}else {
				d[i] = saver.read(dis);
			}
			i++;
			if(i == length) {
				break;
			}
		}
		
		while(true) {
			d[i] = saver.read(dis);
			i++;
			if(i == length) {
				break;
			}
		}
		return new DataBuffer<T>(saver, Arrays.asList((T[]) d), Arrays.asList(ev));
	}
	
	public void save(DataOutputStream dos) throws IOException {
		//Find null values
		List<Integer> ev = new ArrayList<Integer>();
		List<T> toSave = new ArrayList<T>();
		
		for(int i = 0; i < data.size(); i++) {
			T dat = data.get(i);
			if(dat == null) {
				ev.add(i);
			}else {
				toSave.add(dat);
			}
		}
		
		//Write empty positions
		dos.writeInt(emptyValues.size());
		for(int i = 0; i < emptyValues.size(); i++) {
			dos.writeInt(emptyValues.get(i));
		}
		
		//Write data
		dos.writeInt(data.size());
		for(int i = 0; i < toSave.size(); i++) {
			saver.save(dos, toSave.get(i));
		}
	}
	
	public static <T> DataBuffer<T> empty(Saver<T> saver) {
		return new DataBuffer<T>(saver, new ArrayList<T>(), new ArrayList<Integer>());
	}
	
	/**
	 * 
	 * @param index
	 * @return
	 * @throws ArrayInde
	 */
	public T get(int index) {
		return data.get(index);
	}
	
	public void replace(int index, T value) {
		data.set(index, value);
	}
	
	public int create(T value) {
		if(emptyValues.isEmpty()) {
			//If all values are used, extend list
			data.add(value);
			return data.size() - 1;
		}
		//Reuse old index
		int index;
		loop:
		while(true) {
			index = emptyValues.get(0);
			emptyValues.remove(0);
			if(get(index) == null){
				break loop;
			}
			if(emptyValues.isEmpty()) {
				return create(value);
			}
		}
		replace(index, value);
		return index;
	}
	
	public int size() {
		return data.size();
	}
	
	public void destroy(int index) {
		if(get(index) != null) {
			replace(index, null);
			emptyValues.add(index);
		}
	}
	public boolean inUse(int index) {
		return  index < size() && get(index) != null && !emptyValues.contains(index);
	}
}

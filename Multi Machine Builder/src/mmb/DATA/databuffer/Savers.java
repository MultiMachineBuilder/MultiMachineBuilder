package mmb.DATA.databuffer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import mmb.WORLD.tileworld.map.TileMap;

public final class Savers {

	public final static Saver<Byte> byteSaver = new Saver<Byte>() {

		@Override
		public void save(DataOutputStream dos, Byte data) throws IOException {
			dos.write(new byte[]{data});
		}

		@Override
		public Byte read(DataInputStream dis) throws IOException {
			return dis.readByte();
		}
	};
	
	public final static Saver<Short> shortSaver = new Saver<Short>() {

		@Override
		public void save(DataOutputStream dos, Short data) throws IOException {
			dos.writeShort(data);
		}

		@Override
		public Short read(DataInputStream dis) throws IOException {
			return dis.readShort();
		}
	};
	
	public final static Saver<Integer> intSaver = new Saver<Integer>() {

		@Override
		public void save(DataOutputStream dos, Integer data) throws IOException {
			dos.writeInt(data);
		}

		@Override
		public Integer read(DataInputStream dis) throws IOException {
			return dis.readInt();
		}
	};
	
	public final static Saver<Long> longSaver = new Saver<Long>() {

		@Override
		public void save(DataOutputStream dos, Long data) throws IOException {
			dos.writeLong(data);
		}

		@Override
		public Long read(DataInputStream dis) throws IOException {
			return dis.readLong();
		}
	};
	
	public final static Saver<String> txtSaver = new Saver<String>() {

		@Override
		public void save(DataOutputStream dos, String data) throws IOException {
			dos.writeUTF(data);
		}

		@Override
		public String read(DataInputStream dis) throws IOException {
			return dis.readUTF();
		}
	};

	public final static Saver<Byte[]> bytesSaver = Savers.arraySaver(byteSaver);
	public final static Saver<Short[]> shortsSaver = Savers.arraySaver(shortSaver);
	public final static Saver<Integer[]> intsSaver = Savers.arraySaver(intSaver);
	public final static Saver<Long[]> longsSaver = Savers.arraySaver(longSaver);
	public final static Saver<String[]> txtsSaver = Savers.arraySaver(txtSaver);
	
	
	public static <T> Saver<T[]> arraySaver(Saver<T> original){
		return new Saver<T[]>() {
			final Saver<T> svr = original;
			@Override
			public void save(DataOutputStream dos, T[] data) throws IOException {
				dos.writeInt(data.length);
				for (int i = 0; i < data.length; i++) {
					svr.save(dos, data[i]);
				}
			}

			@SuppressWarnings("unchecked")
			@Override
			public T[] read(DataInputStream dis) throws IOException {
				T[] data = (T[]) new Object[dis.readInt()];
				for(int i = 0; i < data.length; i++) {
					data[i] = svr.read(dis);
 				}
				return data;
			}
			
		};
	}
	
	/*public final static Saver<TileMap> mapSaver = new Saver<TileMap>() {

		@Override
		public void save(DataOutputStream dos, TileMap data) throws IOException {
			dos.writeInt(data.startX);
			dos.writeInt(data.startY);
			dos.writeInt(data.sizeX);
			dos.writeInt(data.sizeY);
			for(int i = 0; i < data.size; i++) {
				dos.writeInt(data.data[i]);
			}
			dos.flush();
		}

		@Override
		public TileMap read(DataInputStream dis) throws IOException {
			int startX = dis.readInt();
			int startY = dis.readInt();
			int sizeX = dis.readInt();
			int sizeY = dis.readInt();
			int size = sizeX * sizeY;
			int[] data = new int[size];
			for(int i = 0; i < size; i++) {
				data[i] = dis.readInt();
			}
			return new TileMap(startX, startY, sizeX, sizeY, data);
		}
		
	};*/
}

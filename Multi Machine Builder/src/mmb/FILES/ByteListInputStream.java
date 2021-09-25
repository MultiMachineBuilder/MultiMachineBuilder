/**
 * 
 */
package mmb.FILES;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.annotation.Nonnull;

import it.unimi.dsi.fastutil.bytes.ByteCollection;
import it.unimi.dsi.fastutil.bytes.ByteIterator;
import it.unimi.dsi.fastutil.bytes.ByteList;
import it.unimi.dsi.fastutil.bytes.ByteListIterator;

/**
 * @author oskar
 *
 */
public class ByteListInputStream{
	private ByteListInputStream() {}
	/**
	 * If given iterable is a boxed collection, the returned input stream throws IOException when a null value is encountered
	 * If given iterable is a list, the returned input stream supports mark() and reset()
	 * If given iterable is a collection, the returned input stream supports avaliable()
	 * @param list iterable to convert to input stream
	 * @return the input stream for the iterable
	 */
	@Nonnull public static InputStream of(Iterable<Byte> list) {
		if(list instanceof ByteList) return new SpecialList((ByteList) list);
		if(list instanceof ByteCollection) return new SpecialCollect((ByteCollection) list);
		if(list instanceof List) return new BoxedList((List<Byte>) list);
		if(list instanceof Collection) return new BoxedCollect((Collection<Byte>) list);
		return of(list);
	}
	@Nonnull public static InputStream of(Iterator<Byte> list) {
		if(list instanceof ByteIterator){
			return new Special((ByteIterator) list);
		}
		return new Boxed(list);
	}
}
class Boxed extends InputStream{
	private final Iterator<Byte> iter;
	Boxed(Iterator<Byte> iter){
		this.iter = iter;
	}
	@Override
	public int read() throws IOException {
		if(iter.hasNext()) {
			Byte next = iter.next();
			if(next == null) throw new IOException("Null encountered during iteration");
			return Byte.toUnsignedInt(next.byteValue());
		}
		return -1;
	}

	@Override
	public long skip(long n) throws IOException {
		for(long i = 0; i < n; i++) {
			if(!iter.hasNext()) return i;
			iter.next();
		}
		return n;
	}
}
class Special extends InputStream{
	private final ByteIterator iter;
	Special(ByteIterator iter){
		this.iter = iter;
	}
	@Override
	public int read() throws IOException {
		if(iter.hasNext()) 
			return iter.nextByte();
		return -1;
	}
	@Override
	public long skip(long n) throws IOException {
		for(long i = 0; i < n; i++) {
			if(!iter.hasNext()) return i;
			iter.nextByte();
		}
		return n;
	}
}
class BoxedCollect extends InputStream{
	private final Iterator<Byte> iter;
	private final Collection<Byte> collect;
	private int positions;
	BoxedCollect(Collection<Byte> iter){
		this.iter = iter.iterator();
		this.collect = iter;
	}
	@Override
	public int read() throws IOException {
		if(iter.hasNext()) {
			Byte next = iter.next();
			if(next == null) throw new IOException("Null encountered during iteration");
			positions++;
			return Byte.toUnsignedInt(next.byteValue());
		}
		return -1;
	}
	@Override
	public long skip(long n) throws IOException {
		for(long i = 0; i < n; i++) {
			if(!iter.hasNext()) {
				positions += i;
				return i;
			}
			iter.next();
			
		}
		positions += n;
		return n;
	}
	@Override
	public int available() throws IOException {
		return collect.size() - positions;
	}
}
class SpecialCollect extends InputStream{
	private final ByteCollection collect;
	private final ByteIterator iter;
	private int positions;
	SpecialCollect(ByteCollection iter){
		this.iter = iter.iterator();
		collect = iter;
	}
	@Override
	public int read() throws IOException {
		if(iter.hasNext()) 
			return iter.nextByte();
		return -1;
	}
	@Override
	public long skip(long n) throws IOException {
		for(long i = 0; i < n; i++) {
			if(!iter.hasNext()) {
				positions += i;
				return i;
			}
			iter.nextByte();
		}
		positions += n;
		return n;
	}
}
class BoxedList extends InputStream{
	private final ListIterator<Byte> iter;
	private final Collection<Byte> collect;
	private int positions;
	private int mark = -1;
	private int maxMark = -1;
	BoxedList(List<Byte> iter){
		this.iter = iter.listIterator();
		this.collect = iter;
	}
	@Override
	public int read() throws IOException {
		if(iter.hasNext()) {
			Byte next = iter.next();
			if(next == null) throw new IOException("Null encountered during iteration");
			positions++;
			return Byte.toUnsignedInt(next.byteValue());
		}
		return -1;
	}
	@Override
	public long skip(long n) throws IOException {
		for(long i = 0; i < n; i++) {
			if(!iter.hasNext()) {
				positions += i;
				return i;
			}
			iter.next();
			
		}
		positions += n;
		return n;
	}
	@Override
	public int available() throws IOException {
		return collect.size() - positions;
	}
	@Override
	public synchronized void mark(int n) {
		mark = positions;
		maxMark = mark+n;
	}
	@Override
	public boolean markSupported() {
		return true;
	}
	@Override
	public synchronized void reset() throws IOException {
		if(positions >= maxMark) {
			if(mark == -1) throw new IOException("Not yet marked");
			throw new IOException("Mark expired "+(positions-maxMark)+" bytes ago, at"+maxMark);
		}
		for(;positions > mark; positions--) {
			iter.previous();
		}
	}
}
class SpecialList extends InputStream{
	private final ByteList collect;
	private final ByteListIterator iter;
	private int positions;
	private int mark = -1;
	private int maxMark = -1;
	SpecialList(ByteList iter){
		this.iter = iter.iterator();
		collect = iter;
	}
	@Override
	public int read() throws IOException {
		if(iter.hasNext()) 
			return iter.nextByte();
		return -1;
	}
	@Override
	public long skip(long n) throws IOException {
		for(long i = 0; i < n; i++) {
			if(!iter.hasNext()) {
				positions += i;
				return i;
			}
			iter.nextByte();
		}
		positions += n;
		return n;
	}
	@Override
	public int available() throws IOException {
		return collect.size() - positions;
	}
	@Override
	public synchronized void mark(int n) {
		mark = positions;
		maxMark = mark+n;
	}
	@Override
	public boolean markSupported() {
		return true;
	}
	@Override
	public synchronized void reset() throws IOException {
		if(positions >= maxMark) {
			if(mark == -1) throw new IOException("Not yet marked");
			throw new IOException("Mark expired "+(positions-maxMark)+" bytes ago, at"+maxMark);
		}
		positions -= iter.back(positions - mark);
	}
}

/**
 * 
 */
package mmb.DATA.lists;

import java.util.function.BiFunction;

import org.joml.Vector3i;

import io.vavr.Function3;

/**
 * @author oskar
 *
 */
public final class Lists3D {
	@SuppressWarnings("null")
	public static <A, B, C, R> ReadWriteList3D<R> crossover(A[] a, B[] b, C[] c, Function3<A, B, C, R> f) {
		ReadWriteList3D<R> list = new ReadWriteList3D<R>(a.length, b.length, c.length);
		for(int i = 0; i < a.length; i++) {
			for(int j = 0; j < b.length; j++) {
				for(int k = 0; k < c.length; k++) {
					list.set(i, j, k, f.apply(a[i], b[j], c[k]));
				}
			}
		}
		return list;
	}
	@SuppressWarnings("null")
	public static <T, U, R> ReadWriteList3D<R> merge(ReadList3D<T> a, ReadList3D<U> b, BiFunction<T, U, R> f){
		Vector3i sizeA = a.dimensions();
		Vector3i sizeB = b.dimensions();
		int x = Math.min(sizeA.x, sizeB.x);
		int y = Math.min(sizeA.y, sizeB.y);
		int z = Math.min(sizeA.z, sizeB.z);
		ReadWriteList3D<R> list = new ReadWriteList3D<R>(x, y, z);
		for(int i = 0; i < x; i++) {
			for(int j = 0; j < y; j++) {
				for(int k = 0; k < z; k++) {
					list.set(i, j, k, f.apply(a.get(i, j, k), b.get(i, j, k)));
				}
			}
		}
		return list;
	}
	public static <T> ReadWriteList3D<T> generate(Vector3i size, DDDGenerator<T> f){
		return generate(size.x, size.y, size.z, f);
	}
	public static <T> ReadWriteList3D<T> generate(int x, int y, int z, DDDGenerator<T> f){
		ReadWriteList3D<T> list = new ReadWriteList3D<T>(x, y, z);
		for(int i = 0; i < x; i++) {
			for(int j = 0; j < y; j++) {
				for(int k = 0; k < z; k++) {
					list.set(i, j, k, f.generate(i, j, k));
				}
			}
		}
		return list;
	}
	@FunctionalInterface
	public static interface DDDGenerator<T>{
		public T generate(int x, int y, int z);
	}
}

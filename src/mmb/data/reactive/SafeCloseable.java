package mmb.data.reactive;

public interface SafeCloseable extends AutoCloseable {
	@Override
	void close();
}

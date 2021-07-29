/**
 * 
 */
package mmb.SERVICE;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

/**
 * @author oskar
 * A service is an object, which runs a part of the game.
 * Services can contain resources
 */
public abstract class Service implements AutoCloseable {
	/**
	 * Called when service is started.
	 * @param owner the object, which holds the service
	 */
	protected abstract void openService(Object owner);
	
	/**
	 * @param request the requested operation
	 * @param out the stream to send the response to. 
	 * @param client the player which made a request
	 * @throws IOException when download or upload fails
	 * @throws ServiceException when service processing fails
	 */
	public abstract void processRequest(Request request, OutputStream out, Client client) throws IOException, ServiceException;
	
	private final List<AutoCloseable> resources = new ArrayList<>();
	protected final void addResource(AutoCloseable closeable) {
		resources.add(closeable);
	}
	protected final void removeResource(AutoCloseable closeable) {
		resources.remove(closeable);
	}
	@Override
	public final void close() throws Exception{
		@Nullable Exception ex = null;
		for(AutoCloseable closeable: resources) {
			try {
				closeable.close();
			}catch(Exception e) {
				if(ex == null) ex = new Exception("At least one component failed to close");
				ex.addSuppressed(e);
			}
		}
		if(ex != null) throw ex;
	}
}

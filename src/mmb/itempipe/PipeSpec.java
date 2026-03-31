package mmb.itempipe;

import mmb.annotations.Nil;
import mmb.engine.rotate.Side;

/**
 * Defines pipe connections
 * @param u target side for requests from the top
 * @param d target side for requests from the down
 * @param l target side for requests from the left
 * @param r target side for requests from the right
 */
public record PipeSpec(@Nil Side u, @Nil Side d, @Nil Side l, @Nil Side r) {
	
}

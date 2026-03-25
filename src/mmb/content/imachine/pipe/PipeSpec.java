package mmb.content.imachine.pipe;

import mmb.annotations.Nil;
import mmb.engine.rotate.Side;

/**
 * Defines a pipe
 */
public record PipeSpec(@Nil Side u, @Nil Side d, @Nil Side l, @Nil Side r) {

}

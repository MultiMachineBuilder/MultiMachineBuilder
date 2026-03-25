package mmb.handler;

import java.util.Objects;

import mmb.engine.rotate.Side;

public record HandlerKey(int x, int y, Side side) {
	public HandlerKey{
		Objects.requireNonNull(side, "side is null");
	}
}

/**
 * 
 */
package mmb.content.electric;

import java.util.Iterator;
import java.util.function.Consumer;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Iterators;

import mmb.NN;
import mmb.Nil;
import mmb.engine.MMBUtils;
import mmb.engine.json.JsonTool;
import mmb.engine.rotate.Side;
import mmbbase.beans.Saver;
import mmbbase.data.variables.ListenerBooleanVariable;

/**
 * @author oskar
 *
 */
public final class SideConfig implements
Cloneable, Iterable<mmb.content.electric.SideConfig.SideBoolean>, Saver{
	/**
	 * Creates a side config with all values set to false
	 */
	public SideConfig() {
		//no initialization necessary
	}
	/**
	 * Creates a side config with vales et
	 * @param u upper setting
	 * @param d lower setting
	 * @param l left setting
	 * @param r right setting
	 */
	public SideConfig(boolean u, boolean d, boolean l, boolean r) {
		U.setValue(u);
		D.setValue(d);
		L.setValue(l);
		R.setValue(r);
	}
	/**
	 * Copies the input
	 * @param cfg value to copy
	 */
	public SideConfig(SideConfig cfg) {
		U.setValue(cfg.U.getValue());
		D.setValue(cfg.D.getValue());
		L.setValue(cfg.L.getValue());
		R.setValue(cfg.R.getValue());
	}
	/** Upper side of this config */
	public final ListenerBooleanVariable U = new ListenerBooleanVariable();
	/** Lower side of this config */
	public final ListenerBooleanVariable D = new ListenerBooleanVariable();
	/** Left side of this config */
	public final ListenerBooleanVariable L = new ListenerBooleanVariable();
	/** Right side of this config */
	public final ListenerBooleanVariable R = new ListenerBooleanVariable();
	
	@Override
	/**
	 * Copies this side config. The config will be equal to current one
	 */
	public SideConfig clone() {
		return new SideConfig(U.getValue(), D.getValue(), L.getValue(), R.getValue());
	}
	@Override
	public int hashCode() {
		final int prime = 2;
		int result = 1;
		result = prime * result + MMBUtils.bool2int(U.getValue());
		result = prime * result + MMBUtils.bool2int(D.getValue());
		result = prime * result + MMBUtils.bool2int(L.getValue());
		result = prime * result + MMBUtils.bool2int(R.getValue());
		return result;
	}
	@Override
	public boolean equals(@Nil Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SideConfig other = (SideConfig) obj;
		return (U.getValue() == other.U.getValue())
			&& (D.getValue() == other.D.getValue())
			&& (L.getValue() == other.L.getValue())
			&& (R.getValue() == other.R.getValue());
	}
	@Override
	public String toString() {
		return "[U"+U.getValue()+", D"+D.getValue()+", L"+L.getValue()+", R"+R.getValue()+"]";
	}
	
	public static class SideBoolean implements Comparable<SideBoolean>{
		public final boolean value;
		@NN public final Side side;
		private final String toString;
		@Override
		public String toString() {
			return toString;
		}
		public final int index;
		private SideBoolean(boolean value, Side side) {
			this.value = value;
			this.side = side;
			this.toString = (value ? "T_" : "F_")+side.toString();
			this.index = (side.ordinal()*2)+MMBUtils.bool2int(value);
		}
		@NN public static final SideBoolean T_U = new SideBoolean(true, Side.U);
		@NN public static final SideBoolean T_D = new SideBoolean(true, Side.D);
		@NN public static final SideBoolean T_L = new SideBoolean(true, Side.L);
		@NN public static final SideBoolean T_R = new SideBoolean(true, Side.R);
		@NN public static final SideBoolean T_UL = new SideBoolean(true, Side.UL);
		@NN public static final SideBoolean T_UR = new SideBoolean(true, Side.UR);
		@NN public static final SideBoolean T_DL = new SideBoolean(true, Side.DL);
		@NN public static final SideBoolean T_DR = new SideBoolean(true, Side.DR);
		@NN public static final SideBoolean F_U = new SideBoolean(false, Side.U);
		@NN public static final SideBoolean F_D = new SideBoolean(false, Side.D);
		@NN public static final SideBoolean F_L = new SideBoolean(false, Side.L);
		@NN public static final SideBoolean F_R = new SideBoolean(false, Side.R);
		@NN public static final SideBoolean F_UL = new SideBoolean(false, Side.UL);
		@NN public static final SideBoolean F_UR = new SideBoolean(false, Side.UR);
		@NN public static final SideBoolean F_DL = new SideBoolean(false, Side.DL);
		@NN public static final SideBoolean F_DR = new SideBoolean(false, Side.DR);
		@Override
		public int compareTo(@SuppressWarnings("null") SideBoolean o) {
			return Integer.compare(index, o.index);
		}
		@Override
		public int hashCode() {
			return index;
		}
		@Override
		public boolean equals(@Nil Object obj) {
			if(obj instanceof SideBoolean) 
				return ((SideBoolean) obj).index == index;
			return false;
		}
		public static @NN SideBoolean of(boolean value, Side side) {
			switch(side) {
			case D:
				return value ? T_D : F_D;
			case DL:
				return value ? T_DL : F_DL;
			case DR:
				return value ? T_DR : F_DR;
			case L:
				return value ? T_L : F_L;
			case R:
				return value ? T_R : F_R;
			case U:
				return value ? T_U : F_U;
			case UL:
				return value ? T_UL : F_UL;
			case UR:
				return value ? T_UR : F_UR;
			default:
				throw new IllegalArgumentException("Somehow an unknown side appeared: "+side);
			}
		}
	}

	@Override
	public Iterator<mmb.content.electric.SideConfig.SideBoolean> iterator() {
		return Iterators.forArray(SideBoolean.of(U.getValue(), Side.U),
				SideBoolean.of(D.getValue(), Side.D),
				SideBoolean.of(L.getValue(), Side.L),
				SideBoolean.of(R.getValue(), Side.R));
	}
	@Override
	public void forEach(@SuppressWarnings("null") Consumer<? super SideBoolean> c) {
		c.accept(SideBoolean.of(U.getValue(), Side.U));
		c.accept(SideBoolean.of(D.getValue(), Side.D));
		c.accept(SideBoolean.of(L.getValue(), Side.L));
		c.accept(SideBoolean.of(R.getValue(), Side.R));
	}
	public void set(Side s, boolean value) {
		switch(s) {
		case D:
			D.setValue(value);
			break;
		case L:
			L.setValue(value);
			break;
		case R:
			R.setValue(value);
			break;
		case U:
			U.setValue(value);
			break;
		default:
			break;
		}
	}
	public boolean get(Side s) {
		switch(s) {
		case U:
			return U.getValue();
		case D:
			return D.getValue();
		case L:
			return L.getValue();
		case R:
			return R.getValue();
		default:
			return false;
		}
	}
	@Override
	public JsonNode save() {
		return JsonTool.newArrayNode().add(U.getValue()).add(D.getValue()).add(L.getValue()).add(R.getValue());
	}
	@Override
	public void load(@Nil JsonNode data) {
		if(data == null) return;
		U.setValue(data.get(0).asBoolean());
		D.setValue(data.get(1).asBoolean());
		L.setValue(data.get(2).asBoolean());
		R.setValue(data.get(3).asBoolean());
	}
	/**
	 * @param cfgInElec
	 */
	public void set(SideConfig cfg) {
		U.setValue(cfg.U.getValue());
		D.setValue(cfg.D.getValue());
		L.setValue(cfg.L.getValue());
		R.setValue(cfg.R.getValue());
	}
}

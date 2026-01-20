package mmb.engine.recipe3;

import java.util.function.DoubleUnaryOperator;

/**
 * A simple linear function in form of mul*x + add.
 * Compositions using the built-in methods return linear functions.
 */
public record LinearFunction(
        double mul, //The factor in the equation. Default 1.
        double add //The constant in the equation. Default 0.
) implements DoubleUnaryOperator{
        public static final LinearFunction IDENTITY = new LinearFunction(1, 0);
        public static final LinearFunction ZERO = new LinearFunction(0, 0);
        public static final LinearFunction ONE = new LinearFunction(0, 1);
        public static LinearFunction fromMultiplier(double mul){
            return new LinearFunction(mul, 0);
        }
        public static LinearFunction fromAdder(double add){
            return new LinearFunction(1, add);
        }
        public static LinearFunction fromConstant(double value){
            return new LinearFunction(0, value);
        }

        public LinearFunction add(LinearFunction other){
            return add(other.mul, other.add);
        }
        public LinearFunction add(double add){
            return new LinearFunction(this.mul, this.add + add);
        }
        public LinearFunction add(double mul, double add){
            return new LinearFunction(this.mul + mul, this.add + add);
        }
        public LinearFunction sub(LinearFunction other){
            return sub(other.mul, other.add);
        }
        public LinearFunction sub(double mul, double add){
            return new LinearFunction(this.mul - mul, this.add - add);
        }
        public LinearFunction sub(double add){
            return new LinearFunction(this.mul, this.add - add);
        }
        public LinearFunction mul(double mul){
            return new LinearFunction(this.mul * mul, this.add * mul);
        }
        public LinearFunction div(double div){
            return new LinearFunction(this.mul / div, this.add / div);
        }
        public LinearFunction andThen(LinearFunction other){
            return new LinearFunction(this.mul * other.mul, other.add + this.mul * this.add);
        }
        public LinearFunction compose(LinearFunction other){
            return other.andThen(this);
        }
        @Override public double applyAsDouble(double operand){
            return (mul * operand) + add;
        }
        
        @Override public String toString() {
        	return toString(true, false);
        }
		public String toString(boolean allowRejectMul, boolean allowRejectAdd) {
			//Special cases
			if(allowRejectMul && mul == 0) return Double.toString(add);
			if(allowRejectAdd && add == 0) return Double.toString(mul) + "x";
			
			StringBuilder sb = new StringBuilder();
			if(allowRejectMul && mul == 1) 
				sb.append('x');
			else if(mul == -1)
				sb.append("-x");
			else
				sb.append(mul).append('x');
			sb.append(' ');
			
			if(add > 0) sb.append('+');
			sb.append(add);
			return sb.toString();
		}
}
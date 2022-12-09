/**
 * 
 */
package mmb.engine.gl;

import org.joml.Vector2f;
import org.joml.Vector2fc;

/**
 * @author oskar
 *
 */
public class ParallelogramTransform {
	private ParallelogramTransform() {}
	
	
	public static Transform2 demapParallelogram(Vector2fc beginA, Vector2fc firstA, Vector2fc secondA, Transform2 tf) {
		return demapParallelogram(beginA.x(), beginA.y(), firstA.x(), firstA.y(), secondA.x(), secondA.y(), tf);
	}
	/**
	 * 
	 * @param ax the A vertex on the image
	 * @param ay the A vertex on the image
	 * @param bx the B vertex on the image
	 * @param by the B vertex on the image
	 * @param cx the C vertex on the image
	 * @param cy the C vertex on the image
	 * @param tf target
	 * @return target
	 * 
	 * See the source for a clear image
	 * transform B-A-C to E-D-F
	 * 
	 *     A----------B
	 *    /          /
	 *   /          / 
	 *  C----------.
	 */
	public static Transform2 demapParallelogram(
			float ax, float ay,
			float bx, float by,
			float cx, float cy,
			Transform2 tf) {
		/*
		 * b1-a1-c1 to (1,0)-(0,0)-(0,1)
		 * Lets transform the first set to square (1,0)-(0,0)-(0,1) and call it f(x)
		 * 
		 * f(b1)=(1,0)
		 * f(a1)=(0,0)
		 * f(c1)=(0,1)
		 * f(b1+c1-a1)=(1,1)
		 * 
		 * u1 = b1-a1
		 * v1 = c1-a1
		 * 
		 * p1 = u*u1 + v*v1 + a1 = a1+s1+s2
		 * q1 = p1-a1 = u*u1 + v*v1
		 * f(p1) = (u, v)
		 * 
		 * s1 = v*u1
		 * s2 = v*v1
		 * Solve for (u, v) that 
		 */
		
		/*float ux = bx-ax;
		float uy = by-ay;
		float vx = cx-ax;
		float vy = cy-ay;
		
		if(ux == 0) {
			//Edge case: U is vertical, borks Y=AX+B, use X=A
			if(vx == 0) 
				//The V axis is horizontal, so is parallel and wont work
				throw new IllegalArgumentException("The axes A-B and A-C cannot be inline or parallel");
			else if(vy == 0) {
				//The V axis is perpendicular, it simplifies stuff, because it is a rectangle a -- b+c-a
				float secondX = bx+cx-ax;
				float secondY = by+cy-ay;
			}
			//The V axis is NOT horizontal, so it works
			/*
			 * Solve equation system:
			 * #u*aa + v*bb = 
			 * #u*cc + v*dd = 
			 */
			
			
			//Find the line L1 a-c
			/*float slope = vy/vx;
			float offset = (slope*ax)-ay;
			float calculatedY = (slope*bx)+offset;
			float calculatedX = bx;*/
			/*Intersect L1 any X=by
			 *AX+B
			 *
			*/
		/*}else if(uy == 0){
			//Use y=ax+b
			//Edge case: U is horizontal
			if(vx == 0) {
				//The V axis is perpendicular, it simplifies stuff, because it is a rectangle a -- b+c-a
				float secondX = bx+cx-ax;
				float secondY = by+cy-ay;
			}else if(vy == 0) {
				//The V axis is horizontal, so is parallel and wont work
				throw new IllegalArgumentException("The axes A-B and A-C cannot be inline or parallel");	
			}
		}else {
			//Use y=ax+b
			//Norm case: U is diagonal
		}*/
		
		Transform2 intermediate = mapParallelogram(ax, ay, bx, by, cx, cy, new Transform2());
		return tf.inverse(intermediate);
	}
	
	/**
	 * 
	 * @param ax the A vertex on the image
	 * @param ay the A vertex on the image
	 * @param bx the B vertex on the image
	 * @param by the B vertex on the image
	 * @param cx the C vertex on the image
	 * @param cy the C vertex on the image
	 * @param tf target
	 * @return target
	 * 
	 * See the source for a clear image
	 * transform square to B-A-C
	 * 
	 *     A----------B
	 *    /          /
	 *   /          / 
	 *  C----------.
	 */
	public static Transform2 mapParallelogram(
			float ax, float ay,
			float bx, float by,
			float cx, float cy,
			Transform2 tf) {
		//offset: ax, ay
		//X-to-: (bx-ax),(by-ay)
		//Y-to-: (cx-ax),(cy-ay)
		tf.offset.x = ax;
		tf.offset.y = ay;
		tf.mat.m00 = bx-ax;
		tf.mat.m01 = by-ay;
		tf.mat.m10 = cx-ax;
		tf.mat.m10 = cy-ay;
		return tf;
	}
	
	
	/*
	 * b1-a1-c1 to b2-a2-c2
	 * (xb1,yb1)-(xa1,ya1)-(xc1,yc1) to (xb2,yb2)-(xa2,ya2)-(xc2,yc2)
	 * Lets transform the first set to square (1,0)-(0,0)-(0,1) and call it f(x)
	 * And the entrirety h(x)
	 * 
	 * 
	 * 
	 * f(b1)=(1,0)       | h(b1)=b2
	 * f(a1)=(0,0)       | h(a1)=a2
	 * f(c1)=(0,1)       | h(c1)=c2
	 * f(b1+c1-a1)=(1,1) | h(b1+c1-a1)=b2+c2-a2
	 * 
	 * u1 = b1-a1
	 * v1 = c1-a1
	 * 
	 * f(u*u1 + v*v1 + a1) = (u, v)
	 * 
	 * q2 = v*u1
	 * q3 = v*v1
	 * x = u*u1 + v*v1 + a1 = a1+q2+q3
	 * q1 = x-a1 = u*u1 + v*v1
	 * 
	 */
}

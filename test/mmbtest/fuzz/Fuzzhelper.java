/**
 * 
 */
package mmbtest.fuzz;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.*;

import mmb.NN;
import mmb.engine.json.JsonTool;

/**
 *
 * @author oskar
 *
 */
public class Fuzzhelper {
	public static int randomSize(FuzzedDataProvider fdp) {
		int sizeclass = fdp.consumeInt();
		int sizeclasshob = Integer.highestOneBit(sizeclass);
		int sizeclasshobsqr = sizeclasshob * sizeclasshob;
		int sizelimit = Integer.reverse(sizeclasshobsqr);
		if(sizelimit == Integer.MIN_VALUE) sizelimit = 1;
		if(sizelimit < 8) sizelimit = 8;
		int length = fdp.consumeInt(0, sizelimit);
		return length;
	}
	@NN public static JsonNode generateJsonNode(FuzzedDataProvider fdp) {
		int type = fdp.consumeInt(0, 10);
		switch(type) {
		case 0:
			//Boolean
			return BooleanNode.valueOf(fdp.consumeBoolean());
		case 1:
			//32-bit integer
			return new IntNode(fdp.consumeInt());
		case 2:
			//64-bit integer
			return new LongNode(fdp.consumeLong());
		case 3:
			//32-bit float
			return new FloatNode(Float.intBitsToFloat(fdp.consumeInt()));
		case 4:
			//64-bit float
			return new DoubleNode(Double.longBitsToDouble(fdp.consumeLong()));
		case 5:
			//Array
			int length = randomSize(fdp);
			ArrayNode node = new ArrayNode(JsonNodeFactory.instance, length);
			for(int i = 0; i < length; i++) {
				node.add(generateJsonNode(fdp));
			}
			return node;
		case 6:
			//Object
			int length2 = randomSize(fdp);
			ObjectNode node2 = JsonTool.newObjectNode();
			for(int i = 0; i < length2; i++) {
				int stringsize = randomSize(fdp);
				node2.set(fdp.consumeAsciiString(stringsize), generateJsonNode(fdp));
			}
			return node2;
		case 7:
			//String (ASCII)
			int stringsize = randomSize(fdp);
			return new TextNode(fdp.consumeAsciiString(stringsize));
		case 8:
			//String (Unicode)
			int stringsize2 = randomSize(fdp);
			return new TextNode(fdp.consumeString(stringsize2));
		default:
			//Null
			return NullNode.instance;
		}
	}
}

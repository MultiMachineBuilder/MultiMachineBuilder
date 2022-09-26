/**
 * 
 */
package mmb;

import java.lang.reflect.*;

import org.ainslec.picocog.PicoWriter;

import mmb.debug.Debugger;

/**
 * @author oskar
 *
 */
public class IntrospectClasses {
	private IntrospectClasses() {}
	public static String introspect(Class<?> cls) {
		PicoWriter writer = new PicoWriter();
		Field[] fields = cls.getDeclaredFields();
		for(Field field: fields) {
			writer.writeln(field2str(field));
		}
		Method[] methods = cls.getDeclaredMethods();
		for(Method method: methods) {
			writer.writeln(method2str(method));
		}
		return writer.toString();
	}
	public static String field2str(Field field) {
		StringBuilder sb = new StringBuilder();
		if((field.getModifiers() & Modifier.PUBLIC) != 0) sb.append("public ");
		if((field.getModifiers() & Modifier.PRIVATE) != 0) sb.append("private ");
		if((field.getModifiers() & Modifier.PROTECTED) != 0) sb.append("protected ");
		if((field.getModifiers() & Modifier.STATIC) != 0) sb.append("static ");
		if((field.getModifiers() & Modifier.VOLATILE) != 0) sb.append("volatile ");
		if((field.getModifiers() & Modifier.TRANSIENT) != 0) sb.append("transient ");
		sb.append(field.getType().getTypeName()).append(' ');
		sb.append(field.getName());
		return sb.toString();
	}
	public static String method2str(Method method) {
		StringBuilder sb = new StringBuilder();
		if((method.getModifiers() & Modifier.PUBLIC) != 0) sb.append("public ");
		if((method.getModifiers() & Modifier.PRIVATE) != 0) sb.append("private ");
		if((method.getModifiers() & Modifier.PROTECTED) != 0) sb.append("protected ");
		if((method.getModifiers() & Modifier.STATIC) != 0) sb.append("static ");
		if((method.getModifiers() & Modifier.VOLATILE) != 0) sb.append("volatile ");
		if((method.getModifiers() & Modifier.TRANSIENT) != 0) sb.append("transient ");
		sb.append(method.getReturnType().getTypeName()).append(' ');
		sb.append(method.getName()).append('(');
		for(Parameter param: method.getParameters()) {
			sb.append(param2str(param)).append(',');
		}
		sb.append(");");
		return sb.toString();
	}
	public static String param2str(Parameter param) {
		return param.getType().getTypeName()+' '+param.getName();
	}
	public static String classname(Class<?> cls) {
		if(cls.isArray()) return classname(cls.getComponentType());
		if(cls == Integer.TYPE) 
			return "int";
		if(cls == Long.TYPE) 
			return "int";
		if(cls == Byte.TYPE) 
			return "byte";
		if(cls == Short.TYPE) 
			return "short";
		if(cls == Float.TYPE) 
			return "float";
		if(cls == Double.TYPE) 
			return "double";
		if(cls == Boolean.TYPE) 
			return "boolean";
		if(cls == Character.TYPE) 
			return "char";
		if(cls == Void.TYPE) 
			return "void";
		
		return null;
		
	}
}

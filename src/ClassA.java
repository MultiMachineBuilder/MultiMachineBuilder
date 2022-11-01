import javax.annotation.Nonnull;
import javax.annotation.Nullable;
public class ClassA<@Nonnull T> {
	public @Nullable T method1(@Nullable T input){
		return input;
	}
	public static void main(String[] args){
		ClassA<String> objA = new ClassA<>();
		String str = null;
		//🡓inferred nonnull, expected nullable, warning on the input, even though it is marked nullable🡓
		String str2 = objA.method1(str);
		int len = str2.length(); //no error reported, even though this method would fail with a NPE
	}
}
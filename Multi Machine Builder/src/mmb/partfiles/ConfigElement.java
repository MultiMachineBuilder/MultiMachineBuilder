package mmb.partfiles;

import java.util.function.Consumer;

public interface ConfigElement {
	public boolean isClass();
	public boolean isKey();
	default public void doIfKey(Consumer<ConfigKey> exec) {
		if(ConfigKey.isThisType(this)) {
			exec.accept((ConfigKey) this);
		}
	}
	default public void doIfClass(Consumer<ConfigClass> exec) {
		if(ConfigClass.isThisType(this)) {
			exec.accept((ConfigClass) this);
		}
	}
}

package language;

public interface TranslationAddon extends Translation{
	
	//File corruption
	public String zipCorruption(String file);
	public String jsonCorruption(String file);
	public String pngCorruption(String file);
	public String nbtCorruption(String file);
	public String oggCorruption(String file);
	public String htmlCorruption(String file);
	public String iniCorruption(String file);
	
	public String fileUnavaliable(String file);
	
	//Relations
	public String loadAlso(String file);
	public String skip(String file);
	public String dependencyUnavaliable(String dep);
	public String 
	
}

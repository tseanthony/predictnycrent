package predictrent;

import java.io.File;

public class Resource {

	File file;
	
	public Resource(String fileName) {
		
		ClassLoader classLoader = getClass().getClassLoader();
		this.file = new File(classLoader.getResource(fileName).getFile());

	}
	
	public File getFile() {
		return file;
		
	}
	
}

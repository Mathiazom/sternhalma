package app.file;

public interface FileHandler<T> {
	
	public void saveToFile(String filePath, T object);
	
	public T readFromFile(String filePath);
	
}

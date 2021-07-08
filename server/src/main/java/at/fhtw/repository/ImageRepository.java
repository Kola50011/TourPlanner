package at.fhtw.repository;

public interface ImageRepository {
    boolean hasImage(String key);

    byte[] getImage(String key);

    void putImage(String key, byte[] value);
}

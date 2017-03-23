package pl.rzonsol.demo.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by rzonsol on 23.03.2017.
 */
@ConfigurationProperties("storage")
public class StorageProperties {
    /**
     * Folder location for storing files
     */
    private String location = "upload-dir";

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


}

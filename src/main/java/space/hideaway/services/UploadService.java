package space.hideaway.services;

import org.csveed.api.CsvClient;
import org.csveed.api.CsvClientImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import space.hideaway.model.Data;

import java.io.*;
import java.util.List;
import java.util.UUID;

/**
 * HIDE CoCoTemp 2016
 * The class responsible for the parsing and upload of user-data.
 */
@Service
public class UploadService {

    @Autowired
    DeviceServiceImplementation deviceServiceImplementation;
    /**
     * The file uploaded by the user.
     */
    private MultipartFile multipartFile;
    @Autowired
    private DataServiceImplementation dataServiceImplementation;


    /**
     * Get the file uploaded by the user.
     *
     * @return The file uploaded by the user.
     */
    public MultipartFile getMultipartFile() {
        return multipartFile;
    }

    /**
     * Set the file uploaded by the user.
     *
     * @param multipartFile The new file uploaded by the user.
     * @return The UploadService class for method chaining.
     */
    public UploadService setMultipartFile(MultipartFile multipartFile) {
        this.multipartFile = multipartFile;
        return this;
    }

    /**
     * Parse the file, line-by-line.
     * This method is performance critical as user data will be a CSV file MANY MANY lines. Time spent here is
     * time wasted.
     * TODO: possible application of multithreading.
     */
    public String parseFile(String deviceKey) {
        File convertedFile = convertToFile();
        try (Reader reader = new FileReader(convertedFile)) {
            CsvClient<Data> csvClient = new CsvClientImpl<>(reader, Data.class);
            final List<Data> dataList = csvClient.readBeans();
            UUID id = deviceServiceImplementation.findByKey(deviceKey).getId();
            for (Data data : dataList) {
                data.setDeviceID(id);
            }
            dataServiceImplementation.batchSave(dataList);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private File convertToFile() {
        File convertedFile = null;
        try {
            convertedFile = File.createTempFile("temp-upload", ".csv");
            FileOutputStream fileOutputStream = new FileOutputStream(convertedFile);
            fileOutputStream.write(multipartFile.getBytes());
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return convertedFile;
    }
}

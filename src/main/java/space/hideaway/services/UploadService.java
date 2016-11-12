package space.hideaway.services;

import org.csveed.api.CsvClient;
import org.csveed.api.CsvClientImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import space.hideaway.model.Data;
import space.hideaway.model.Device;
import space.hideaway.model.User;

import java.io.*;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * HIDE CoCoTemp 2016
 * The class responsible for the parsing and upload of user-data.
 */
@Service
public class UploadService {

    @Autowired
    DeviceServiceImplementation deviceServiceImplementation;
    @Autowired
    UserServiceImplementation userService;
    /**
     * The file uploaded by the user.
     */
    private MultipartFile multipartFile;
    @Autowired
    private DataServiceImplementation dataServiceImplementation;
    @Autowired
    private SecurityService securityService;

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
        User user = userService.getCurrentLoggedInUser();
        Long byUsername = user.getId();
        if (isCorrectUser(user, deviceKey)) {
            File convertedFile = convertToFile();
            try (Reader reader = new FileReader(convertedFile)) {
                CsvClient<Data> csvClient = new CsvClientImpl<>(reader, Data.class);
                final List<Data> dataList = csvClient.readBeans();
                UUID id = deviceServiceImplementation.findByKey(deviceKey).getId();
                for (Data data : dataList) {
                    data.setUserID(Math.toIntExact(byUsername));
                    data.setDeviceID(id);
                }
                dataServiceImplementation.batchSave(dataList);
                return "{}";
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "{\"error\": \"Unauthorized upload: You many only upload to devices you maintain\"}";
    }

    private boolean isCorrectUser(User user, String deviceKey) {
        boolean found = false;
        Set<Device> deviceSet = user.getDeviceSet();
        for (Device device : deviceSet) {
            if (device.getId().toString().equals(deviceKey)) {
                found = true;
            }
        }
        return found;
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

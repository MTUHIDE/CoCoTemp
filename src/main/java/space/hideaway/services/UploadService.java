package space.hideaway.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.supercsv.cellprocessor.ParseDate;
import org.supercsv.cellprocessor.ParseDouble;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;
import space.hideaway.model.Data;
import space.hideaway.model.Device;
import space.hideaway.model.UploadHistory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
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
    UserManagementImpl userService;
    /**
     * The file uploaded by the user.
     */
    private MultipartFile multipartFile;
    @Autowired
    private DataServiceImplementation dataServiceImplementation;

    @Autowired
    private UploadHistoryService uploadHistoryService;

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
        if (deviceServiceImplementation.isCorrectUser(userService.getCurrentLoggedInUser(), deviceKey)) {
            Thread fileUploadThread = new Thread(
                    new FileUploadHandler(deviceServiceImplementation.findByKey(deviceKey), convertToFile())
            );
            fileUploadThread.start();
            return "{status: \"in progress\"}";
        }
        return "{status: \"failed\", message: \"You do not authorized to edit this device\"}";
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

    private class FileUploadHandler implements Runnable {

        private final File file;
        private final Device device;

        FileUploadHandler(Device device, File file) {
            this.file = file;
            this.device = device;
        }

        @Override
        public void run() {
            long start = System.currentTimeMillis();
            UUID deviceId = device.getId();
            Long userId = device.getUserId();
            ArrayList<Data> dataList = new ArrayList<>();
            ICsvBeanReader iCsvBeanReader;
            try {
                iCsvBeanReader = new CsvBeanReader(new FileReader(file), CsvPreference.STANDARD_PREFERENCE);
                final CellProcessor[] cellProcessors = new CellProcessor[]{
                        new ParseDate("yyyy-MM-dd HH:mm:ss", true, Locale.ENGLISH),
                        new ParseDouble()
                };
                final String[] header = iCsvBeanReader.getHeader(true);
                Data dataBean;
                while ((dataBean = iCsvBeanReader.read(Data.class, header, cellProcessors)) != null) {
                    dataBean.setDeviceID(deviceId);
                    dataBean.setUserID(userId.intValue());
                    dataList.add(dataBean);
                }
                dataServiceImplementation.batchSave(dataList);
                iCsvBeanReader.close();
                long end = System.currentTimeMillis();

                UploadHistory uploadHistory = new UploadHistory();
                uploadHistory.setDeviceID(deviceId);
                uploadHistory.setDuration(end - start);
                uploadHistory.setDescription("Data was uploaded successfully");
                uploadHistoryService.save(uploadHistory);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

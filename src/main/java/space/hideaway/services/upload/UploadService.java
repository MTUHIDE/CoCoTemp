package space.hideaway.services.upload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.supercsv.cellprocessor.ParseChar;
import org.supercsv.cellprocessor.ParseDate;
import org.supercsv.cellprocessor.ParseDouble;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;
import space.hideaway.model.Data;
import space.hideaway.model.site.Site;
import space.hideaway.model.upload.UploadHistory;
import space.hideaway.services.data.DataServiceImplementation;
import space.hideaway.services.site.SiteServiceImplementation;
import space.hideaway.services.user.UserServiceImplementation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.UUID;


@Service
public class UploadService
{

    private final SiteServiceImplementation siteServiceImplementation;
    private final UserServiceImplementation userService;
    private final DataServiceImplementation dataServiceImplementation;
    private final UploadHistoryService uploadHistoryService;
    private MultipartFile multipartFile;

    @Autowired
    public UploadService(
            DataServiceImplementation dataServiceImplementation,
            UploadHistoryService uploadHistoryService,
            SiteServiceImplementation siteServiceImplementation,
            UserServiceImplementation userService)
    {
        this.dataServiceImplementation = dataServiceImplementation;
        this.uploadHistoryService = uploadHistoryService;
        this.siteServiceImplementation = siteServiceImplementation;
        this.userService = userService;
    }

    /**
     * Sets the multipartFile. This file comes from the HTTP upload post. The file is csv
     * formatted and looks like the following:
     *
     * dateTime,temperatureStandard,temperature
     * 2014-07-24 09:15:54,C,23.3
     * 2014-07-24 10:15:54,C,22.6
     *
     * @param multipartFile the multipartFile
     * @return <code>this</code> upload object
     */
    public UploadService setMultipartFile(MultipartFile multipartFile)
    {
        this.multipartFile = multipartFile;
        return this;
    }

    public MultipartFile getMultipartFile(){
        return this.multipartFile;
    }
    /**
     * Takes the multipart file and parses the data to be saved in the database.
     * Use <code>setMultipartFile</code> to set the multipart file for parsing.
     * Uploads are parse and saved on a new thread, so not to slow down the server.
     *
     * @param siteKey The id of the site associated with the upload
     * @param description The user's description of the upload
     * @return The status of the upload: "" for no data, "in progress", or "failed"
     */
    public String parseFile(String siteKey, String description)
    {
        // Converts the multipartFile into a Java file object
        File file = convertToFile();

        // No data in the file
        if (file.length() == 0) return "";

        // Checks if the user owns the site associated with the upload
        if (siteServiceImplementation.isCorrectUser(userService.getCurrentLoggedInUser(), siteKey))
        {
            Thread fileUploadThread = new Thread(
                    new FileUploadHandler(siteServiceImplementation.findByKey(siteKey), file, description));

            fileUploadThread.start();
            return "{status: \"in progress\"}";
        }
        return "{status: \"failed\", message: \"You do not authorized to edit this site\"}";
    }

    /**
     * Converts the multipartFile into a Java file object.
     *
     * @return the data file
     */
    private File convertToFile()
    {
        File convertedFile = null;
        try
        {
            convertedFile = File.createTempFile("temp-upload", ".csv");
            FileOutputStream fileOutputStream = new FileOutputStream(convertedFile);
            fileOutputStream.write(multipartFile.getBytes());
            fileOutputStream.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return convertedFile;
    }

    /**
     * A runnable class to parse the data file.
     */
    private class FileUploadHandler implements Runnable
    {

        private final File file;
        private final Site site;
        private final String description;

        FileUploadHandler(Site site, File file, String description)
        {
            this.description = description;
            this.file = file;
            this.site = site;
        }

        /**
         * Handles the process of parsing and saving the temperature readings.
         */
        @Override
        public void run()
        {
            long start = System.currentTimeMillis();
            UUID siteId = site.getId();
            Long userId = site.getUserID();
            ArrayList<Data> dataList = new ArrayList<>();
            UploadHistory pendingHistory = new UploadHistory();


            // A class for converting csv objects into Java objects
            ICsvBeanReader iCsvBeanReader;

            try
            {
                iCsvBeanReader = new CsvBeanReader(new FileReader(file), CsvPreference.STANDARD_PREFERENCE);

                // Format settings
                final CellProcessor[][] standardCellProcessors = new CellProcessor[][]{
                        {
                                new ParseDate("yyyy-MM-dd HH:mm:ss", true, Locale.ENGLISH),
                                new ParseChar(),
                                new ParseDouble()
                        },
                        {
                                new ParseDate("yyyy-MM-dd HH:mm:ss", true, Locale.ENGLISH),
                                new ParseDouble()
                        }

                };

                int format = -1 ;

                final String formats[][]={{"dateTime","temp_standard","temperature"},{"dateTime","temperature"}};
                // Gets the header of the csv (i.e. dateTime, temperature)
                final String[] header = iCsvBeanReader.getHeader(true);

                for(int i=0;i<formats.length;i++) {
                   if(Arrays.equals(formats[i],header))
                   {
                       format = i ;
                   }

                }

                // A data object to contain the temperature and time.
                Data dataBean;
                if(format ==-1){
                    throw new Exception("Format not supported");
                }

                // Parse each row in the csv file into a data object
                while ((dataBean = iCsvBeanReader.read(Data.class, header, standardCellProcessors[format])) != null)
                {
                    if(format == 0 ) {
                        if (dataBean.getTemp_Standard() == 'F') {
                            double fahrenTemp = dataBean.getTemperature();
                            double celsiusTemp = (fahrenTemp - 32) * 5 / 9;

                            dataBean.setTemperature(celsiusTemp);
                            dataBean.setTemp_Standard('C');
                        }
                    }
                    else if(format == 1)
                    {
                        dataBean.setTemp_Standard('C');
                    }
                    dataBean.setSiteID(siteId);
                    dataBean.setUserID(userId.intValue());

                    if(dataServiceImplementation.findIfDataExistsAlready(siteId,dataBean.getDateTime(),userId.intValue(),dataBean.getTemperature())==null) {
                        dataList.add(dataBean);
                    }
                }
                if(!(dataList.isEmpty())) {
                    // Saves that a upload attempt was made by this user
                     pendingHistory = uploadHistoryService.savePending(
                            siteId,
                            Math.toIntExact(userId),
                            false,
                            0,
                            "In Progress",
                            0);
                    // Writes each data object into the database in batches, so not to overload the server
                    dataServiceImplementation.batchSave(site, dataList);
                    iCsvBeanReader.close();

                    long end = System.currentTimeMillis();

                    // Create a record that the file was parsed and saved correctly. Over writes pendingHistory.
                    uploadHistoryService.saveFinished(pendingHistory, false, end - start, dataList.size(), description);
                }
                else{
                    throw new Exception("No data in file provided");
                }

            } catch (Exception e) {

                uploadHistoryService.saveFinished(pendingHistory, true, 0, 0,
                        "Upload failed: " + String.format("%s%n %s", e.getMessage(), description));
            }
        }

    }

}

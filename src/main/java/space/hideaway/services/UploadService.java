package space.hideaway.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * HIDE CoCoTemp 2016
 * The class responsible for the parsing and upload of user-data.
 */
@Service
public class UploadService {

    /**
     * The file uploaded by the user.
     */
    private MultipartFile multipartFile;


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
    public void parseFile() {

        try (
                InputStream inputStream = multipartFile.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))
        ) {
            bufferedReader.lines().forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

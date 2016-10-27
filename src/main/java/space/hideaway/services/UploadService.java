package space.hideaway.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Created by dough on 10/26/2016.
 */
@Service
public class UploadService {

    MultipartFile multipartFile;


    public MultipartFile getMultipartFile() {
        return multipartFile;
    }

    public UploadService setMultipartFile(MultipartFile multipartFile) {
        this.multipartFile = multipartFile;
        return this;
    }

    public void parseFile() {

        try (
                InputStream  inputStream = multipartFile.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))
                ) {
            bufferedReader.lines().forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

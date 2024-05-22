package com.example.pathokdataloader;

import com.example.pathokdataloader.author.Author;
import com.example.pathokdataloader.author.AuthorRepository;
import jakarta.annotation.PostConstruct;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@SpringBootApplication
public class PathokDataLoaderApplication {

    @Autowired
    AuthorRepository authorRepository;

    @Value("${datadump.location.author}")
    private String authorDumpLocation;

    @Value("${datadump.location.works}")
    private String worksDumpLocation;

    public static void main(String[] args) {
        SpringApplication.run(PathokDataLoaderApplication.class, args);
    }

    private void initAuthors(){

        Path path = Paths.get(authorDumpLocation);

        try(Stream<String> lines = Files.lines(path)){

            lines.forEach(line ->{

                // Read and parse the line
                String jsonString = line.substring(line.indexOf("{"));

                try {

                    JSONObject jsonObject = new JSONObject(jsonString);

                    // Construct Author object
                    Author author = new Author();
                    author.setName(jsonObject.optString("name"));
                    author.setPersonalName(jsonObject.optString("personal_name"));
                    author.setAuthorId(jsonObject.optString("key").replace("/authors/", ""));

                    // Persist using repository
                    authorRepository.save(author);

                    System.out.println("Author saved named ::::::::: " + author.getName());

                }catch (JSONException e){
                    e.printStackTrace();
                }
            });

        }catch (IOException e){
            e.printStackTrace();
        }


    }

    private void initWorks(){

    }

    @PostConstruct
    public void start(){
        System.out.println("Application Started");
        initAuthors();


    }

}

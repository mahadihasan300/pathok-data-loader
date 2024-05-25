package com.example.pathokdataloader;

import com.example.pathokdataloader.author.Author;
import com.example.pathokdataloader.author.AuthorRepository;
import com.example.pathokdataloader.book.Book;
import com.example.pathokdataloader.book.BookRepository;
import jakarta.annotation.PostConstruct;
import org.json.JSONArray;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
public class PathokDataLoaderApplication {

    @Autowired
    AuthorRepository authorRepository;

    @Autowired
    BookRepository bookRepository;

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

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");

        Path path = Paths.get(worksDumpLocation);

        try(Stream<String> lines = Files.lines(path)){

            lines.forEach(line ->{

                // Read and parse the line
                String jsonString = line.substring(line.indexOf("{"));

                try {

                    JSONObject jsonObject = new JSONObject(jsonString);

                    // Construct Book object
                    Book book = new Book();
                    book.setBookId(jsonObject.getString("key").replace("/works/",""));
                    book.setName(jsonObject.optString("title"));

                    JSONObject descriptionObj = jsonObject.optJSONObject("description");
                    if(descriptionObj != null){
                        book.setDescription(descriptionObj.optString("value"));
                    }


                    JSONArray coversJSONArr =  jsonObject.optJSONArray("covers");
                    if(coversJSONArr != null){
                        List<String> coverIds = new ArrayList<>();
                        for(int i = 0; i < coversJSONArr.length(); i++){
                            coverIds.add(coversJSONArr.getString(i));
                        }
                        book.setCoverIds(coverIds);
                    }


                    JSONArray authorsJSONArr =  jsonObject.optJSONArray("authors");
                    if(authorsJSONArr != null){
                        List<String> authorIds = new ArrayList<>();
                        for(int i = 0; i < authorsJSONArr.length(); i++){
                            String authorId = authorsJSONArr.getJSONObject(i).getJSONObject("author").getString("key")
                                    .replace("/authors/","");
                            authorIds.add(authorId);
                        }
                        book.setAuthorIds(authorIds);

                        List<String> authorNames =  authorIds.stream().map(id -> authorRepository.findByAuthorId(id))
                                .map(optionalAuthor ->{
                                    if(!optionalAuthor.isPresent()) return "Unknown author";
                                    return optionalAuthor.get().getName();
                                }).collect(Collectors.toList());

                        book.setAuthorNames(authorNames);
                    }


                    JSONObject publishedDateObj = jsonObject.optJSONObject("created");
                    if(publishedDateObj != null){
                        String dateStr = publishedDateObj.optString("value");
                        book.setPublishedDate(LocalDate.parse(dateStr,dateTimeFormatter));
                    }


                    // Persist using repository
                    bookRepository.save(book);

                    System.out.println("Book saved named ::::::::: " + book.getName());

                }catch (JSONException e){
                    e.printStackTrace();
                }
            });

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @PostConstruct
    public void start(){
        System.out.println("Application Started");
        //initAuthors();
        //initWorks();


    }

}

package com.group2.projectmanagementapi.appusers.image;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final Path root = Paths.get("upload");
    private final ImageRepository imageRepository;
    
    public void init(){
        try{
            Files.createDirectories(root);
        } catch (IOException e){
            throw new RuntimeException("could not Initialize folder for upload!");
        }
    }

    public String save(MultipartFile file){
        try{
            Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()));
            return this.root.toString() + "/" +file.getOriginalFilename();
        } catch (Exception e){
            if (e instanceof FileAlreadyExistsException){
                throw new RuntimeException("A file of that name already exists.");
            }

            throw new RuntimeException(e.getMessage());
        }
    }

    public Resource load(Image image){
        try{
            Path file = Path.of(image.getUrl());
            Resource resource = new UrlResource(file.toUri());
            
            if (resource.exists() || resource.isReadable()){
                return resource;
            } else {
                throw new RuntimeException("could not read the file");
            }
        } catch (MalformedURLException e){
            throw new RuntimeException("Error : "+ e.getMessage());
        }
    }

    public void deleteImage(Image image){
        File file = new File(image.getUrl());
        if (!file.delete()){
            throw new FailedDeleteFileException("Failed to delete the image "+image.getName());
        }

    }

    public void deleteAll(){
        FileSystemUtils.deleteRecursively(root.toFile());
        imageRepository.deleteAll();

    }

    public Stream<Path> loadAll(){
        try{
            return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
            
        } catch (IOException e){
            throw new RuntimeException("Could not load the files!");
        }
    }
    
}

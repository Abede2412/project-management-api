package com.group2.projectmanagementapi.appusers;

import java.io.IOException;
import java.util.Optional;

import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.group2.projectmanagementapi.applicationuser.ApplicationUserService;
import com.group2.projectmanagementapi.appusers.image.Image;
import com.group2.projectmanagementapi.appusers.image.ImageNotFoundException;
import com.group2.projectmanagementapi.appusers.image.ImageService;
import com.group2.projectmanagementapi.authentication.model.UserPrincipal;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AppUserController {
    
    private final AppUserService appUserService;
    private final ImageService imageService;
    private final ApplicationUserService applicationUserService;

    @PostMapping("/register")
    public ResponseEntity<AppUserResponse> createOne(@RequestBody AppUserRequest appUserRequest){
        AppUser appUser = appUserRequest.convertToEntity();
        AppUser newAppUser = appUserService.createOne(appUser);
        AppUserResponse appUserResponse = newAppUser.convertToResponse();
        return ResponseEntity.status(201).body(appUserResponse);       
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/appusers")
    @Operation(summary = "get user by email or username, if parameter null get logged user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",description = "user is get", 
            content= @Content(mediaType = "application/json",
                schema = @Schema(implementation = AppUserResponse.class),
                examples = @ExampleObject())),
        @ApiResponse(responseCode = "404", description = "user not found"),
        @ApiResponse(responseCode = "400", description = "invalid request")
    })
    public ResponseEntity<AppUserResponse> getByUsernameOrEmail(@RequestParam("search") Optional<String> usernameOrEmail) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        AppUser loggedUser = applicationUserService.findById(userPrincipal.getId()).getAppUser();

        if (usernameOrEmail.isPresent()){
            AppUser appUser = appUserService.findByUsernameOrEmail(usernameOrEmail.get()).orElseThrow(AppUserNotFoundException::new);
            
            return ResponseEntity.ok().body(appUser.convertToResponse());
        }

        return ResponseEntity.ok().body(loggedUser.convertToResponse());
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/appusers/{id}")
    @Operation(summary = "get user by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",description = "user is get", 
            content= @Content(mediaType = "application/json",
                schema = @Schema(implementation = AppUserResponse.class),
                examples = @ExampleObject())),
        @ApiResponse(responseCode = "404", description = "user not found"),
        @ApiResponse(responseCode = "400", description = "invalid request")
    })
    public ResponseEntity<AppUserResponse> getById(@PathVariable("id") Long id) {
        AppUser appUser = appUserService.findById(id);
        return ResponseEntity.ok().body(appUser.convertToResponse());
    }

    @SecurityRequirement(name = "bearerAuth")
    @PatchMapping(value = "/appusers/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "upload image loggedUser")
    public ResponseEntity<byte[]> updateImageByMovie(@RequestPart("image") MultipartFile imageFile) throws IOException{

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        AppUser loggedUser = applicationUserService.findById(userPrincipal.getId()).getAppUser();
        
        String url = imageService.save(imageFile);
        Image saveImage = loggedUser.getImage();
        saveImage.setName(imageFile.getOriginalFilename());
        saveImage.setType(imageFile.getContentType());
        saveImage.setUrl(url);
        AppUser updateAppUser = appUserService.updateOne(loggedUser);
        
        Resource resource = imageService.load(updateAppUser.getImage());

        return ResponseEntity.ok().contentType(MediaType.valueOf(updateAppUser.getImage().getType())).body(resource.getContentAsByteArray());
    }

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Get Image by AppUser")
    @GetMapping("/appusers/{id}/images")
    public ResponseEntity<byte[]> getOneImageByAppUser(@PathVariable("id") Long id) throws IOException{
        AppUser appUser = appUserService.findById(id);
        Image image = appUser.getImage();
        Resource resource = imageService.load(image);

        return ResponseEntity.ok().contentType(MediaType.valueOf(image.getType())).body(resource.getContentAsByteArray());
    }

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Get Image by logged AppUser")
    @GetMapping("/appusers/images")
    public ResponseEntity<byte[]> getImageLoggedUser() throws IOException{
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        AppUser appUser = applicationUserService.findById(userPrincipal.getId()).getAppUser();
        Image image = appUser.getImage();
        if (image.getUrl() == null){
            throw new ImageNotFoundException("Image not found");
        }

        Resource resource = imageService.load(image);

        return ResponseEntity.ok().contentType(MediaType.valueOf(image.getType())).body(resource.getContentAsByteArray());
    }




//     @GetMapping("/AppUser/{id}/images")
//     public ResponseEntity<byte[]> getOneImageByAppUser(@PathVariable("id") Long id){
//         AppUser appUser = this.appUserService.findOneById(id);
//         Image image = appUser.getImage();

//         return ResponseEntity.ok().contentType(MediaType.valueOf(image.getType())).body(ImageUtility.decompressImage(image.getImage()));
        
//     }

//     @PatchMapping("/AppUsers/{id}")
//     public ResponseEntity<AppUserResponse> updateImageUrl(@PathVariable("id") Long id, @RequestBody UpdateImageUrlRequest request) {
//     AppUser appUser = appUserService.findOneById(id);

//     if (appUser != null) {
//         byte[] imageData = ImageUtility.compressImage(request.getImageUrl());
//         appUser = appUserService.updateImageUrl(appUser, imageData);
//         AppUserResponse appUserResponse = appUser.convertToResponse();
//         return ResponseEntity.ok(appUserResponse);
//     } else {
//         return ResponseEntity.notFound().build();
//     }
// }

}

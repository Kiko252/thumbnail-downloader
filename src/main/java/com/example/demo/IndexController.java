package com.example.demo;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@Controller
public class IndexController {

    private String currentUrl;
    private static final String IMAGE_NOT_FOUND_URL = "https://www.salonlfc.com/wp-content/uploads/2018/01/image-not-found-scaled-1150x647.png";

    @GetMapping("/home")
    public String index(Model model) {
        String imgUrl = "https://pickywallpapers.com/img/2018/10/youtube-red-desktop-wallpaper-526-534-hd-wallpapers.jpg";
        model.addAttribute("imgUrl", imgUrl);
        return "Thumbnail";
    }

    @PostMapping("/showImg")
    public String getUrl(@RequestParam String videoUrl, Model model) {
        String notValidated = "https://img.youtube.com/vi/" + getVideoId(videoUrl) + "/maxresdefault.jpg";
        String imgUrl = isValidImageURL(notValidated) ? notValidated : IMAGE_NOT_FOUND_URL;
        currentUrl = imgUrl;
        model.addAttribute("imgUrl", imgUrl);
        return "Thumbnail";
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadImage() {
        try {
            URL urlImage = new URL(currentUrl);
            InputStream inputStream = urlImage.openStream();

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=image" + currentUrl + ".jpg");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(new InputStreamResource(inputStream));
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }

    private String getVideoId(String videoUrl) {
        if (videoUrl.contains("v=")) {
            int index = videoUrl.indexOf("v=");
            return videoUrl.substring(index + 2, index + 13);
        } else if (videoUrl.contains(".be/")) {
            int index = videoUrl.indexOf(".be/");
            return videoUrl.substring(index + 4, index + 15);
        }
        return null;
    }

    private boolean isValidImageURL(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD");
            connection.connect();
            return connection.getResponseCode() == HttpURLConnection.HTTP_OK;
        } catch (IOException e) {
            return false;
        }
    }
}

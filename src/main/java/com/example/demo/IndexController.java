package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@Controller
public class IndexController {

    @Autowired
    private VideoLink oUrl;


    @GetMapping("/")
    public String index(Model model) {
        oUrl.setUrl("https://pickywallpapers.com/img/2018/10/youtube-red-desktop-wallpaper-526-534-hd-wallpapers.jpg");
        oUrl.setId(null);
        model.addAttribute("oUrl", oUrl);
        return "Thumbnail";
    }

    @PostMapping("showImg")
    public String getUrl(@ModelAttribute VideoLink url, Model model) {
        String videoUrl = url.getUrl();
        String videoID = null;

        String ytImages;
        if (videoUrl.contains("v=")) {
            int index = videoUrl.indexOf("v=");
            videoID = videoUrl.substring(index + 2, index + 13);
            ytImages = "https://img.youtube.com/vi/" + videoID + "/maxresdefault.jpg";

            if (!isValidImageURL(ytImages)) {
                ytImages = "https://i.redd.it/x1sr1lob3ai41.jpg";
                videoID=null;
            }

        } else {
            ytImages = "https://i.redd.it/x1sr1lob3ai41.jpg";
        }
        oUrl.setUrl(ytImages);
        oUrl.setId(videoID);
        model.addAttribute("oUrl", oUrl);

        return "Thumbnail";
    }

    @GetMapping("/show")
    public RedirectView show() {
        RedirectView redirectView = new RedirectView(oUrl.getUrl(), true);
        redirectView.setExposeModelAttributes(false);
        redirectView.setHttp10Compatible(false);
        return redirectView;
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadImage(Model model) throws IOException {
        // Get the image URL
        String imageUrl = oUrl.getUrl();

        // Create a URL object to read the image
        URL urlImage = new URL(imageUrl);
        InputStream inputStream = urlImage.openStream();

        // Set the headers for the response
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=image" + oUrl.getId() + ".jpg");

        model.addAttribute("oUrl", oUrl);
        // Return the ResponseEntity with the image file and headers
        return ResponseEntity.ok()
                .headers(headers)
                .body(new InputStreamResource(inputStream));
    }


    private boolean isValidImageURL(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD");
            connection.connect();

            int responseCode = connection.getResponseCode();
            return responseCode == HttpURLConnection.HTTP_OK;
        } catch (IOException e) {
            return false;
        }
    }
}

package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;

@Controller
public class IndexController {

    @GetMapping("/")
    public String index(Model model) {

        return "Thumbnail";
    }

    String ytImages;
    String videoID;

    @PostMapping("/getUrl")
    public String getUrl(@ModelAttribute Url url) {

        String videoUrl = url.toString();
        if (videoUrl.contains("v=")) {
            int index = videoUrl.indexOf("v=");
            videoID = videoUrl.substring(index + 2, index + 13);

            ytImages = "https://img.youtube.com/vi/vdID/maxresdefault.jpg";
            ytImages = ytImages.replace("vdID", videoID);
        }

        return "Download";

    }


    @GetMapping("/show")
    public RedirectView show() {
        RedirectView redirectView = new RedirectView(ytImages, true);
        redirectView.setExposeModelAttributes(false);
        redirectView.setHttp10Compatible(false);
        return redirectView;
    }

//    @GetMapping("/show")
//    public String show(@ModelAttribute("url") Url ytImages) {
//
//        return "Download";
//    }
// test for GIT


    @GetMapping("/download")
    public String downloadImage() throws InterruptedException {
        if (ytImages != null) {
            String home = System.getProperty("user.home");
            String imagePath = home + "/Downloads/thumbnail_" + videoID + ".jpg";

            BufferedImage image;
            try {

                URL urlImg = new URL(ytImages);
                image = ImageIO.read(urlImg);

                ImageIO.write(image, "jpg", new File(imagePath));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Thread.sleep(1000);

        return "Download";
    }
}

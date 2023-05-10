package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

@Controller
public class IndexController {

    @Autowired
    private Url oUrl;
    private String ytImages;
    private String videoID;

    @GetMapping("/")
    public String index(Model model) {
        return "Thumbnail";
    }

    @PostMapping("/getUrl")
    public String getUrl(@ModelAttribute Url url, Model model) {
        String videoUrl = url.getUrl();

        if (videoUrl.contains("v=")) {
            int index = videoUrl.indexOf("v=");
            videoID = videoUrl.substring(index + 2, index + 13);
            ytImages = "https://img.youtube.com/vi/" + videoID + "/maxresdefault.jpg";
        }
        oUrl.setUrl(ytImages);
        model.addAttribute("oUrl", oUrl);

        return "Download";
    }

    @GetMapping("/show")
    public RedirectView show() {
        RedirectView redirectView = new RedirectView(oUrl.getUrl(), true);
        redirectView.setExposeModelAttributes(false);
        redirectView.setHttp10Compatible(false);
        return redirectView;
    }

    @GetMapping("/download")
    public String downloadImage(Model model) throws InterruptedException {
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

        model.addAttribute("oUrl", oUrl);

        return "Download";
    }
}

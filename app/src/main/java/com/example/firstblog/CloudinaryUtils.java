package com.example.firstblog;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import android.net.Uri;
import java.io.File;
import java.util.Map;

public class CloudinaryUtils {

    private static final Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", " ",
            "api_key", ",
            "api_secret", ""
    ));

    public static String uploadImage(Uri imageUri) {
        try {
            File file = new File(imageUri.getPath());
            Map uploadResult = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
            return (String) uploadResult.get("CLOUDINARY_URL=cloudinary://<your_api_key>:<your_api_secret>@ddaaj9mgz");  // URL of the uploaded image
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

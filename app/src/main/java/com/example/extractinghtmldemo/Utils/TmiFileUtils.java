package com.example.extractinghtmldemo.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class TmiFileUtils {

    public static boolean copyInputStreamToFile(InputStream inputStream, File file, boolean isDownload) {
        OutputStream out = null;

        try {
            if(file.getName().endsWith(".pdf") && inputStream.available() < 1150 && !isDownload) return false;
            out = new FileOutputStream(file);
            byte[] buf = new byte[1024 * 1024];
            int len;
            while ((len = inputStream.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            // Ensure that the InputStreams are closed even if there's an exception.
            try {
                if (out != null) {
                    out.close();
                }

                // If you want to close the "in" InputStream yourself then remove this
                // from here but ensure that you close it yourself eventually.
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    private static final String[] ReservedChars = {"|", "?", "\\*", "<", "\"", ":", ">", " ", "/"};

    public static String removeForbiddenCharacters(String string) {

        String retS = string;

        for(int i = 0; i < ReservedChars.length; i++){
            retS = retS.replace(ReservedChars[i], "_");
        }


        return retS;
    }


}

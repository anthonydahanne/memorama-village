package net.dahanne.memorama.client;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import net.dahanne.memorama.client.model.ImageElement;


public class HtmlParser {

    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd_HH-mm-ss";

    public static ImageElement parseImagePhp(String html) {

        String nextAsString = "0";
        try {
            String substring = html.substring(0, html.indexOf("class=\"prev modal\""));
            String substring2 = substring.substring(substring.lastIndexOf("=") + 1);
            nextAsString = substring2.substring(0, substring2.lastIndexOf("\""));
        } catch (StringIndexOutOfBoundsException e) {
            // no previous photo
        }


        String substring = html.substring(0, html.indexOf("class=\"next modal\""));
        String substring2 = substring.substring(substring.lastIndexOf("=") + 1);
        String previousAsString = substring2.substring(0, substring2.lastIndexOf("\""));

        substring = html.substring(html.indexOf("<input type=\"hidden\" name=\"id\" value=\"") + 38);
        String idAsString = substring.substring(0, substring.indexOf("\""));

        substring = html.substring(0, html.indexOf(".jpeg") + 5);
        String rawUrl = substring.substring(substring.lastIndexOf("\"") + 1);

        String cameraAsString = substring.substring(substring.indexOf("camera") + 7, substring.indexOf("camera") + 8);

        String timeStampAsString = substring.substring(substring.indexOf("_") + 1, substring.lastIndexOf("_"));
        timeStampAsString = timeStampAsString.substring(0, timeStampAsString.lastIndexOf("_"));

        DateFormat dateFormat = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
;
        // System.out.println(substring2);
        // String previousAsString = substring2.substring(0,
        // substring2.lastIndexOf("\""));

        ImageElement imageElement = new ImageElement();
        imageElement.setNext(Integer.parseInt(nextAsString));
        imageElement.setPrevious(Integer.parseInt(previousAsString));
        imageElement.setId(Integer.parseInt(idAsString));
        imageElement.setFullUrl("http://www.memorama.me/" + rawUrl);
        imageElement.setCamera(Integer.parseInt(cameraAsString));
        try {
            imageElement.setTimestamp(dateFormat.parse(timeStampAsString));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return imageElement;

    }

}

package net.dahanne.memorama.client;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;

import net.dahanne.memorama.client.HtmlParser;
import net.dahanne.memorama.client.model.ImageElement;

import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

public class HtmlParserTest {

    @Test
    public void test() throws IOException {

        URL resource = Resources.getResource("image.html");
        String string = Resources.toString(resource, Charsets.UTF_8);

        ImageElement parseImagePhp = HtmlParser.parseImagePhp(string);
        assertEquals(15503, parseImagePhp.getNext());
        assertEquals(15500, parseImagePhp.getPrevious());
        assertEquals("http://www.memorama.me/images/2012/06/15/camera-2_2012-06-15_19-51-13_644.0_large.jpeg", parseImagePhp.getFullUrl());

        GregorianCalendar dateAsCalendar = new GregorianCalendar();
        dateAsCalendar.set(2012, 5, 15, 19, 51, 13);
        dateAsCalendar.clear(Calendar.MILLISECOND);

        assertEquals(dateAsCalendar.getTime().getTime(), parseImagePhp.getTimestamp().getTime());
        // assertEquals("date not the same as expected", 0,
        // dateAsCalendar.getTime().compareTo(parseImagePhp.getTimestamp()));
        assertEquals(15501, parseImagePhp.getId());
        assertEquals(2, parseImagePhp.getCamera());

    }

}

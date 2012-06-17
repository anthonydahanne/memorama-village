package net.dahanne.memorama.client;

import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import net.dahanne.memorama.client.model.PhotoElement;

import org.junit.Test;

public class AjaxInterfaceClientTest {

	@Test
    public void getLatestPhotoTest() throws RestClientException {
		Date now = new Date();
		GregorianCalendar nowAsCalendar = new GregorianCalendar();
		nowAsCalendar.setTime(now);
        String dayOfMonth = nowAsCalendar.get(Calendar.DAY_OF_MONTH) < 10 ? "0" + nowAsCalendar.get(Calendar.DAY_OF_MONTH) : ""
                + nowAsCalendar.get(Calendar.DAY_OF_MONTH);
        int currentMonth = nowAsCalendar.get(Calendar.MONTH) + 1;
        String month = currentMonth < 10 ? "0" + currentMonth : "" + currentMonth;
		int year = nowAsCalendar.get(Calendar.YEAR);
		
		
		AjaxInterfaceClient client = new AjaxInterfaceClient();
        PhotoElement latestPhoto = client.getLatestPhoto(1);
        System.out.println(latestPhoto);
        assertTrue(latestPhoto.getFullUrl().contains("camera-1"));
		//provided camera 1 gets at least a photo every day
        String currentDay = year + "-" + month + "-" + dayOfMonth;
        assertTrue(latestPhoto.getFullUrl().contains(currentDay));
		
	}

    @Test
    public void getPhotoTest() throws RestClientException {
        Date now = new Date();
        GregorianCalendar nowAsCalendar = new GregorianCalendar();
        nowAsCalendar.setTime(now);
        String dayOfMonth = nowAsCalendar.get(Calendar.DAY_OF_MONTH) < 10 ? "0" + nowAsCalendar.get(Calendar.DAY_OF_MONTH) : ""
                + nowAsCalendar.get(Calendar.DAY_OF_MONTH);
        int currentMonth = nowAsCalendar.get(Calendar.MONTH) + 1;
        String month = currentMonth < 10 ? "0" + currentMonth : "" + currentMonth;
        int year = nowAsCalendar.get(Calendar.YEAR);

        AjaxInterfaceClient client = new AjaxInterfaceClient();
        PhotoElement photo = client.getPhoto(3, 1);
        System.out.println(photo);
        assertTrue(photo.getFullUrl().contains("camera-3"));
        // provided camera 1 gets at least a photo every day
        String currentDay = year + "-" + month + "-" + dayOfMonth;
        assertTrue(photo.getFullUrl().contains(currentDay));

    }

    @Test
    public void findPhotoByDateTest() throws RestClientException {
        // Date now = new Date();
        GregorianCalendar nowAsCalendar = new GregorianCalendar();
        nowAsCalendar.set(2012, 5, 11, 22, 52, 03);

        AjaxInterfaceClient client = new AjaxInterfaceClient();
        PhotoElement latestPhoto = client.findPhotoByDate(3, nowAsCalendar.getTime());
        System.out.println(latestPhoto);
        System.out.println(latestPhoto.getFullUrl());
        assertTrue(latestPhoto.getFullUrl().contains("camera-3"));
        // provided camera 1 gets at least a photo every day
        String currentDay = "2012-06-11";
        assertTrue(latestPhoto.getFullUrl().contains(currentDay));

    }

}

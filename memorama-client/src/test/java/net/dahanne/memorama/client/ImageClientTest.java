package net.dahanne.memorama.client;

import static org.junit.Assert.assertEquals;

import net.dahanne.memorama.client.ImageClient;
import net.dahanne.memorama.client.RestClientException;
import net.dahanne.memorama.client.model.ImageElement;

import org.junit.Test;

public class ImageClientTest {

    @Test
    public void getPhotoByIdTest() throws RestClientException {
        ImageClient imageClient =  new ImageClient();
        ImageElement imageById = imageClient.getImageById(15988);
        assertEquals("http://www.memorama.me/images/2012/06/16/camera-1_2012-06-16_01-44-44_691.0_large.jpeg", imageById.getFullUrl());
    }

}

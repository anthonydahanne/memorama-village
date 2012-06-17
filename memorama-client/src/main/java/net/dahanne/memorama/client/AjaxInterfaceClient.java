package net.dahanne.memorama.client;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import net.dahanne.memorama.client.model.PhotoElement;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

public class AjaxInterfaceClient {

    private static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    private static final String MM_DD_YYYY_HH_MM_A = "MM/dd/yyyy hh:mm a";
    private static final String MEMORAME_AJAX_INTERFACE_URL = PhotoElement.MEMORAMA_BASE_URL + "inc/ajax.php?";
    private JsonFactory factory;
    private ObjectMapper mapper;

    public AjaxInterfaceClient() {

        factory = new JsonFactory();
        mapper = new ObjectMapper();

        DateFormat dateFormat = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
        mapper.setDateFormat(dateFormat);
    }

    public PhotoElement getLatestPhoto(int cameraId) throws RestClientException {
        return getPhoto(cameraId, 1);


    }

    private InputStream requestToResponseAsInputStream(String parameters) throws IOException {
        URL url = new URL(MEMORAME_AJAX_INTERFACE_URL + parameters);
        InputStream openStream = url.openStream();
        return openStream;
        // BufferedReader rd = new BufferedReader(new
        // InputStreamReader(openStream), 4096);
        // String line;
        // StringBuilder sb = new StringBuilder();
        // while ((line = rd.readLine()) != null) {
        // sb.append(line);
        // }
        // rd.close();
        // return sb.toString();
    }

    // private HttpEntity requestToResponseEntity(String parameters) throws
    // UnsupportedEncodingException, IOException, ClientProtocolException,
    // RestClientException {
    // HttpClient defaultHttpClient = new DefaultHttpClient();
    // HttpRequestBase httpMethod;
    // httpMethod = new HttpGet(MEMORAME_AJAX_INTERFACE_URL + parameters);
    // // adding the userAgent to the request
    // httpMethod.setHeader("User-Agent", "android");
    // HttpResponse response = null;
    //
    // response = defaultHttpClient.execute(httpMethod);
    //
    // int responseStatusCode = response.getStatusLine().getStatusCode();
    // HttpEntity responseEntity = null;
    // if (response.getEntity() != null) {
    // responseEntity = response.getEntity();
    // }
    //
    // switch (responseStatusCode) {
    // case HttpURLConnection.HTTP_OK:
    // break;
    // default:
    // throw new RestClientException("HTTP code " + responseStatusCode);
    // }
    //
    // return responseEntity;
    // }

    public PhotoElement findPhotoByDate(int cameraId, Date date) throws RestClientException {

        PhotoElement lastestPhoto = null;

        try {
            DateFormat dateFormat = new SimpleDateFormat(MM_DD_YYYY_HH_MM_A);
            String dateFormatted = dateFormat.format(date);

            String parameters = "camera=" + cameraId + "&date=" + URLEncoder.encode(dateFormatted, "UTF-8");
            InputStream responseAsString = requestToResponseAsInputStream(parameters);
            // HttpEntity requestToResponseEntity =
            // requestToResponseEntity(parameters);
            JsonParser parser = factory.createJsonParser(responseAsString);
            List<PhotoElement> photosList = mapper.readValue(parser, new TypeReference<List<PhotoElement>>() {
            });
            lastestPhoto = photosList.get(0);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return lastestPhoto;
        // http://cartespostales.dailytlj.com/inc/ajax.php?camera=2&date=06%2F09%2F2012+07%3A59+AM&display=shot
    }

    public PhotoElement getPhoto(int cameraId, int id) throws RestClientException {
        PhotoElement lastestPhoto = null;

        try {
            String parameters = "camera=" + cameraId + "&shot=" + id;
            InputStream responseAsString = requestToResponseAsInputStream(parameters);
            // HttpEntity requestToResponseEntity =
            // requestToResponseEntity(parameters);
            JsonParser parser = factory.createJsonParser(responseAsString);
            List<PhotoElement> photosList = mapper.readValue(parser, new TypeReference<List<PhotoElement>>() {
            });
            lastestPhoto = photosList.get(0);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return lastestPhoto;
    }

}

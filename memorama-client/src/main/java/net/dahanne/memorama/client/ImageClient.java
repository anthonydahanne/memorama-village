package net.dahanne.memorama.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import net.dahanne.memorama.client.model.ImageElement;
import net.dahanne.memorama.client.model.PhotoElement;

public class ImageClient {

    private static final String MEMORAME_AJAX_INTERFACE_URL = PhotoElement.MEMORAMA_BASE_URL + "image.php?id=";

    public ImageClient() {
        super();
    }

    private String requestToResponseAsString(String parameters) throws IOException {
        URL url = new URL(MEMORAME_AJAX_INTERFACE_URL + parameters);
        InputStream openStream = url.openStream();
         BufferedReader rd = new BufferedReader(new
         InputStreamReader(openStream), 4096);
         String line;
         StringBuilder sb = new StringBuilder();
         while ((line = rd.readLine()) != null) {
         sb.append(line);
         }
         rd.close();
         return sb.toString();
    }

    public ImageElement getImageById(int id) throws RestClientException {

//        try {
//            HttpEntity responseEntity = requestToResponseEntity(id);
//            BufferedReader reader = new BufferedReader(new InputStreamReader(responseEntity.getContent()));
//            String line;
//            StringBuilder sb = new StringBuilder();
//            try {
//                while ((line = reader.readLine()) != null) {
//                    sb.append(line);
//                    sb.append("\n");
//                }
//            } finally {
//                reader.close();
//            }
//            String result = sb.toString();
//            return HtmlParser.parseImagePhp(result);
//        } catch (UnsupportedEncodingException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (ClientProtocolException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (RestClientException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//        return null;
        
        try {
            return HtmlParser.parseImagePhp(requestToResponseAsString("" + id));
        } catch (IOException e) {
            throw new RestClientException(e);
        }
    }

    // private HttpEntity requestToResponseEntity(int id) throws
    // UnsupportedEncodingException, IOException, ClientProtocolException,
    // RestClientException {
    // HttpClient defaultHttpClient = new DefaultHttpClient();
    // HttpRequestBase httpMethod;
    // httpMethod = new HttpGet(MEMORAME_AJAX_INTERFACE_URL + id);
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



}

package ru.cftfilestore;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;


public class App {
    public static void main(String[] args) throws IOException, URISyntaxException {

        if(args.length==1 && args[0].equals("get_files_list")){
            String url = "http://localhost:8080/getfilelist";
            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
                HttpGet request = new HttpGet(url);
                HttpResponse result = httpClient.execute(request);

                String json = EntityUtils.toString(result.getEntity(), "UTF-8");
                JSONArray jsonArray = new JSONArray(json);

                System.out.println("Список файлов в хранилише:");

                        for (Object object : jsonArray) {
                            String obj = (String) object;
                            System.out.println(obj);
                        }
        }

        if(args.length==2 && args[0].equals("put_file")){

            String url = "http://localhost:8080/addFile";
            String path = args[1].replace("\\" , "\\\\");

            File file = new File(path);

            CloseableHttpClient client = HttpClients.createDefault();
                HttpPost post = new HttpPost(url);
                HttpEntity entity = MultipartEntityBuilder.create().addPart("file", new FileBody(file)).build();
                post.setEntity(entity);

                client.execute(post);
        }

        if(args.length==3 && args[0].equals("get_file")){

            HttpClient httpClient = HttpClients.createDefault();

            HttpGet httpget = new HttpGet("http://localhost:8080/download");
            URI uri = new URIBuilder(httpget.getURI())
                    .addParameter("filename", args[1])
                    .build();
            httpget.setURI(uri);
            HttpResponse response = httpClient.execute(httpget);
            HttpEntity entity = response.getEntity();

            InputStream is = entity.getContent();
            String filePath = args[2];
            FileOutputStream fos = new FileOutputStream(new File(filePath));

            byte[] buffer = new byte[4096];
            int inByte;
            BufferedInputStream bis = new BufferedInputStream(is);
            while((inByte = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, inByte);
            }
            is.close();
            fos.close();
        }

        if(args.length==0){
            System.out.println("Слишком мало аргументов!");
        }
    }
}

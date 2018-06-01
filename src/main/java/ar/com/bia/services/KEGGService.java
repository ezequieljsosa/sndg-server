package ar.com.bia.services;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import org.apache.http.HttpHost;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

@Service
public class KEGGService {

    @Cacheable(cacheNames = "queries")
    public Map<String, String> keggPathwayData(String pathway, Collection<String> kos) {

        Map<String, String> res = new HashMap<>();
        StringJoiner sj = new StringJoiner("/");
        kos.stream().forEach(x -> sj.add(x));
        //if (proxyEnabled) {
        Unirest.setProxy(new HttpHost("proxy.fcen.uba.ar", 8080));
        //}

        HttpResponse<String> response;
        try {
            String keggUrl = "http://www.kegg.jp/kegg-bin/show_pathway?@" + pathway +
                    "/reference%3dwhite/default%3d%23bfffbf/" + sj.toString();
            response = Unirest.get(keggUrl) .asString();
            if (response.getStatus() == 200) {

                ByteArrayOutputStream result = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int length;
                while ((length = response.getRawBody().read(buffer)) != -1) {
                    result.write(buffer, 0, length);
                }
                // StandardCharsets.UTF_8.name() > JDK 7
                String string = result.toString("UTF-8");
                String str1 = string.split("\" name=\"pathwayimage\"")[0];
                String imageUrl = "http://www.kegg.jp" + str1.split("src=\"")[str1.split("src=\"").length - 1];

                res.put("imageUrl", imageUrl);
                String imageMap = string.split("<map name=\"mapdata\">")[1].split("</map>")[0];
                res.put("mapdata", "<map name=\"mapdata\">" + imageMap + "</map>");
                res.put("pageUrl", keggUrl);


            } else {
                System.out.println(response.getStatusText() + " -- " + response.getBody());
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("error al consultar los conjuntos de datos del centro " + pathway + "\n" + ex.toString());
        }
        return res;
    }

}

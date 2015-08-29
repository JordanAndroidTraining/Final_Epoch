package com.yahoo.shopping.spotplace.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yahoo.shopping.spotplace.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by jamesyan on 8/28/15.
 */
@Service
public class SpotPlaceService {
    private Logger logger = Logger.getLogger(SpotPlaceService.class.getName());

    @Autowired
    private SpotPlaceRepository repository;

    public Page<SpotPlace> getResourcesByType(SpotPlaceType type, int pageIndex) {
        return repository.findByType(type, new PageRequest(pageIndex, 20));
    }

    public SpotPlace getResourceById(long id) {
        return repository.findOne(id);
    }

    public Page<SpotPlace> searchResourcesByKeyword(String keyword, int pageIndex) {
        return repository.findByKeyword("%" + keyword + "%", new PageRequest(pageIndex, 20));
    }

    public Page<SpotPlace> searchResourcesByKeywordAndType(String keyword, SpotPlaceType type, int pageIndex) {
        return repository.findByKeywordAndType("%" + keyword + "%", type, new PageRequest(pageIndex, 20));
    }


    public void addCommentsById(long id, Comment comment) {
        SpotPlace place = repository.findOne(id);
        place.addComment(comment);

        repository.save(place);
    }

    public void processResources(SpotPlaceType type, SpotPlaceResource resource) {
        logger.info("Process: " + resource.getUrl());

        switch (resource.getType()) {
            case CSV:
                processResource_CSVType(type, resource);
                break;
            case JSON:
                processResource_JSONType(type, resource);
                break;
            case XML:
                break;
        }
    }

    private void waitForNextFetch(long second) {
        try {
            Thread.sleep(second * 1000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
    }

    private String findImageByGoogleSearch(String keyword) {
        String url = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&rsz=8&q=" + URLEncoder.encode(keyword) + "&imgsz=xlarge";

        for (int retry = 0; retry < 5; retry++) {
            RestTemplate template = new RestTemplate();
            String result = template.getForObject(url, String.class);

            ObjectMapper objectMapper = new ObjectMapper();
            if (!result.isEmpty()) {
                JsonNode rootNode = null;

                try {
                    rootNode = objectMapper.readTree(result);
                } catch (IOException e) {
                    e.printStackTrace();
                    return "";
                }

                try {
                    return rootNode.get("responseData").get("results").get(0).get("url").asText();
                } catch (Exception e) {
                    logger.info("retry (" + retry + "): " + url);
                }
            }
            waitForNextFetch(60);
        }

        return "";
    }

    private void processResource_CSVType(SpotPlaceType type, SpotPlaceResource resource) {
        RestTemplate template = new RestTemplate();
        String result = template.getForObject(resource.getUrl(), String.class);

        String[] list = result.split("\n");
        for (int i = 1; i < list.length; i++) {
            String[] record = list[i].split(",");

            if (record.length >= 5) {
                String title = record[2];
                String address = record[4];
                String feature = record[5];
                String imageUrl = findImageByGoogleSearch(title);

                SpotPlace model = new SpotPlace(type, title, imageUrl, address, "", feature, "", "");
                repository.save(model);

                logger.info("Fetch: " + title);
            }
        }
    }

    @Transactional
    private void processResource_JSONType(SpotPlaceType type, SpotPlaceResource resource) {
        RestTemplate template = new RestTemplate();
        String result = template.getForObject(resource.getUrl(), String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        if (!result.isEmpty()) {
            JsonNode rootNode = null;

            try {
                rootNode = objectMapper.readTree(result);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            if (rootNode.isArray()) {
                for (JsonNode element : rootNode) {
                    Map<SpotPlace.Fields, String> fiedlsMapping = resource.getFiedlsMapping();

                    String title = "";
                    if (fiedlsMapping.containsKey(SpotPlace.Fields.Title)) {
                        title = element.get(fiedlsMapping.get(SpotPlace.Fields.Title)).asText();
                    }

                    String address = "";
                    if (fiedlsMapping.containsKey(SpotPlace.Fields.Address)) {
                        address = element.get(fiedlsMapping.get(SpotPlace.Fields.Address)).asText();
                    }

                    String phoneNumber = "";
                    if (fiedlsMapping.containsKey(SpotPlace.Fields.PhoneNumber)) {
                        phoneNumber = element.get(fiedlsMapping.get(SpotPlace.Fields.PhoneNumber)).asText();
                    }

                    String feature = "";
                    if (fiedlsMapping.containsKey(SpotPlace.Fields.Feature)) {
                        feature = element.get(fiedlsMapping.get(SpotPlace.Fields.Feature)).asText();
                    }

                    String reminder = "";
                    if (fiedlsMapping.containsKey(SpotPlace.Fields.Reminder)) {
                        reminder = element.get(fiedlsMapping.get(SpotPlace.Fields.Reminder)).asText();
                    }

                    String trafficInfo = "";
                    if (fiedlsMapping.containsKey(SpotPlace.Fields.TrafficInfo)) {
                        trafficInfo = element.get(fiedlsMapping.get(SpotPlace.Fields.TrafficInfo)).asText();
                    }

                    String imageUrl = findImageByGoogleSearch(title);

                    SpotPlace model = new SpotPlace(type, title, address, imageUrl, phoneNumber, feature, trafficInfo, reminder);
                    repository.save(model);

                    logger.info("Fetch: " + title);
                }
            }
        }
    }
}

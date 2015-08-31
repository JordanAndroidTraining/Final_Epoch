package com.yahoo.shopping.spotplace.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yahoo.shopping.spotplace.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
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

    public void updateImageUrlById(long id, String imageUrl) {
        SpotPlace place = repository.findOne(id);

        if (place != null) {
            place.setImageUrl(imageUrl);
            repository.save(place);
        }
    }

    public void addCommentsById(long id, Comment comment) {
        SpotPlace place = repository.findOne(id);

        int total = 0;
        int totalRecords = 0;
        for (Comment commentObj: place.getComments()) {
            if (commentObj.getRating() > 0) {
                total += commentObj.getRating();
                totalRecords ++;
            }
        }

        if (totalRecords != 0) {
            place.setAverageRating(total / totalRecords);
        } else {
            place.setAverageRating(comment.getRating());
        }
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
        String url = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&rsz=8&q=" + URLEncoder.encode(keyword);

        for (int retry = 0; retry < 1; retry++) {
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
            waitForNextFetch(15);
        }

        return "";
    }

    private boolean checkRecordExisted(String title, SpotPlaceType type) {
        List<SpotPlace> places = repository.findByTitleAndType(title, type);
        if (places == null || (places.size() == 0)) {
            return false;
        }

        boolean emptyImage = false;
        for (SpotPlace place : places) {
            if (place.getImageUrl().isEmpty()) {
                emptyImage = true;
            }
        }

        if (emptyImage) {
            for (SpotPlace place : places) {
                repository.delete(place.getId());
            }
            return false;
        }

        logger.info(title + " existed");

        return true;
    }

    private void processResource_CSVType(SpotPlaceType type, SpotPlaceResource resource) {
        RestTemplate template = new RestTemplate();
        template.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName(resource.getEncoding())));
        String result = template.getForObject(resource.getUrl(), String.class);

        String[] list = result.split("\n");
        for (int i = 1; i < list.length; i++) {
            String[] record = list[i].split(",");

            Map<SpotPlace.Fields, String> fiedlsMapping = resource.getFiedlsMapping();
            try {
                String title = "";
                if (fiedlsMapping.containsKey(SpotPlace.Fields.Title)) {
                    title = record[Integer.parseInt(fiedlsMapping.get(SpotPlace.Fields.Title))];
                }

                if (title.isEmpty()) {
                    throw new RuntimeException("Resource title cannot empty");
                }

                if (!checkRecordExisted(title, type)) {
                    String address = "";
                    if (fiedlsMapping.containsKey(SpotPlace.Fields.Address)) {
                        address = record[Integer.parseInt(fiedlsMapping.get(SpotPlace.Fields.Address))];
                    }

                    String phoneNumber = "";
                    if (fiedlsMapping.containsKey(SpotPlace.Fields.PhoneNumber)) {
                        phoneNumber = record[Integer.parseInt(fiedlsMapping.get(SpotPlace.Fields.PhoneNumber))];
                    }

                    String feature = "";
                    if (fiedlsMapping.containsKey(SpotPlace.Fields.Feature)) {
                        feature = record[Integer.parseInt(fiedlsMapping.get(SpotPlace.Fields.Feature))];
                    }

                    String reminder = "";
                    if (fiedlsMapping.containsKey(SpotPlace.Fields.Reminder)) {
                        reminder = record[Integer.parseInt(fiedlsMapping.get(SpotPlace.Fields.Reminder))];
                    }

                    String trafficInfo = "";
                    if (fiedlsMapping.containsKey(SpotPlace.Fields.TrafficInfo)) {
                        trafficInfo = record[Integer.parseInt(fiedlsMapping.get(SpotPlace.Fields.TrafficInfo))];
                    }

                    String imageUrl = findImageByGoogleSearch(title);

                    SpotPlace model = new SpotPlace(type, title, address, imageUrl, phoneNumber, feature, trafficInfo, reminder);
                    repository.save(model);

                    logger.info("Fetch: " + title);
                }
            } catch (Exception e) {
                logger.info(e.toString());
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

                    if (title.isEmpty()) {
                        throw new RuntimeException("Resource title cannot empty");
                    }

                    if (!checkRecordExisted(title, type)) {
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
}

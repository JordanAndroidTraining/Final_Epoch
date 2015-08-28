package com.yahoo.shopping.spotplace.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yahoo.shopping.spotplace.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.io.IOException;
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

    public List<SpotPlace> getResourcesByType(SpotPlaceType type) {
        return repository.findByType(type);
    }

    public SpotPlace getResourceById(long id) {
        return repository.findOne(id);
    }

    public void addCommentsById(long id, Comment comment) {
        SpotPlace place = repository.findOne(id);
        place.addComment(comment);

        repository.save(place);
    }

    public void processResources(SpotPlaceType type, SpotPlaceResource resource) {
        switch (resource.getType()) {
            case CSV:
                break;
            case JSON:
                processResource_JSONType(type, resource);
                break;
            case XML:
                break;
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

                    SpotPlace model = new SpotPlace(type, title, address, phoneNumber, feature, trafficInfo, reminder);
                    repository.save(model);
                }
            }
        }
    }
}

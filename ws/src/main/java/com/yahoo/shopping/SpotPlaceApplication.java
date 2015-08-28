package com.yahoo.shopping;

import com.yahoo.shopping.spotplace.model.SpotPlace;
import com.yahoo.shopping.spotplace.model.SpotPlaceResource;
import com.yahoo.shopping.spotplace.model.SpotPlaceType;
import com.yahoo.shopping.spotplace.services.SpotPlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.jpa.vendor.HibernateJpaSessionFactoryBean;

import javax.persistence.EntityManagerFactory;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class SpotPlaceApplication implements CommandLineRunner {
    private Map<SpotPlaceType, List<String>> urls;

    @Autowired
    private SpotPlaceService service;

    public static void main(String[] args) {
        SpringApplication.run(SpotPlaceApplication.class, args);
    }

    @Bean
    public HibernateJpaSessionFactoryBean sessionFactory(EntityManagerFactory emf) {
        HibernateJpaSessionFactoryBean factory = new HibernateJpaSessionFactoryBean();
        factory.setEntityManagerFactory(emf);
        return factory;
    }

    private Map<SpotPlaceType, List<SpotPlaceResource>> getColdStartResourcesInfo() {
        Map<SpotPlaceType, List<SpotPlaceResource>> urls = new HashMap<>();

//        urls.put(SpotPlaceType.CountrySide, Arrays.asList(
//                "http://data.coa.gov.tw/Service/OpenData/EzgoSuggestTravel.aspx",
//                "http://data.coa.gov.tw/Service/OpenData/EzgoLocalFeature.aspx",
//                "http://data.coa.gov.tw/Service/OpenData/EzgoTravelHotelStay.aspx",
//                "http://data.coa.gov.tw/Service/OpenData/EzgoAttractions.aspx",
//                "http://data.coa.gov.tw/Service/OpenData/EzgoTravelFoodStay.aspx"));
//
//        urls.put(SpotPlaceType.FarmVisit, Arrays.asList(
//                "http://data.coa.gov.tw/Service/OpenData/EzgoQualityFarm.aspx"));
//
//        urls.put(SpotPlaceType.Spot, Arrays.asList(
//                "http://data.gov.tw/iisi/logaccess/2182?dataUrl=http://www.hakka.gov.tw/public/opendata/Hakka-Att.csv&ndctype=CSV&ndcnid=7746"));
//
//        urls.put(SpotPlaceType.Aborigines, Arrays.asList(
//                "http://data.gov.tw/iisi/logaccess/1449?dataUrl=http://lod2.apc.gov.tw/APCDataApi.ashx?apiId=102013&format=json&ndctype=JSON&ndcnid=7100",
//                "http://data.gov.tw/iisi/logaccess/1452?dataUrl=http://lod2.apc.gov.tw/APCDataApi.ashx?apiId=102014&format=json&ndctype=JSON&ndcnid=7101",
//                "http://data.gov.tw/iisi/logaccess/1533?dataUrl=http://lod2.apc.gov.tw/APCDataApi.ashx?apiId=102010&format=json&ndctype=JSON&ndcnid=7128"));

        urls.put(SpotPlaceType.CountrySide,
                Arrays.asList(
                        new SpotPlaceResource("http://data.coa.gov.tw/Service/OpenData/EzgoMovingRoad.aspx",
                                SpotPlaceResource.DataType.JSON,
                                new HashMap<SpotPlace.Fields, String>() {
                                    {
                                        put(SpotPlace.Fields.Title, "Name");
                                        put(SpotPlace.Fields.Address, "AreaLocation");
                                        put(SpotPlace.Fields.PhoneNumber, "Tel");
                                        put(SpotPlace.Fields.Feature, "Feature");
                                        put(SpotPlace.Fields.TrafficInfo, "TrafficGuidelines");
                                        put(SpotPlace.Fields.Reminder, "Reminder");
                                    }
                                })
                ));

        urls.put(SpotPlaceType.FarmVisit,
                Arrays.asList(
                        new SpotPlaceResource("http://data.coa.gov.tw/Service/OpenData/EzgoQualityFarm.aspx",
                                SpotPlaceResource.DataType.JSON,
                                new HashMap<SpotPlace.Fields, String>() {
                                    {
                                        put(SpotPlace.Fields.Title, "FarmNm_CH");
                                        put(SpotPlace.Fields.Address, "Address_CH");
                                        put(SpotPlace.Fields.PhoneNumber, "TEL");
                                        put(SpotPlace.Fields.Feature, "Feature_CH");
                                        put(SpotPlace.Fields.TrafficInfo, "Address_CH");
                                    }
                                })
                ));

        urls.put(SpotPlaceType.Aborigines,
                Arrays.asList(
                        new SpotPlaceResource("http://data.gov.tw/iisi/logaccess/1449?dataUrl=http://lod2.apc.gov.tw/APCDataApi.ashx?apiId=102013&format=json&ndctype=JSON&ndcnid=7100",
                                SpotPlaceResource.DataType.JSON,
                                new HashMap<SpotPlace.Fields, String>() {
                                    {
                                        put(SpotPlace.Fields.Title, "Name");
                                        put(SpotPlace.Fields.Address, "Address");
                                        put(SpotPlace.Fields.PhoneNumber, "Telephone");
                                    }
                                })
                ));

        return urls;
    }

    @Override
    public void run(String... args) throws Exception {
        for (Map.Entry<SpotPlaceType, List<SpotPlaceResource>> entry : getColdStartResourcesInfo().entrySet()) {
            SpotPlaceType type = entry.getKey();
            List<SpotPlaceResource> resources = entry.getValue();

            for (SpotPlaceResource resource : resources) {
                service.processResources(type, resource);
            }
        }
    }
}
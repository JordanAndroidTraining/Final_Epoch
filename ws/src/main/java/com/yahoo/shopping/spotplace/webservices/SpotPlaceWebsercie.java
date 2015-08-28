package com.yahoo.shopping.spotplace.webservices;

import com.yahoo.shopping.spotplace.model.Comment;
import com.yahoo.shopping.spotplace.model.SpotPlace;
import com.yahoo.shopping.spotplace.model.SpotPlaceType;
import com.yahoo.shopping.spotplace.services.SpotPlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by jamesyan on 8/27/15.
 */
@RestController
@RequestMapping("/")
public class SpotPlaceWebsercie {
    @Autowired
    private SpotPlaceService service;

    @RequestMapping("hello")
    public String hello() {
        return "hello";
    }

    @RequestMapping(value = "resources", method = RequestMethod.GET)
    public List<SpotPlace> getResourcesByType(@RequestParam("type")SpotPlaceType type) {
        return service.getResourcesByType(type);
    }

    @RequestMapping(value = "resources/{id}", method = RequestMethod.GET)
    public SpotPlace getResourceById(@PathVariable long id) {
        return service.getResourceById(id);
    }

    @RequestMapping(value = "resources/{id}/comments", method = RequestMethod.POST)
    public void createCommentById(@PathVariable long id,
                                  @RequestParam("subject") String subject,
                                  @RequestParam("comment") String comment,
                                  @RequestParam("imageUrl") String imageUrl) {
        service.addCommentsById(id, new Comment(subject, comment, imageUrl));
    }
}

package com.yahoo.shopping.spotplace.webservices;

import com.yahoo.shopping.spotplace.model.Comment;
import com.yahoo.shopping.spotplace.model.SpotPlace;
import com.yahoo.shopping.spotplace.model.SpotPlaceType;
import com.yahoo.shopping.spotplace.services.SpotPlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

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
    public Page<SpotPlace> getResourcesByType(@RequestParam("type") SpotPlaceType type,
                                              @RequestParam(value = "page", defaultValue = "0") int pageIndex) {
        return service.getResourcesByType(type, pageIndex);
    }

    @RequestMapping(value = "resources/{id}", method = RequestMethod.GET)
    public SpotPlace getResourceById(@PathVariable long id) {
        return service.getResourceById(id);
    }

    @RequestMapping(value = "resources/{id}/comments", method = RequestMethod.POST)
    public void createCommentById(@PathVariable long id,
                                  @RequestParam("subject") String subject,
                                  @RequestParam("comment") String comment,
                                  @RequestParam("imageUrl") String imageUrl,
                                  @RequestParam(value = "rating", defaultValue = "0") int rating) {
        service.addCommentsById(id, new Comment(subject, comment, imageUrl, rating));
    }

    @RequestMapping(value = "resources/search", method = RequestMethod.GET)
    public Page<SpotPlace> searchByKeyword(@RequestParam("keyword") String keyword,
                                           @RequestParam(value = "type", required = false) SpotPlaceType type,
                                           @RequestParam(value = "page", defaultValue = "0") int pageIndex) {
        if (type != null) {
            return service.searchResourcesByKeywordAndType(keyword, type, pageIndex);
        }
        return service.searchResourcesByKeyword(keyword, pageIndex);
    }

    @RequestMapping(value = "resources/{id}/imageUrl", method = RequestMethod.POST)
    public void updateImageUrlById(@PathVariable("id") long id, @RequestParam("imageUrl") String imageUrl) {
        service.updateImageUrlById(id, imageUrl);
    }
}

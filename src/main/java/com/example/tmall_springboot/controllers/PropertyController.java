package com.example.tmall_springboot.controllers;

import com.example.tmall_springboot.domains.Property;
import com.example.tmall_springboot.services.PropertyService;
import com.example.tmall_springboot.utils.Page4Navigator;
import org.springframework.web.bind.annotation.*;

@RestController
public class PropertyController {

    private final PropertyService propertyService;

    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @GetMapping("/categories/{cid}/properties")
    public Page4Navigator<Property> list(@PathVariable Long cid, @RequestParam(value = "start", defaultValue = "0") int start,
                                         @RequestParam(value = "size", defaultValue = "5") int size) throws Exception {
        start = start < 0 ? 0 : start;
        return propertyService.pageFromJpa(cid, start, size);
    }

    @GetMapping("/properties/{id}")
    public Object get(@PathVariable Long id) throws Exception {
        return propertyService.get(id);
    }

    @PostMapping("/properties")
    public Property add(@RequestBody Property property) throws Exception {
        return propertyService.add(property);
    }

    @DeleteMapping("/properties/{id}")
    public void delete(@PathVariable Long id)  throws Exception {
        propertyService.delete(id);
    }

    @PutMapping("/properties")
    public Property update(@RequestBody Property property) throws Exception {
        return propertyService.update(property);
    }

}

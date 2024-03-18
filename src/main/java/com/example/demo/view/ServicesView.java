package com.example.demo.view;


import com.example.demo.service.ServicesDto;
import com.example.demo.service.ServicesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;


@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class ServicesView {
    private final ServicesService servicesService;

    @GetMapping("/services")
    public List<ServicesDto> allBooks() {
        return servicesService.findAll();
    }

    @GetMapping("/services/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ServicesDto> getBook(@PathVariable Long id) {
        return ResponseEntity.ok().body(servicesService.findById(id));
    }

    @PostMapping("/services")
    public ResponseEntity<ServicesDto> createBook( @RequestBody ServicesDto service) throws URISyntaxException {
        ServicesDto result = servicesService.save(service);
        return ResponseEntity.created(new URI("/api/v1/services/" + result.getId()))
                .body(result);
    }

    @PutMapping("/services/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ServicesDto> updateBook( @PathVariable Long id, @RequestBody ServicesDto service) {
        return ResponseEntity.ok().body(servicesService.save(service));
    }

    @DeleteMapping("/services/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {
        servicesService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}

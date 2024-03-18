package com.example.demo.service;

import java.util.List;

public interface ServicesService {
    List<ServicesDto> findAll ();
    ServicesDto findById( Long id);
    ServicesDto save (ServicesDto services);
    void deleteById (Long id);
}

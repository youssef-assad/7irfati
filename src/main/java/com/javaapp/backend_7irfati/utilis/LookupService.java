package com.javaapp.backend_7irfati.utilis;

import com.javaapp.backend_7irfati.Dtos.select.CategoryDTO;
import com.javaapp.backend_7irfati.Dtos.select.CityDTO;
import com.javaapp.backend_7irfati.repository.optional.CategoryRepository;
import com.javaapp.backend_7irfati.repository.optional.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LookupService {

    private final CityRepository cityRepository;
    private final CategoryRepository categoryRepository;

    public Map<String, Object> getRegisterData() {

        List<CityDTO> cities = cityRepository.findAll()
                .stream()
                .map(c -> new CityDTO(
                        c.getId(),
                        c.getNameFr(),
                        c.getNameAr()
                ))
                .toList();

        List<CategoryDTO> categories = categoryRepository.findAll()
                .stream()
                .map(cat -> new CategoryDTO(
                        cat.getId(),
                        cat.getNameFr(),
                        cat.getNameAr()
                ))
                .toList();

        return Map.of(
                "cities", cities,
                "categories", categories
        );
    }
}

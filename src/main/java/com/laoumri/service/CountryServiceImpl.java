package com.laoumri.service;

import com.laoumri.entity.Country;
import com.laoumri.exception.CountryNotFoundException;
import com.laoumri.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {
    private final CountryRepository countryRepository;

    @Override
    public Country getCountryById(Long id) {
        return countryRepository.findById(id).orElseThrow(CountryNotFoundException::new);
    }

    @Override
    public Country getCountryByName(String name) {
        return countryRepository.findByName(name).orElseThrow(CountryNotFoundException::new);
    }

    @Override
    public List<Country> getCountryList() {
        return countryRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }
}

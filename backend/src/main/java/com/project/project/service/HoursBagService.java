package com.project.project.service;

import com.project.project.dto.HoursBagDTO;
import com.project.project.entity.HoursBag;
import com.project.project.respository.HoursBagRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HoursBagService {

    private final HoursBagRepository hoursBagRepository;

    HoursBagService(HoursBagRepository hoursBagRepository) {
        this.hoursBagRepository = hoursBagRepository;
    }

    public List<HoursBagDTO> getAllHourBags() {
        return hoursBagRepository.findAll().stream().map(HoursBagMapper::toDto)
                .collect(Collectors.toList());
    }

    public HoursBagDTO getHourBagById(Long idHoursBag) {

        Optional<HoursBag> hourBag = hoursBagRepository.findById(idHoursBag);

        if(hourBag.isPresent()){
            return HoursBagMapper.toDto(hourBag.get());
        }

        return new HoursBagDTO();
    }

     public HoursBagDTO create(HoursBagDTO hoursBagDTO) {
        hoursBagDTO.setCreatedAt(LocalDate.now());

        return HoursBagMapper.toDto(hoursBagRepository.save(HoursBagMapper.toEntity(hoursBagDTO)));
    }

    public HoursBagDTO update(HoursBagDTO hoursBagDTO) {
        return HoursBagMapper.toDto(hoursBagRepository.save(HoursBagMapper.toEntity(hoursBagDTO)));
    }

    public void deleHoursBagById(Long idHourBag) {

        hoursBagRepository.deleteById(idHourBag);
    }

    public void deleteHoursBags() {

        hoursBagRepository.deleteAll();
    }
}

package com.project.project.service;

import com.project.project.dto.HoursBagDTO;
import com.project.project.entity.HoursBag;
import com.project.project.respository.HoursBagRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HoursBagService {

    private final HoursBagRepository hoursBagRepository;
    //private final ContractService contractService;

    HoursBagService(HoursBagRepository hoursBagRepository) {
        this.hoursBagRepository = hoursBagRepository;
    }

    public List<HoursBagDTO> getAllHourBags() {
        return hoursBagRepository.findAll().stream().map(HoursBagMapper::toDto)
                .collect(Collectors.toList());
    }

    public HoursBagDTO getHourBagById(Long idHoursBag) {
        return HoursBagMapper.toDto(hoursBagRepository.findById(idHoursBag).isPresent() ?
                hoursBagRepository.findById(idHoursBag).get() : new HoursBag());
    }

    /*public HoursBagDTO create(HoursBagDTO hoursBagDTO) {
        hoursBagDTO.setCreatedAt(LocalDate.now());

        String benef = contractService.getContractById(hoursBagDTO.getContractId()).getBeneficiary();
        String name = contractService.getContractById(hoursBagDTO.getContractId()).getName();

        hoursBagDTO.setContractBN("["+benef+"]"+name);

        return HoursBagMapper.toDto(hoursBagRepository.save(HoursBagMapper.toEntity(hoursBagDTO)));
    }*/

     public HoursBagDTO create(HoursBagDTO hoursBagDTO) {
        hoursBagDTO.setCreatedAt(LocalDate.now());

        return HoursBagMapper.toDto(hoursBagRepository.save(HoursBagMapper.toEntity(hoursBagDTO)));
    }

    public HoursBagDTO update(HoursBagDTO hoursBagDTO) {
        return HoursBagMapper.toDto(hoursBagRepository.save(HoursBagMapper.toEntity(hoursBagDTO)));
    }

    public void deleHoursBagById(Long idHourBag) {

        HoursBagDTO hourBag = getHourBagById(idHourBag);
        hoursBagRepository.delete(HoursBagMapper.toEntity(hourBag));
    }

    public void deleteHoursBags() {

        hoursBagRepository.deleteAll();
    }
}

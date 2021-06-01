package com.project.project.controller;

import com.project.project.dto.HoursBagDTO;
import com.project.project.service.HoursBagService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hoursBag")
@CrossOrigin
public class HoursBagController {

    private final HoursBagService hoursBagService;

    public HoursBagController(HoursBagService hoursBagService) {
        this.hoursBagService = hoursBagService;
    }

    @GetMapping
    public ResponseEntity<List<HoursBagDTO>> getAll() {
        return ResponseEntity.ok(hoursBagService.getAllHourBags());
    }

    @GetMapping("/{hourBagId}")
    public ResponseEntity<HoursBagDTO> getById(@PathVariable String hourBagId) {
        Long id = Long.parseLong(hourBagId);
        return ResponseEntity.ok(hoursBagService.getHourBagById(id));
    }

    @PostMapping
    public ResponseEntity<HoursBagDTO> create(@RequestBody HoursBagDTO data) {
        return ResponseEntity.ok(hoursBagService.create(data));
    }

    @PutMapping
    public ResponseEntity<HoursBagDTO> update(@RequestBody HoursBagDTO data) {
        if (data.getId() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(hoursBagService.update(data));
    }

    @DeleteMapping("/{hourBagId}")
    public ResponseEntity<Void> deleteById(@PathVariable String hourBagId) {

        Long id = Long.parseLong(hourBagId);

        hoursBagService.deleHoursBagById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAll() {

        hoursBagService.deleteHoursBags();

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

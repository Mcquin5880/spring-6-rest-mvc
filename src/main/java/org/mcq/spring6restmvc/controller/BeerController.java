package org.mcq.spring6restmvc.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mcq.spring6restmvc.model.Beer;
import org.mcq.spring6restmvc.service.BeerService;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
@AllArgsConstructor
@Slf4j
public class BeerController {

    private final BeerService beerService;

    public Beer getBeerById(UUID id) {
        log.info("Get beer by id - in controller");
        return beerService.getBeerById(id);
    }

}

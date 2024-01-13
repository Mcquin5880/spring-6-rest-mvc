package org.mcq.spring6restmvc.service;

import org.mcq.spring6restmvc.model.Beer;

import java.util.UUID;

public interface BeerService {

    Beer getBeerById(UUID id);
}

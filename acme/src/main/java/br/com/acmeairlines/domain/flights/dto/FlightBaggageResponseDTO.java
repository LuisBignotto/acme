package br.com.acmeairlines.domain.flights.dto;

import br.com.acmeairlines.domain.baggages.model.BaggageModel;

import java.util.List;

public record FlightBaggageResponseDTO(FlightDataDTO data, List<BaggageModel> baggage){}

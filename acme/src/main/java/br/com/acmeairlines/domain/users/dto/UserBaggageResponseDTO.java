package br.com.acmeairlines.domain.users.dto;

import br.com.acmeairlines.domain.baggages.model.BaggageModel;

import java.util.List;

public record UserBaggageResponseDTO(UserDataDTO user, List<BaggageModel> baggage) {}

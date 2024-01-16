package br.com.acmeairlines.domain.users;

import br.com.acmeairlines.domain.baggages.BaggageModel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
public class UserAndBaggageResponse {
    private final UserDataRecord user;
    private final List<BaggageModel> baggage;

    public UserAndBaggageResponse(UserDataRecord user, List<BaggageModel> baggage) {
        this.user = user;
        this.baggage = baggage;
    }
}

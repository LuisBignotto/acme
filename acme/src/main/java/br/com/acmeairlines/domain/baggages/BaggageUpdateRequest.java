package br.com.acmeairlines.domain.baggages;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaggageUpdateRequest {
    private BaggageUpdateData data;
    private String tag;
}

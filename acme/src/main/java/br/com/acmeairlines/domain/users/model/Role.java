package br.com.acmeairlines.domain.users.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Role {
    @JsonProperty("ADMINISTRATOR")
    ADMINISTRATOR,

    @JsonProperty("BAGGAGE_MANAGER")
    BAGGAGE_MANAGER,

    @JsonProperty("SUPPORT")
    SUPPORT,

    @JsonProperty("REGULAR_USER")
    REGULAR_USER
}

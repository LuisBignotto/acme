package br.com.acmeairlines.domain.baggages.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Status {
    @JsonProperty("DESPACHADA")
    DESPACHADA,

    @JsonProperty("EM_ANALISE_DE_SEGURANCA")
    EM_ANALISE_DE_SEGURANCA,

    @JsonProperty("REPROVADA_PELA_ANALISE_DE_SEGURANCA")
    REPROVADA_PELA_ANALISE_DE_SEGURANCA,

    @JsonProperty("APROVADA_PELA_ANALISE_DE_SEGURANCA")
    APROVADA_PELA_ANALISE_DE_SEGURANCA,

    @JsonProperty("NA_AERONAVE")
    NA_AERONAVE,

    @JsonProperty("EM_VOO")
    EM_VOO,

    @JsonProperty("DESTINO_INCERTO")
    DESTINO_INCERTO,

    @JsonProperty("EXTRAVIADA")
    EXTRAVIADA,

    @JsonProperty("AGUARDANDO_RECOLETA")
    AGUARDANDO_RECOLETA,

    @JsonProperty("COLETADA")
    COLETADA
}

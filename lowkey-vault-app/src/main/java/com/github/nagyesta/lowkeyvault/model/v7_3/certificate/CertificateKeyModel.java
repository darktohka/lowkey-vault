package com.github.nagyesta.lowkeyvault.model.v7_3.certificate;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.nagyesta.lowkeyvault.model.v7_2.key.constants.KeyCurveName;
import com.github.nagyesta.lowkeyvault.model.v7_2.key.constants.KeyType;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class CertificateKeyModel {

    @JsonProperty("exportable")
    private boolean exportable;

    @JsonProperty("crv")
    private KeyCurveName keyCurveName;

    @NotNull
    @JsonProperty("kty")
    private KeyType keyType;

    @Min(1024)
    @Max(4096)
    @JsonProperty("key_size")
    private Integer keySize;

    @JsonProperty("reuse_key")
    private boolean reuseKey;

}

package com.example.cloud.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Demo implements Serializable {

    @JsonProperty("id")
    private String id;

    @JsonProperty("value")
    private String value;
}

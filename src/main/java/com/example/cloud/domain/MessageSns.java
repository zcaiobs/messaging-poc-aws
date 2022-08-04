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
public class MessageSns implements Serializable {

    @JsonProperty("Type")
    private String Type;

    @JsonProperty("MessageId")
    private String MessageId;

    @JsonProperty("TopicArn")
    private String TopicArn;

    @JsonProperty("Message")
    private String Message;

    @JsonProperty("Timestamp")
    private String Timestamp;

    @JsonProperty("SignatureVersion")
    private String SignatureVersion;

    @JsonProperty("Signature")
    private String Signature;

    @JsonProperty("SigningCertURL")
    private String SigningCertURL;

    @JsonProperty("UnsubscribeURL")
    private String UnsubscribeURL;
}

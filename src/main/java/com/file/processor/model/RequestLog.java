package com.file.processor.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Data
public class RequestLog {

    @Id
    private UUID requestId;

    @Column
    private String requestUri;

    @Column
    private Timestamp requestTimestamp;

    @Column
    private int httpResponseCode;

    @Column
    private String requestIpAddress;

    @Column
    private String requestCountryCode;

    @Column
    private String requestIpProvider;

    @Column
    private long timeLapsed;

    @Transient
    private long startTime;

}

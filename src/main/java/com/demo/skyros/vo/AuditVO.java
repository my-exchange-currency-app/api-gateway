package com.demo.skyros.vo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;


@Setter
@Getter
public class AuditVO implements Serializable {

    @DateTimeFormat(pattern = "dd-M-yyyy hh:mm:ss")
    private Date createdDate;
    private String createdBy;
    private Long createdById;
    @DateTimeFormat(pattern = "dd-M-yyyy hh:mm:ss")
    private Date lastModifiedDate;
    private String lastModifiedBy;
    private Long lastModifiedById;

}

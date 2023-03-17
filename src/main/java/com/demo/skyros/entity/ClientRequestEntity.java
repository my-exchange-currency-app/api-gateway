package com.demo.skyros.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "CLIENT_REQUEST")
public class ClientRequestEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CLIENT_REQUEST_SEQ")
    @SequenceGenerator(name = "CLIENT_REQUEST_SEQ", sequenceName = "CLIENT_REQUEST_SEQ", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @Column(name = "REQUEST_ID")
    private String requestId;

    @Column(name = "TAG")
    private String tag;

    @Column(name = "REQUEST_BODY")
    @Lob
    private String requestBody;

}

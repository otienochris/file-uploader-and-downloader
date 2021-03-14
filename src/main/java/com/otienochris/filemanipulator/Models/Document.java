package com.otienochris.filemanipulator.Models;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "documents")

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 512, nullable = false, unique = true)
    private String name;
    private long size;

    @Column(name = "upload_time")
    private Date uploadTime;

    @Column(columnDefinition = "MEDIUMBLOB")
    private byte[] content;

    public Document(Long id, String name, long size) {
        this.id = id;
        this.name = name;
        this.size = size;
    }
}

package com.alwertus.spassistent.parts.info.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "info_page")
public class Page {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name="space_id")
    private Space space;

    @Column(name = "title")
    private String title;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name="parent_id")
    private Page parent;

    @Column(name = "created")
    @CreationTimestamp
    private Date created;

    @Lob
    @Column(name = "html")
    private String html;
}
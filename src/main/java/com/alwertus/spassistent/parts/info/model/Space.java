package com.alwertus.spassistent.parts.info.model;

import com.alwertus.spassistent.user.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "info_space")
public class Space {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "descr")
    private String description;

    @Column(name = "created")
    private Date created;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @Override
    public String toString() {
        return "Space{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", created=" + created +
                '}';
    }
}
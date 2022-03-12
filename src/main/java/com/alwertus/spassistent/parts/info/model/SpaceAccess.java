package com.alwertus.spassistent.parts.info.model;

import com.alwertus.spassistent.user.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "info_space_access")
public class SpaceAccess {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "space")
    private Space space;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "user")
    private User user;

    @Column(name = "access")
    @Enumerated(EnumType.STRING)
    private SpaceAccessEnum access;
}

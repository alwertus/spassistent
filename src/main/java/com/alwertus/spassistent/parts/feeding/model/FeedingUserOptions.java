package com.alwertus.spassistent.parts.feeding.model;

import com.alwertus.spassistent.user.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "feeding_user_options")
public class FeedingUserOptions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @Column(name = "access_id")
    private String accessId;
}

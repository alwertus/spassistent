package com.alwertus.spassistent.parts.info.model;

import com.alwertus.spassistent.user.model.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

//TODO: Седлать, чтоб записи из этой таблицы удалялись при удалении пользователя

@Getter
@Setter
@Entity
@Table(name = "info_user_options")
public class InfoUserOptions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "user")
    private User user;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name="selected_space")
    private Space selectedSpace;
}

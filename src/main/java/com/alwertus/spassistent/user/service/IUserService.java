package com.alwertus.spassistent.user.service;

import com.alwertus.spassistent.user.model.User;

public interface IUserService {

    void saveUser(User user);

    User getUser(String login);

    User getCurrentUser();
}

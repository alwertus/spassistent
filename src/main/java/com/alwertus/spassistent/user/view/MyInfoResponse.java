package com.alwertus.spassistent.user.view;

import com.alwertus.spassistent.common.view.ResponseOk;
import com.alwertus.spassistent.user.model.User;

@SuppressWarnings("unused")
public class MyInfoResponse extends ResponseOk {
    private final User user;

    public MyInfoResponse(User user) {
        this.user = user;
    }

    public String getFirstName() {
        return user.getFirstName();
    }

    public String getLastName() {
        return user.getLastName();
    }

    public String getLogin() { return user.getLogin(); }
}

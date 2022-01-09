package com.alwertus.spassistent.userold.view;

import com.alwertus.spassistent.user.model.User;

@SuppressWarnings("unused")
public record AuthenticationResponse(/*JwtToken token, */String refreshString, User currentUser) {

//    public String getToken() {
//        return token.getTokenString();
//    }
    public String getRefreshString() { return refreshString; }
    public String getFirstName() { return currentUser.getFirstName(); }
    public String getLastName() { return currentUser.getLastName(); }
    public String getResult() { return "Ok"; }
}

package io.bvb.smarthealthcare.backend.util;

import io.bvb.smarthealthcare.backend.constant.LoginUserType;

public class LoginUserTypeUtil {
    private static final ThreadLocal<LoginUserType> LOGIN_USER_TYPE = new ThreadLocal<>();

    public static LoginUserType getLoginUserType() {
        return LOGIN_USER_TYPE.get();
    }

    public static void setLoginUserType(LoginUserType loginUserType) {
        LOGIN_USER_TYPE.set(loginUserType);
    }

    public static void unsetLoginUserType() {
        LOGIN_USER_TYPE.remove();
    }
}


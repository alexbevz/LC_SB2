package ru.bevz.freeter.domain.util;

import ru.bevz.freeter.domain.User;

public abstract class MessageHelper {
    public static String getAuthorName(User user) {
        return user != null ? user.getUsername() : "<none>";
    }
}

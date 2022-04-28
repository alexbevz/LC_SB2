package ru.bevz.LC_SB2.domain.util;

import ru.bevz.LC_SB2.domain.User;

public abstract class MessageHelper {
    public static String getAuthorName(User user) {
        return user != null ? user.getUsername() : "<none>";
    }
}

package service;

import entity.User;

public interface UserService {
    User getUserInfo(String username);

    String loginUser(String username, String password);
}

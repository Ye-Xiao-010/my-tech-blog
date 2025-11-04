package com.xiaofei.my_tech_blog.service;

import com.xiaofei.my_tech_blog.entity.User;

public interface UserService {


        User registerUser(String username, String email, String password);
        User loginUser(String username, String password);
        User getUserById(Long id);
        boolean isUsernameAvailable(String username);
        boolean isEmailAvailable(String email);

}

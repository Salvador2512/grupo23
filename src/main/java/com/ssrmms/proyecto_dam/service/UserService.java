package com.ssrmms.proyecto_dam.service;

import com.ssrmms.proyecto_dam.dto.ChangePasswordForm;
import com.ssrmms.proyecto_dam.entity.User;

public interface UserService {

    public Iterable<User> getAllUsers();

    public User createUser(User user) throws Exception;
    
    public User getUserById(Long id) throws Exception;
    
    public User updateUser(User user) throws Exception;
    
    public void deleteUser(Long id) throws Exception;
    
    public User changePassword(ChangePasswordForm form) throws Exception;
    
}

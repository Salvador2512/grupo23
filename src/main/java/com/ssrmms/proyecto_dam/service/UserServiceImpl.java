package com.ssrmms.proyecto_dam.service;

import com.ssrmms.proyecto_dam.dto.ChangePasswordForm;
import com.ssrmms.proyecto_dam.entity.User;
import com.ssrmms.proyecto_dam.repository.UserRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository repository;

    @Override
    public Iterable<User> getAllUsers() {
        //return repository.findAllByStatus(true);
        return repository.findAll();
    }

    private boolean checkUsernameAvailable(User user) throws Exception {
        Optional userFound = repository.findByUserName(user.getUserName());
        if (userFound.isPresent()) {
            throw new Exception("Username no disponible");
        }
        return true;
    }

    private boolean checkPasswordValid(User user) throws Exception {
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            throw new Exception("Password y Confirm Password no son iguales");
        }
        return true;
    }

    @Override
    public User createUser(User user) throws Exception {
        if (checkUsernameAvailable(user) && checkPasswordValid(user)) {
            user = repository.save(user);
        }
        return user;
    }

    @Override
    public User getUserById(Long id) throws Exception {

        User user = repository.findById(id).orElseThrow(() -> new Exception("El usuario a editar no existe"));
        return user;
    }

    @Override
    public User updateUser(User fromUser) throws Exception {
        User toUser = getUserById(fromUser.getId());
        mapUser(fromUser, toUser);
        return repository.save(toUser);
    }

    /**
     * Map everythin but the password.
     *
     * @param from
     * @param to
     */
    protected void mapUser(User from, User to) {
        to.setUserName(from.getUserName());
        to.setFirstName(from.getFirstName());
        to.setLastName(from.getLastName());
        to.setEmail(from.getEmail());
        to.setRoles(from.getRoles());

    }

    @Override
    public void deleteUser(Long id) throws Exception {
        User user = repository.findById(id)
                .orElseThrow(() -> new Exception("El usuario a eliminar no existe -" + this.getClass().getName()));

        repository.delete(user);
    }

    @Override
    public User changePassword(ChangePasswordForm form) throws Exception {
        User user = getUserById(form.getId());
        
        if(!user.getPassword().equals(form.getCurrentPassword())){
            throw new Exception ("Current Password no válido");
        }
        if(user.getPassword().equals(form.getNewPassword())){
            throw new Exception ("Debe ser diferente al password actual");
        }
        if(!form.getNewPassword().equals(form.getConfirmPassword())){
            throw new Exception ("Deben coincidir new password y confirm password");
        }
        user.setPassword(form.getNewPassword());
        
        return repository.save(user);        
    }
}

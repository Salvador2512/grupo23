
package com.ssrmms.proyecto_dam.repository;

import com.ssrmms.proyecto_dam.entity.User;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long>{

    public Optional<User> findByUserName(String userName);

}

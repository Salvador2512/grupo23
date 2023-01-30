package com.ssrmms.proyecto_dam.repository;

import com.ssrmms.proyecto_dam.entity.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {

    @Override
    public Iterable<Role> findAll();
}

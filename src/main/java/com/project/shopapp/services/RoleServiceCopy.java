package com.project.shopapp.services;

import com.project.shopapp.models.Role;
import com.project.shopapp.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


public record RoleServiceCopy(RoleRepository roleRepository) implements IRoleService {


    @Override
    public List<Role> getAllRoles() {
        return List.of();
    }
}

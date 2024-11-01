package com.devcrew.usermicroservice.repository;

import com.devcrew.usermicroservice.model.Permission;
import com.devcrew.usermicroservice.model.Role;
import com.devcrew.usermicroservice.model.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, Integer> {

    @Query("SELECT p FROM Permission p JOIN RolePermission rp ON p.id = rp.permission.id WHERE rp.role.id = ?1")
    List<Permission> findByRole(Integer roleId);

    @Query("SELECT r FROM Role r JOIN RolePermission rp ON r.id = rp.role.id WHERE rp.permission.id = ?1")
    List<Role> findByPermission(Integer permissionId);

    @Modifying
    @Query("DELETE FROM RolePermission rp WHERE rp.role.id = ?1")
    void deleteByRole(Integer roleId);

    @Modifying
    @Query("DELETE FROM RolePermission rp WHERE rp.permission.id = ?1")
    void deleteByPermission(Integer id);
}
package com.devcrew.usermicroservice.repository;

import com.devcrew.usermicroservice.model.Permission;
import com.devcrew.usermicroservice.model.Role;
import com.devcrew.usermicroservice.model.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * RolePermissionRepository interface extends JpaRepository to use the CRUD operations for the RolePermission entity.
 * (Relationship between Role and Permission)
 */
@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, Integer> {

    /**
     * Finds permissions by role ID.
     *
     * @param roleId the ID of the role
     * @return a list of permissions associated with the role
     */
    @Query("SELECT p FROM Permission p JOIN RolePermission rp ON p.id = rp.permission.id WHERE rp.role.id = ?1")
    List<Permission> findByRole(Integer roleId);

    /**
     * Finds roles by permission ID.
     *
     * @param permissionId the ID of the permission
     * @return a list of roles associated with the permission
     */
    @Query("SELECT r FROM Role r JOIN RolePermission rp ON r.id = rp.role.id WHERE rp.permission.id = ?1")
    List<Role> findByPermission(Integer permissionId);

    /**
     * Deletes role permissions by role ID.
     *
     * @param roleId the ID of the role
     */
    @Modifying
    @Query("DELETE FROM RolePermission rp WHERE rp.role.id = ?1")
    void deleteByRole(Integer roleId);

    /**
     * Deletes role permissions by permission ID.
     *
     * @param id the ID of the permission
     */
    @Modifying
    @Query("DELETE FROM RolePermission rp WHERE rp.permission.id = ?1")
    void deleteByPermission(Integer id);
}
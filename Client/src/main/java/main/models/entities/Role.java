package main.models.entities;
import main.enums.RoleName;

public class Role {

    private int roleId;
    private RoleName roleName;

    public Role() {
    }

    public Role(int roleId, RoleName roleName) {
        this.roleId = roleId;
        this.roleName = roleName;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public RoleName getRoleName() {
        return roleName;
    }

    public void setRoleName(RoleName roleName) {
        this.roleName = roleName;
    }
}

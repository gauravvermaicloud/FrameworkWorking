package com.boilerplate.java.entities;

import java.io.Serializable;

import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * A role entity for the roles
 * @author gaurav.verma.icloud
 */
@ApiModel(value="A role", description="The role entity for users", parent=BaseEntity.class)
public class Role extends BaseEntity implements Serializable{

	/**
	 * Creates a new instance of role
	 */
	public Role(){
		
	}
	
	/**
	 * Creates a role
	 * @param id
	 * @param roleName
	 * @param description
	 * @param isSystemRole
	 * @param isSelfAssign
	 */
	public Role(String id, String roleName, String description, boolean isSystemRole, boolean isSelfAssign){
		this.setId(id);
		this.roleName = roleName;
		this.description = description;
		this.isSystemRole = isSystemRole;
		this.isSelfAssign = isSelfAssign;
	}
	/**
	 * The name of the role
	 */
	@ApiModelProperty(value="The name of the role"
			,required=true)
	private String roleName;
	
	/**
	 * This defines if the role is a system role.
	 * This may be used to distinguish roles created
	 * as part of seed data and created by user
	 */
	@ApiModelProperty(value="This tells if the role is a system role"
			,required=true, notes="System roles are roles created as part of seed data")
	private boolean isSystemRole;
	
	/**
	 * This describes the role
	 */
	@ApiModelProperty(value="The descripiton of the role"
			,required=true)
	private String description;
	
	/**
	 * This tells if the user can self assign the role
	 */
	@ApiModelProperty(value="This tells if the user can self assign this role, if not"
			+ "then users belonging to Admin role or RoleAssigner role can grant access to"
			+ "users."
			,required=true)
	private boolean isSelfAssign;

	/**
	 * @see BaseEntity.validate
	 */
	@Override
	public boolean validate() throws ValidationFailedException {
		return true;
	}
	
	/**
	 * gets the role name
	 * @return the role name
	 */
	public String getRoleName() {
		return roleName;
	}

	/**
	 * sets the role name
	 * @param roleName The role name
	 */
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	/**
	 * Tells if the role is a system role
	 * @return True if the role is a system role
	 */
	public boolean getIsSystemRole() {
		return isSystemRole;
	}

	public void setIsSystemRole(boolean isSystemRole) {
		this.isSystemRole = isSystemRole;
	}

	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description of the role
	 * @param description The description of the role
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Returns if the role is a self assign role
	 * @return True if users can self assign else false
	 */
	public boolean getIsSelfAssign() {
		return isSelfAssign;
	}

	/**
	 * Sets if the role is a self assign role
	 * @param isSelfAssign The value is true if users can
	 * self assign else false
	 */
	public void setIsSelfAssign(boolean isSelfAssign) {
		this.isSelfAssign = isSelfAssign;
	}

	/**
	 * @see BaseEntity.transformToInternal
	 */
	@Override
	public BaseEntity transformToInternal() {
		return this;
	}

	/**
	 * @see BaseEntity.transformToExternal
	 */
	@Override
	public BaseEntity transformToExternal() {
		return this;
	}
}

package com.restfulbackend.modules.sys.entity;

public class User {

	public User(){
		this.id = Long.valueOf(0);
		this.uuid = "";
		this.username = "";
		this.password = "";
		this.forgetDigest = "";
		this.accessLevel = 0;
		this.userProfileId = Long.valueOf(0);
		this.createdAt = "";
		this.updatedAt = "";
		this.userProfile = new UserProfile();
	}

    private Long id;
    
    private String uuid;

    private String username;
    
    private String password;

	private String forgetDigest;

	private int accessLevel;

	private long roleId;

	private Long userProfileId;

    private String createdAt;

	private String updatedAt;

	private UserProfile userProfile;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getAccessLevel() {
		return accessLevel;
	}

	public void setAccessLevel(int accessLevel) {
		this.accessLevel = accessLevel;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}

	public UserProfile getUserProfile() {
		return userProfile;
	}

	public void setUserProfile(UserProfile userProfile) {
		this.userProfile = userProfile;
	}

	public Long getUserProfileId() {
		return userProfileId;
	}

	public void setUserProfileId(Long userProfileId) {
		this.userProfileId = userProfileId;
	}

	public String getForgetDigest() {
		return forgetDigest;
	}

	public void setForgetDigest(String forgetDigest) {
		this.forgetDigest = forgetDigest;
	}

	public long getRoleId() {
		return roleId;
	}

	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}
}

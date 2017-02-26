package com.restfulbackend.modules.sys.service;

import java.util.List;

import com.restfulbackend.modules.sys.entity.*;

public interface UserService {
	/**
	 * 根据id获取用户
	 */
	public User getUserById(long id);

	public User getUserByUsername(String username);

	public User getUserByUUID(String uuid);

	public User validateUser(User user);

	public int insertUser(User user);

	public int insertUserProfile(UserProfile userProfile);

	public int updateUser(User user);

	public int updateUserPassword(User user);

	public int updateUserProfileId(User user);

	public int updateUserProfile(UserProfile userProfile);

	public int updateUserProfileAvatar(UserProfile userProfile);

	public int updateUserAccessLevel(User user);
//
	public UserProfile getUserProfileById(Long id);

	public UserProfile getUserProfileByEmail(String email);

	public String getNickByUserName(String userName);

	public User getUserByEmail(String email);

	public UserFriendList getUserFriends(String ownerUuid);

	public List<UserBlock> getUserBlock(String ownerUuid);

	public Long insertUserFriend(UserFriend userFriend);

	public Long insertUserBlock(UserBlock userBlock);

	public List<User> getUsersByRole(String roleId);
}

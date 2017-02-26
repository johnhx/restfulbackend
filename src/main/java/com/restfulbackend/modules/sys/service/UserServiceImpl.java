package com.restfulbackend.modules.sys.service;

import com.restfulbackend.modules.sys.dao.UserBlockMapper;
import com.restfulbackend.modules.sys.dao.UserFriendMapper;
import com.restfulbackend.modules.sys.dao.UserProfileMapper;
import com.restfulbackend.modules.sys.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.restfulbackend.modules.sys.dao.UserMapper;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
	private UserMapper userMapper;

	private UserProfileMapper userProfileMapper;

	private UserFriendMapper userFriendMapper;

	private UserBlockMapper userBlockMapper;

	public UserMapper getUserMapper() {
		return userMapper;
	}

	@Autowired
	public void setUserMapper(UserMapper userMapper) {
		this.userMapper = userMapper;
	}

	@Autowired
	public void setUserProfileMapper(UserProfileMapper userProfileMapper) {
		this.userProfileMapper = userProfileMapper;
	}

	@Autowired
	public void setUserFriendMapper(UserFriendMapper userFriendMapper){
		this.userFriendMapper = userFriendMapper;
	}

	@Autowired
	public void setUserBlockMapper(UserBlockMapper userBlockMapper){
		this.userBlockMapper = userBlockMapper;
	}

//	@Override
//	public List<User> getAllUsers(){
//		return userMapper.getAllUsers();
//	}
////
	@Override
	public User validateUser(User user){
		return userMapper.validate(user);
	}

	@Override
	public User getUserById(long id) {
		return userMapper.selectByPrimaryKey(id);
	}

	@Override
	public User getUserByEmail(String email){
		return userMapper.selectByEmail(email);
	}

	@Override
	public User getUserByUsername(String username) {
		return userMapper.selectByUsername(username);
	}

	@Override
	public String getNickByUserName(String userName){
		return userMapper.selectNickByUserName(userName);
	}

	@Override
	public User getUserByUUID(String uuid){
		return userMapper.selectByUUID(uuid);
	}

	@Override
	public int insertUser(User user){
		return userMapper.insert(user);
//		return 0;
	}

	@Override
	public int insertUserProfile(UserProfile userProfile){
		return userMapper.insertProfile(userProfile);
	}

	@Override
	public int updateUser(User user){
		return userMapper.update(user);
//		return null;
	}

	@Override
	public int updateUserPassword(User user){
		return userMapper.updatePassword(user);
	}

	@Override
	public int updateUserProfileId(User user){
		return userMapper.updateUserProfileId(user);
	}

	@Override
	public int updateUserProfile(UserProfile userProfile){
		return userProfileMapper.update(userProfile);
	}

	@Override
	public int updateUserProfileAvatar(UserProfile userProfile){
		return userProfileMapper.updateAvatar(userProfile);
	}

	@Override
	public int updateUserAccessLevel(User user){
		return userMapper.updateAccessLevel(user);
	}
//
	@Override
	public UserProfile getUserProfileById(Long id){
		return userProfileMapper.selectByPrimaryKey(id);
	}

	@Override
	public UserProfile getUserProfileByEmail(String email){
		return userProfileMapper.selectByEmail(email);
	}

	@Override
	public UserFriendList getUserFriends(String ownerUuid) {
		return userFriendMapper.selectByOwner(ownerUuid);
	}

	@Override
	public List<UserBlock> getUserBlock(String ownerUuid) {
		return (List<UserBlock>) userBlockMapper.selectByOwner(ownerUuid);
	}

	@Override
	public Long insertUserFriend(UserFriend userFriend) {
		return userFriendMapper.insert(userFriend);
	}

	@Override
	public Long insertUserBlock(UserBlock userBlock) {
		return userBlockMapper.insert(userBlock);
	}

	@Override
	public List<User> getUsersByRole(String roleId){
		UserByRole userByRole = userMapper.selectByRole(roleId);

		if(userByRole == null){
			return null;
		}

		return userByRole.getUsers();
	}
}

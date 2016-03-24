package com.mao.easyword;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.mao.bean.User;

public class UserManager {

	private static User mCurrentUser;
	
	private final static UserManager sUserManager = new UserManager();
	
	private ReadWriteLock mLock = new ReentrantReadWriteLock();
	
	private UserManager() {}
	
	public static UserManager getInstance() {
		return sUserManager;
	}
	
	public void setCurrentUser(User user) {
		mLock.writeLock().lock();
		try {
			mCurrentUser = user;	
		} finally {
			mLock.writeLock().unlock();
		}
	}
	
	public User getCurrentUser() {
		mLock.readLock().lock();
		try {
			return mCurrentUser;
		} finally {
			mLock.readLock().unlock();
		}
	}
}

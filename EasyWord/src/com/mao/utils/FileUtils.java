package com.mao.utils;

import java.io.File;
import java.io.IOException;

import android.text.TextUtils;

/**
 * 文件操作工具类
 * 
 * @author mao
 *
 */
public class FileUtils {

	/**
	 * 判断指定的路径文件或者文件夹是否存在
	 * 
	 * @param path 指定的路径
	 * @return 存在返回true,不存在返回false.
	 */
	public static boolean exist(String path) {
		if(TextUtils.isEmpty(path)) {
			return false;
		}
		File f = new File(path);
		if(f.exists()) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 递归清除指定目录下的所有文件及文件夹
	 * 
	 * @param path 指定的目录路径
	 */
	public static void clearDir(String path) {
		if(exist(path)) {
			File f = new File(path);
			if(f.isDirectory()) {
				File[] files = f.listFiles();
				for(File file : files) {
					clearDir(file.getAbsolutePath());
				}
			} else {
				f.delete();
			}
		}
	}
	
	/**
	 * 创建指定目录
	 * 
	 * @param path 要创建的目录路径
	 * @return 创建成功返回true,失败返回false.注意如果已存在也是返回false.
	 */
	public static boolean createDirIfNotExist(String path) {
		if(!exist(path)) {
			File f = new File(path);
			return f.mkdirs();
		}
		return false;
	}
	
	/**
	 * 创建指定文件
	 * 
	 * @param path 要创建的文件路径
	 * @return 创建成功返回true,失败返回false.注意如果已存在也是返回false.
	 */
	public static boolean createFileIfNotExist(String path) {
		if(!exist(path)) {
			File f = new File(path);
			try {
				return f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}
	
	/***
	 * 复制文件
	 * 
	 * @param srcPath 源文件路径,包括文件名
	 * @param dstPath 目标文件路径,包括文件名
	 * @return 成功返回true,失败返回false.
	 */
	public static boolean copyFile(String srcPath, String dstPath) {
		if(exist(srcPath)) {
			if(createFileIfNotExist(dstPath)) {
				return IoUtils.copy(srcPath, dstPath);
			}
		}
		return false;
	}
	
	/**
	 * 获取指定目录所有文件名(包括:文件名,目录名,隐藏文件,隐藏文件夹)
	 * 
	 * @param path 指定的目录
	 * @return 返回指定目录下所有文件名
	 */
	public static String[] getDirectory(String path) {
		if(exist(path)) {
			return new File(path).list();
		} else {
			return null;
		}
	}
	
	/**
	 * 删除某一文件或目录,如果是目录会删除目录下所有文件和文件夹
	 * 注意:该方法并没有进行事务操作,因此可能会出现删除了部分文件(或文件夹)
	 * 的情况,此时返回false.
	 * 
	 * @param path 指定路径
	 * @return 完全删除成功返回true,否则返回false.
	 */
	public static boolean removeFileOrDirectory(String path) {
		if(exist(path)) {
			File f = new File(path);
			if(f.isFile()) {
				return f.delete();
			} else if(f.isDirectory()) {
				//递归删除文件夹
				return false;//暂不支持
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
}

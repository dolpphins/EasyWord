package com.mao.utils;

import java.io.File;
import java.io.IOException;

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
}

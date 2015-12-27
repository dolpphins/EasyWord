package com.mao.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

/**
 * IO操作工具类
 * 
 * @author mao
 *
 */
public class IoUtils {

	/**
	 * 采用对象序列化实现彻底深度对象复制
	 * 
	 * @param obj 要复制的对象
	 * @return 复制成功相应新的对象,复制失败返回null.
	 */
	public static <T extends Serializable> T copyObject(Object obj) {
		if(obj == null) {
			return null;
		}
		ByteArrayOutputStream baos = null;
		ObjectOutputStream oos = null;
		ByteArrayInputStream bais = null;
		ObjectInputStream ois = null;
		try {
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(obj);
			bais = new ByteArrayInputStream(baos.toByteArray());
			ois = new ObjectInputStream(bais);
			Object object = ois.readObject();
			return (T) object;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			closeOutputStream(oos);
			closeOutputStream(baos);
			closeInputStream(ois);
			closeInputStream(bais);
		}
	}
	
	/**
	 * 关闭输出流
	 * 
	 * @param os 要关闭的输出流
	 * @return 关闭成功返回true,失败返回false.
	 */
	public static boolean closeOutputStream(OutputStream os) {
		if(os == null) {
			return false;
		}
		try {
			os.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 关闭输出入流
	 * 
	 * @param is 要关闭的输入流
	 * @return 关闭成功返回true,失败返回false.
	 */
	public static boolean closeInputStream(InputStream is) {
		if(is == null) {
			return false;
		}
		try {
			is.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 复制指定路径对应的流到指定路径
	 * 
	 * @param srcPath 源路径
	 * @param dstPath 目的路径
	 * @return 复制成功返回true,失败返回false.
	 */
	public static boolean copy(String srcPath, String dstPath) {
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		try {
			fis = new FileInputStream(srcPath);
			bis = new BufferedInputStream(fis);
			fos = new FileOutputStream(dstPath);
			bos = new BufferedOutputStream(fos);
			
			int length;
			byte[] b = new byte[1024];
			while((length = bis.read(b)) != -1) {
				bos.write(b, 0, length);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			closeInputStream(bis);
			closeInputStream(fis);
			closeOutputStream(bos);
			closeOutputStream(fos);
		}
	}
}

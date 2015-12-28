package com.mao.loader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.mao.bean.Note;
import com.mao.cache.DiskCacheManager;
import com.mao.conf.AppConfig;
import com.mao.db.OrmDatabaseHelper;
import com.mao.utils.FileUtils;

import android.content.Context;
import android.text.TextUtils;

/**
 * 笔记加载器,包括读取、保存等
 * 
 * @author mao
 *
 */
public class NoteLoader {

	private final static NoteLoader sInstance = new NoteLoader();
	
	private NoteLoader() {}
	
	public static NoteLoader getInstance() {
		return sInstance;
	}
	
//	/**
//	 * 创建或者更新一条笔记
//	 * 
//	 * @param context 上下文
//	 * @param item 相关信息
//	 * @return 执行成功返回true,失败返回false.
//	 */
//	public boolean createOrUpdate(Context context, Note item, List<String> fileUrlList) {
//		//先更新数据库
//		if(createOrUpdateDB(context, item)) {
//			//再保存相关文件
//			if(createOrUpdateFile(context, item, fileUrlList)) {
//				return true;
//			}
//		}
//		return false;
//	}
	
	/**
	 * 创建(不存在的话)或更新一条数据库记录
	 * 
	 * @param context 上下文
	 * @param item 相关信息
	 * @return 成功返回true,失败返回false.
	 */
	public boolean createOrUpdateDB(Context context, Note item) {
		if(context == null || item == null) {
			return false;
		}
		OrmDatabaseHelper ormHelper = new OrmDatabaseHelper(context);
		Dao<Note, Integer> dao = ormHelper.getNoteDao();
		try {	
			//以id为判断条件
			dao.createOrUpdate(item);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 复制相关文件(如果已存在则更新)
	 * 
	 * @param context 上下文
	 * @param dstPath 目的文件根路径
	 * @param fileMap 要复制文件信息,其中key为目的文件名,value为源文件路径
	 * @return 返回所有复制成功的目标文件路径集合,失败返回null.
	 */
	public List<String> createOrUpdateFile(Context context, String path, Map<String, String> fileMap) {
		//以GUID判断是否存在
		if(context == null || fileMap == null 
						   || TextUtils.isEmpty(path)
						   || fileMap.size() <= 0) {
			return null;
		}
		FileUtils.createDirIfNotExist(path);
		//FileUtils.clearDir(path);
		//开始复制文件
		List<String> successSrcPathList = new ArrayList<String>();
		Set<Entry<String, String>> entrys = fileMap.entrySet();
		for(Entry<String, String> entry : entrys) {
			String dstName = entry.getKey();
			String srcPath = entry.getValue();
			String dstPath = path + dstName;
			if(FileUtils.exist(dstPath) || FileUtils.copyFile(srcPath, dstPath)) {
				successSrcPathList.add(dstPath);
			}
		}
		//删除掉其它文件
		String[] filenames = FileUtils.getDirectory(path);
		if(filenames != null) {
			for(String s : filenames) {
				if(!successSrcPathList.contains(s)) {
					FileUtils.removeFileOrDirectory(s);
				}
			}
		}
		
		return successSrcPathList;
	}
	
	/**
	 * 判断在Note表中是否已已存在某条记录
	 * 
	 * @param context
	 * @param GUID
	 * @return
	 */
	public boolean exist(Context context, String GUID) {
		if(context != null && !TextUtils.isEmpty(GUID)) {
			OrmDatabaseHelper ormHelper = new OrmDatabaseHelper(context);
			Dao<Note, Integer> dao = ormHelper.getNoteDao();
			try {
				List<Note> list = dao.queryForEq("GUID", GUID);
				if(list != null && list.size() == 0) {
					return true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	/**
	 * 构建笔记中文件url字符串表示形式
	 * 
	 * @param urlList
	 * @return
	 */
	public String buildSurroundUrl(String url) {
		if(TextUtils.isEmpty(url)) {
			return null;
		}
		StringBuilder sb = new StringBuilder(AppConfig.NOTE_URL_START_MARK);
		sb.append(url);
		sb.append(AppConfig.NOTE_URL_END_MARK);
		return sb.toString();
	}
	
	public String buildDestinationUrl(Context context, String url, String GUID) {
		if(context == null || TextUtils.isEmpty(url) || TextUtils.isEmpty(GUID)) {
			return null;
		}
		
		StringBuilder sb = new StringBuilder(DiskCacheManager.getInstance().getCacheRootPath(context));
		sb.append("/");
		sb.append(GUID);
		sb.append("/");
		int index = url.lastIndexOf("/");
		if(index >= 0) {
			sb.append(url.substring(index + 1, url.length()));
		} else {
			sb.append(url);
		}
		return sb.toString();
	}
	
	/**
	 * 从指定的字符串中提取所有url
	 * 
	 * @param content 指定的字符串
	 * @return 提取成功返回一个url集合,失败返回null.
	 */
	public List<String> findFileUrls(String content) {
		if(TextUtils.isEmpty(content)) {
			return null;
		}
		List<String> list = new ArrayList<String>();
		StringBuilder sb = new StringBuilder();
		sb.append("(?<=");
		sb.append(AppConfig.NOTE_URL_START_MARK);
		sb.append(")(.*?)(");
		sb.append(AppConfig.NOTE_URL_END_MARK);
		sb.append(")");
		Pattern pattern = Pattern.compile(sb.toString());
		Matcher matcher = pattern.matcher(content);
		while(matcher.find()) {
			String s = matcher.group();
			if(!TextUtils.isEmpty(s)) {
				list.add(s.replace(AppConfig.NOTE_URL_END_MARK, ""));
			}
		}
		return list;
	}
	
	/**
	 * 读取所有笔记信息,该列表将会按更新时间按降序排序
	 * 
	 * @param context 上下文,不能为null.
	 * @return 读取成功返回笔记列表,失败返回null.
	 */
	public List<Note> queryAll(Context context) {
		if(context == null) {
			return null;
		}
		OrmDatabaseHelper helper = new OrmDatabaseHelper(context);
		Dao<Note, Integer> dao = helper.getNoteDao();
		try {
			QueryBuilder<Note, Integer> queryBuilder = dao.queryBuilder();
			//按更新时间降序排序
			queryBuilder = queryBuilder.orderBy(Note.getUpdateTimeColumnName(), false);
			return queryBuilder.query();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}

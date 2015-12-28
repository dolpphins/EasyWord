package com.mao.conf;

/**
 * 保存所有Activity的RequestCode和ResultCode
 * 
 * @author mao
 *
 */
public class ActivityRequestResultCode {

	/** SelectPictureActivity请求码 */
	public final static int SELECT_PICTURE_ACTIVITY_REQUEST_CODE = 1;
	
	/** 拍照请求码 */
	public final static int TAKE_PHOTO_ACTIVITY_REQUEST_CODE = 2;
	
	/** 从登录界面跳转到注册界面请求码 */
	public final static int LOGIN_TO_REGISTER_ACTIVITY_REQUEST_CODE = 3;
	
	/** 从笔记列表界面跳转到添加或修改界面请求码 */
	public final static int NOTE_LIST_TO_ADDORREVISE_ACTIVITY_REQUEST_CODE = 4;
	
	/** 从笔记列表界面跳转到查看笔记界面请求码 */
	public final static int NOTE_LIST_TO_VIEW_ACTIVITY_REQUEST_CODE = 5;
	
	/** 从笔记查看界面跳转到笔记修改界面请求码 */
	public final static int NOTE_VIEW_TO_REVISE_ACTIVITY_REQUEST_CODE = 6;
	
	
	
	/** SelectPictureActivity结果码 */
	public final static int SELECT_PICTURE_ACTIVITY_RESULT_CODE = 1001;
	
	/** NoteAddOrReviseActivity结果码 */
	public final static int NOTE_ADDORREVISE_ACTIVITY_RESULT_CODE = 1002;
	
	/** NoteViewActivity结果码 */
	public final static int NOTE_VIEW_ACTIVITY_RESULT_CODE = 1003;
	
}

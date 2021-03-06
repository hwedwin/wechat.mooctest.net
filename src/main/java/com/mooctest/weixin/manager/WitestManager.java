package com.mooctest.weixin.manager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.mooctest.weixin.entity.*;
import com.mooctest.weixin.util.HttpRequestUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;


public class WitestManager {
	
	//服务器url
	//private static String server = "http://976a29f6.ngrok.io/weixin/";
	private static String server= "http://wechat.mooctest.net/weixin/";
	 
	
	public static String account_page=server +"q/account/info";  //账号信息页面url
	public static String bind_page=server+"q/account/new";  //账号绑定页面url
	public static String check_account=server+"q/account/check";  //验证账号控制器url
	public static String task_page=server+"q/task/query";//任务密码url	
	public static String grade_page=server+"q/task/grade";//任务成绩url
	public static String group_page=server+"q/group/query";//我的群组url
	public static String join_page=server+"q/group/join";//加入群组url
	public static String createquiz_page=server+"q/quiz/create";//创建小测url
	public static String showquiz_page=server+"q/quiz/show";//参与小测url
	public static String quiz_result_page=server +"q/answer/result?quizid=QUIZID";//老师端本次小测结果url
	public static String quiz_result_page1=server+"q/answer/toresult";//老师端历次小测结果url
	public static String rollcall_create_page=server+"q/rollcall/create";//创建点名url
	public static String rollcall_join_page=server+"q/rollcall/join_rollcall";//参与点名url
	public static String rollcall_result_page=server+"q/rollcall/result?openid=OPEN&rollcallid=ROLLCALLID";//老师端点名结果
	public static String manager_grade_page=server+"q/task/manager_grade";
	public static String manager_task_page=server+"q/task/manager_task";
	
	//慕测主站url
	private static String moocserver="http://www.mooctest.net/api/wechat";
	
	public static String is_Mooc_url=moocserver+"/checkWorker";
	public static String taskinfo_url=moocserver+"/getTaskInfo";
	public static String taskgrade_url=moocserver+"/getFinishedTaskInfo";
	public static String group_url=moocserver+"/getGroupList";
	public static String join_url=moocserver+"/joinGroup";
	public static String is_Manager_url=moocserver+"/checkManager";
	public static String getstudent_url=moocserver+"/getWorkersByGroup";
	public static String group_url2=moocserver+"/getGroupsByManager";
	public static String accountinfo_url=moocserver+"/getUserInfo";
	public static String manager_task_url=moocserver+"/getManagerTask";
	public static String worker_grade_url=moocserver+"/getWorkersGrade";
	public static String worker_password_url=moocserver+"/getWorkersPassword";
	
	//判断用户身份
	public static int identity(String openid){		
		return Managers.accountManager.checkAccount(openid);
	}	
	
	//判断用户输入的慕测学生账号密码是否正确
	public static int isWorker(String username,String password){	
		try{
			String param="account="+username+"&password="+password;
			String result=HttpRequestUtil.sendGet(is_Mooc_url, param);
			JSONObject jsonObject=JSONObject.fromObject(result);	
			if(jsonObject==null){
				return 0;
			}
			JSONObject object=JSONObject.fromObject(jsonObject.get("data"));
			int isMooc=object.getInt("id");
			System.out.println(isMooc);
			return isMooc;
		}catch(Exception ex){
			ex.printStackTrace();
			return 0;
		}
	} 
	
	//判断用户输入的慕测老师账号密码是否正确
	public static int isManager(String username,String password){
		try {
			String param="account="+username+"&password="+password;
			String result=HttpRequestUtil.sendGet(is_Manager_url, param);
			JSONObject jsonObject=JSONObject.fromObject(result);			
			JSONObject object=JSONObject.fromObject(jsonObject.get("data"));
			if(object==null)
				return 0;
			int id=object.getInt("id");
			return id;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	//从主站获取生成任务信息
	public static List<TaskInfo> getTaskInfo(String username) throws IOException{
		String param="account="+username;
		String result=HttpRequestUtil.sendGet(taskinfo_url, param);
		JSONObject jsonObject=JSONObject.fromObject(result);
		JSONArray ja=JSONArray.fromObject(jsonObject.get("data"));
		JSONObject obj=new JSONObject();
		List<TaskInfo> list=new ArrayList<TaskInfo>();
		for(int i=0;i<ja.size();i++){
			obj=ja.getJSONObject(i);
			TaskInfo taskInfo=new TaskInfo();
			taskInfo=(TaskInfo)JSONObject.toBean(obj, TaskInfo.class);
			list.add(taskInfo);
		}
		return list;
	}
	
	//获取任务成绩
	public static List<FinishedTask> getFinishedTaskInfo (String username) throws IOException{
		String param="account="+username;
		String result=HttpRequestUtil.sendGet(taskgrade_url, param);
		JSONObject jsonObject=JSONObject.fromObject(result);
		JSONArray ja=JSONArray.fromObject(jsonObject.get("data"));
		List<FinishedTask> list=new ArrayList<>();
		JSONObject obj=new JSONObject();
		FinishedTask fTask=new FinishedTask();
		for(int i=0;i<ja.size();i++){
			obj=ja.getJSONObject(i);
			fTask=(FinishedTask)JSONObject.toBean(obj, FinishedTask.class);
			list.add(fTask);
		}
		return list;
	}
	
	//获取群组信息
	public static List<Group> getGroup(String username) throws IOException{
		String param="account="+username;
		String result=HttpRequestUtil.sendGet(group_url, param);
		JSONObject jsonObject=JSONObject.fromObject(result);
		JSONArray ja=JSONArray.fromObject(jsonObject.get("data"));
		List<Group> list=new ArrayList<Group>();
		Group group=new Group();
		JSONObject obj=new JSONObject();
		for(int i=0;i<ja.size();i++){
			obj=ja.getJSONObject(i);
			group=(Group)JSONObject.toBean(obj, Group.class);
			list.add(group);
		}
		return list;
	}
	
	//加入群组
	public static JoinResult joinGroup(String username,String groupId,String managerName){
		JoinGroup joinGroup=new JoinGroup();
		joinGroup.setAccount(username);
		joinGroup.setGroupId(groupId);
		joinGroup.setManagerName(managerName);
		//对象转JSON
		String param=JSONSerializer.toJSON(joinGroup).toString();
		String result=HttpRequestUtil.post(join_url, param);
		JSONObject jsonObject=JSONObject.fromObject(result);
		//JSON转对象
		JoinResult jr=(JoinResult)JSONObject.toBean(jsonObject, JoinResult.class);
		return jr;
	}
	
	//根据老师账号获取群组
	public static List<Group> getGroup2(int id){
		List<Group> list=new ArrayList<Group>();
		String param="managerId="+id;
		String result=HttpRequestUtil.sendGet(group_url2, param);
		JSONObject jsonObject=JSONObject.fromObject(result);
		JSONObject object=JSONObject.fromObject(jsonObject.get("data"));
		JSONArray ja=JSONArray.fromObject(object.get("groups"));
		Group group=new Group();
		JSONObject obj=new JSONObject();
		for(int i=0;i<ja.size();i++){
			obj=ja.getJSONObject(i);
			group=(Group)JSONObject.toBean(obj, Group.class);
			list.add(group);
		}
		return list;
	}
	
	//根据群组ID获取成员
	public static List<Worker> getMember(String groupId){
		List<Worker> list=new ArrayList<Worker>();
		String param="groupId="+groupId;
		String result=HttpRequestUtil.sendGet(getstudent_url, param);
		JSONObject jsonObject=JSONObject.fromObject(result);
		JSONObject jObject=JSONObject.fromObject(jsonObject.get("data"));
		JSONArray jArray=JSONArray.fromObject(jObject.get("workers"));
		Worker worker=new Worker();
		JSONObject object=new JSONObject();
		for(int i=0;i<jArray.size();i++){
			object=jArray.getJSONObject(i);
			worker=(Worker)JSONObject.toBean(object, Worker.class);
			list.add(worker);
		}
		return list;
	}
	
	//根据慕测账号获取用户信息
	public static Accountinfo getInfo(int id,String type){
		String param="userId="+id+"&identity="+type;
		String result=HttpRequestUtil.sendGet(accountinfo_url, param);
		JSONObject jsonObject=JSONObject.fromObject(result);
		JSONObject object=JSONObject.fromObject(jsonObject.get("data"));
		Accountinfo accountinfo=(Accountinfo)JSONObject.toBean(object, Accountinfo.class);
		return accountinfo;
	}

	//根据老师id获取已结束的任务
	public static List<Task> getFinishedTask(int id){
		List<Task> list=new ArrayList<Task>();
		String param="userId="+id;
		String result=HttpRequestUtil.sendGet(manager_task_url,param);
		JSONObject jsonObject=JSONObject.fromObject(result);
		JSONObject object=JSONObject.fromObject(jsonObject.get("data"));
		JSONArray ja=JSONArray.fromObject(object.get("tasks"));
		JSONObject obj=new JSONObject();
		Task task=new Task();
		for(int i=0;i<ja.size();i++){
			obj=ja.getJSONObject(i);
			task=(Task)JSONObject.toBean(obj,Task.class);
			if(task.getStatus()==2) {
				list.add(task);
			}else
				continue;
		}
		return list;
	}

	//根据老师id获取正在进行的任务
	public static List<Task> getCurrentTask(int id){
		List<Task> list=new ArrayList<Task>();
		String param="userId="+id;
		String result=HttpRequestUtil.sendGet(manager_task_url,param);
		JSONObject jsonObject=JSONObject.fromObject(result);
		JSONObject object=JSONObject.fromObject(jsonObject.get("data"));
		JSONArray ja=JSONArray.fromObject(object.get("tasks"));
		JSONObject obj=new JSONObject();
		Task task=new Task();
		for(int i=0;i<ja.size();i++){
			obj=ja.getJSONObject(i);
			task=(Task)JSONObject.toBean(obj,Task.class);
			if(task.getStatus()==1) {
				list.add(task);
			}else
				continue;
		}
		return list;
	}

	//根据老师id获取未开始的任务
	public static List<Task> getUnstartedTask(int id){
		List<Task> list=new ArrayList<Task>();
		String param="userId="+id;
		String result=HttpRequestUtil.sendGet(manager_task_url,param);
		JSONObject jsonObject=JSONObject.fromObject(result);
		JSONObject object=JSONObject.fromObject(jsonObject.get("data"));
		JSONArray ja=JSONArray.fromObject(object.get("tasks"));
		JSONObject obj=new JSONObject();
		Task task=new Task();
		for(int i=0;i<ja.size();i++){
			obj=ja.getJSONObject(i);
			task=(Task)JSONObject.toBean(obj,Task.class);
			if(task.getStatus()==0){
				list.add(task);
			}else
				continue;
		}
		return list;
	}

	//根据任务id获取workers成绩
	public static List<Grade> getWorkersGrade(String id){
		List<Grade> list=new ArrayList<Grade>();
		String param="taskId="+id;
		String result=HttpRequestUtil.sendGet(worker_grade_url,param);
		JSONObject jsonObject=JSONObject.fromObject(result);
		JSONObject object=JSONObject.fromObject(jsonObject.get("data"));
		JSONArray ja=JSONArray.fromObject(object.get("grades"));
		Grade grade=new Grade();
		JSONObject obj=new JSONObject();
		for(int i=0;i<ja.size();i++){
			obj=ja.getJSONObject(i);
			grade=(Grade)JSONObject.toBean(obj,Grade.class);
			list.add(grade);
		}
		return list;
	}

	//根据任务id获取workers密码
	public static List<Password> getWorkersPassword(String id){
		List<Password> list=new ArrayList<Password>();
		String param="taskId="+id;
		String result=HttpRequestUtil.sendGet(worker_password_url,param);
		JSONObject jsonObject=JSONObject.fromObject(result);
		JSONObject object=JSONObject.fromObject(jsonObject.get("data"));
		JSONArray ja=JSONArray.fromObject(object.get("passwords"));
		Password password=new Password();
		JSONObject obj=new JSONObject();
		for(int i=0;i<ja.size();i++){
			obj=ja.getJSONObject(i);
			password=(Password)JSONObject.toBean(obj,Password.class);
			list.add(password);
		}
		return list;
	}
}

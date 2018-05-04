package com.mgp.common.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.shiro.crypto.hash.Sha256Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import com.mgp.commons.beans.EasemobGroupBean;
import com.mgp.commons.beans.RegisterBean;
import com.mgp.commons.beans.UserPushGroupBean;
import com.mgp.commons.beans.easemob.EasemobFriend;
import com.mgp.commons.beans.easemob.EasemobSignUpBean;
import com.mgp.commons.beans.easemob.EasemobUserSend;
import com.mgp.commons.beans.easemob.ImageMsgContent;
import com.mgp.commons.beans.easemob.SendInfo;
import com.mgp.commons.beans.easemob.SharingExt;
import com.mgp.commons.service.impl.easemob.EasemobChatGroup;
import com.mgp.commons.service.impl.easemob.EasemobIMUsers;
import com.mgp.commons.service.impl.easemob.EasemobSendMessage;
import com.mgp.commons.utils.easemob.RegisterUsers;
import com.mgp.usermanager.beans.User;

import io.swagger.client.model.MsgContent;
import io.swagger.client.model.Group;
import io.swagger.client.model.Msg;
import io.swagger.client.model.UserName;
import io.swagger.client.model.UserNames;
import okhttp3.*;
import retrofit2.Call;

public class EasemobUtils {

	private static Logger logger = LoggerFactory.getLogger(EasemobUtils.class);
	
    private static EasemobChatGroup easemobChatGroup = new EasemobChatGroup();
    
	private static EasemobIMUsers easemobIMUsers = new EasemobIMUsers();
	
	//private static EasemobFile easemobFile = new EasemobFile();
	
	private static EasemobSendMessage easemobSendMessage = new EasemobSendMessage();
	
	//private static EasemobChatMessage easemobChatMessage = new EasemobChatMessage();
	
	/*@Autowired(required=false)@Qualifier("mongoTemplate")
    private MongoTemplate mongoTemplate;*/
	
	/**
	 * @info 环信批量注册用户  带用户昵称
	 * @param userNameList
	 * @return
	 */
	public static List<EasemobSignUpBean> EasemobBatchRegisteredUsersNickList(List<RegisterBean> registerBeanList){
		List<EasemobSignUpBean> easemobSignUpBeanList = new ArrayList<EasemobSignUpBean>();
		List<List<RegisterBean>> userList = new ArrayList<List<RegisterBean>>();
		List<RegisterBean> list = new ArrayList<RegisterBean>();
		Properties properties = PropertiesUtils.getInfoConfigProperties();
		int secondCount = Integer.parseInt(properties.getProperty("easemob.second.users.register.count"));
		int registerCount = Integer.parseInt(properties.getProperty("easemob.users.count.register"));
		int count=secondCount*registerCount;
		//分配数据
		int cou = 0;
		for(int c = 0;c<registerBeanList.size();c++){
			list.add(registerBeanList.get(c));
			cou++;
			if(cou==count || c==registerBeanList.size()-1){
				userList.add(list);
				cou=0;
				list = new ArrayList<RegisterBean>();
			}
		}
		//分批创建
		for(int t=0;t<userList.size();t++){
			easemobSignUpBeanList.addAll(EasemobBatchRegisteredUsersNick(userList.get(t)));
		}
		
	   return easemobSignUpBeanList;
	}
	
	/**
	 * @info 环信批量添加好友
	 * @param easemobGroupBeanList 每秒10次
	 * @return
	 */
	public static List<EasemobSignUpBean> EasemobAddFriend(List<EasemobFriend> easemobFriendList){
		List<EasemobSignUpBean> easemobSignUpBeanList = new ArrayList<EasemobSignUpBean>();
		for(int i=0;i<easemobFriendList.size();i++){
		    Object result = easemobIMUsers.addFriendSingle(easemobFriendList.get(i).getUserName(), easemobFriendList.get(i).getFriendName());
		    EasemobSignUpBean easemobSignUpBean = JsonUtils.jsonToObj(result.toString(), EasemobSignUpBean.class);
		    easemobSignUpBeanList.add(easemobSignUpBean);
		    try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return easemobSignUpBeanList;
	}

	/**
	 * @info 环信批量创建群
	 * @param easemobGroupBeanList 每秒10次
	 * @return
	 */
	public static List<com.mgp.usermanager.beans.Group> EasemobBatchRegisteredGroups(List<EasemobGroupBean> easemobGroupBeanList){
		List<com.mgp.usermanager.beans.Group> groupList = new ArrayList<com.mgp.usermanager.beans.Group>();
		for(int i=0;i<easemobGroupBeanList.size();i++){
	        try {
	        	EasemobGroupBean easemobGroupBean = easemobGroupBeanList.get(i);
				Group group = new Group();
		        group.groupname(easemobGroupBean.getGroupName()).desc(easemobGroupBean.getGroupDesc())._public(easemobGroupBean.getGroupPublic())
		                                 .maxusers(easemobGroupBean.getMaxusers()).approval(easemobGroupBean.getApproval()).owner(easemobGroupBean.getOwner());
		        Object result = easemobChatGroup.createChatGroup(group);
		        EasemobSignUpBean easemobSignUpBean = JsonUtils.jsonToObj(result.toString(), EasemobSignUpBean.class);
		        com.mgp.usermanager.beans.Group sysGroup = new com.mgp.usermanager.beans.Group();
		        sysGroup.setEasemobgroupid(easemobSignUpBean.getData().get("groupid").toString());
		        sysGroup.setCreatetime(new Date());
		        sysGroup.setUsername(easemobGroupBean.getUsername());
		        sysGroup.setGroupcategoryid(easemobGroupBean.getGroupType());
		        sysGroup.setGroupcount(easemobGroupBean.getGroupCount());
		        sysGroup.setGroupnamecode(MD5Utils.getMd5(easemobGroupBean.getGroupName()).toLowerCase());
		        sysGroup.setGroupname(easemobGroupBean.getGroupName());
		        sysGroup.setGroupowner(easemobGroupBean.getOwner());
		        sysGroup.setRdindex(1);
		        sysGroup.setUpdatetime(new Date());
		        sysGroup.setGroupicon(easemobGroupBean.getGroupIcon());
		        groupList.add(sysGroup);
				Thread.sleep(100);
			} catch (Exception e) {}
        }
		return groupList;
	}
	
	/**
	 * @info 环信批量拉人入群
	 * @param easemobGroupBeanList
	 * @return
	 */
   	public static List<EasemobSignUpBean> EasemobBatchGroupsFromPushUser(String groupId, List<String> userNameList){
    	List<EasemobSignUpBean> easemobSignUpBeanList = new ArrayList<EasemobSignUpBean>();
    	List<String> userName = new ArrayList<String>();
       	List<List<String>> userNames = new ArrayList<List<String>>();
    	Properties properties = PropertiesUtils.getInfoConfigProperties();
    	int groupCount = Integer.parseInt(properties.getProperty("easemob.users.count.fromgroup"));
    	//分数据
       	int cou =0;
    	for(int c=0;c<userNameList.size();c++){
	   		userName.add(userNameList.get(c));
	   		cou++;
	   		if(cou==groupCount || c==userNameList.size()-1){
	   			userNames.add(userName);
	   			cou=0;
	   			userName = new ArrayList<String>();
	   		}
   		}
    	//分批拉人入群
   		for(int i=0;i<userNames.size();i++){
   			UserPushGroupBean userPushGroupBean = new UserPushGroupBean();
	       	userPushGroupBean.setGroupId(groupId);
   	   		userPushGroupBean.setUserNameList(userNames.get(i));
   	   		EasemobSignUpBean easemobSignUpBean= EasemobUtils.EasemobBatchPushUserGroups(userPushGroupBean);
   	   		if(easemobSignUpBean!=null){
   	   	       easemobSignUpBeanList.add(easemobSignUpBean);
   	   	    }
   	   	   	try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
   		}
   		return easemobSignUpBeanList;
   	}

	public static EasemobSignUpBean EasemobBatchPushUserGroups(UserPushGroupBean userPushGroupBeanList){
		String groupId = userPushGroupBeanList.getGroupId();
		List<String> users = userPushGroupBeanList.getUserNameList();
	    UserNames userNames = new UserNames();
	    UserName userList = new UserName();
	    for(int i=0;i<users.size();i++){    
	        userList.add(users.get(i));
	    }
	    userNames.usernames(userList);
	    EasemobSignUpBean easemobSignUpBean=null;
	    try{
	      Object result = easemobChatGroup.addBatchUsersToChatGroup(groupId, userNames);
	      easemobSignUpBean = JsonUtils.jsonToObj(result.toString(), EasemobSignUpBean.class);
	    }catch(Exception e){}
		return easemobSignUpBean;
	}
	/**
	 * @Info 注册无昵称用户
	 * @param userNameList
	 * @return
	 */
	public static List<EasemobSignUpBean> EasemobBatchRegisteredUsers(List<String> userNameList){
		 List<EasemobSignUpBean> easemobSignUpBeanList = new ArrayList<EasemobSignUpBean>();
		 Properties properties = PropertiesUtils.getInfoConfigProperties();
		 int secondCount = Integer.parseInt(properties.getProperty("easemob.second.users.register.count"));
		 int registerCount = Integer.parseInt(properties.getProperty("easemob.users.count.register"));
		 int cou = 0;
		 for(int i=0;i<secondCount;i++){
		   io.swagger.client.model.RegisterUsers users = new io.swagger.client.model.RegisterUsers();	 
		   for(int z=0;z<registerCount;z++){
			   if(userNameList.size()==cou || StringUtils.judgeBlank(userNameList.get(cou))){
				   break;
			   }
			   io.swagger.client.model.User user = new io.swagger.client.model.User();		
			   user.username(userNameList.get(cou++)).password(new Sha256Hash(properties.getProperty("easemob.users.register.password")).toHex());
		       users.add(user);	  
	       }
		   if(users.size()!=0){
			   Object result = easemobIMUsers.createNewIMUserSingle(users);
			   EasemobSignUpBean easemobSignUpBean = new EasemobSignUpBean();
			   easemobSignUpBean = JsonUtils.jsonToObj(result.toString(), EasemobSignUpBean.class);
			   easemobSignUpBeanList.add(easemobSignUpBean);
			   try {
					Thread.sleep(100);
			   } catch (InterruptedException e) {
					e.printStackTrace();
			   }
		   }
		 }
		return easemobSignUpBeanList;
	}
	/**
	 * @Info 注册昵称用户
	 * @param userNameList
	 * @return
	 */
	public static List<EasemobSignUpBean> EasemobBatchRegisteredUsersNick(List<RegisterBean> userNameList){
		 List<EasemobSignUpBean> easemobSignUpBeanList = new ArrayList<EasemobSignUpBean>();
		 Properties properties = PropertiesUtils.getInfoConfigProperties();
		 int secondCount = Integer.parseInt(properties.getProperty("easemob.second.users.register.count"));
		 int registerCount = Integer.parseInt(properties.getProperty("easemob.users.count.register"));
		 int cou = 0;
		 for(int i=0;i<secondCount;i++){
		   RegisterUsers users = new RegisterUsers();	 
		   for(int z=0;z<registerCount;z++){
			   if(userNameList.size()==cou || userNameList.get(cou)==null){
				   break;
			   }
			   com.mgp.commons.beans.User user = new com.mgp.commons.beans.User();
			   user.username(userNameList.get(cou).getUsername())
			       .password(userNameList.get(cou).getPassword())
			       .nickname(userNameList.get(cou).getNickname());
		       users.add(user);
		       cou++;
	       }
		   if(users.size()!=0){
			   try {
				   Object result = easemobIMUsers.createNewIMUserSingle(users);
				   EasemobSignUpBean easemobSignUpBean = JsonUtils.jsonToObj(result.toString(), EasemobSignUpBean.class);
				   easemobSignUpBeanList.add(easemobSignUpBean);				  
				   Thread.sleep(100);
			   } catch (Exception e) {
					
			   }
		   }
		 }
		return easemobSignUpBeanList;
	}

	/**
	 * @Info 上传用户图片或聊天信息
	 * @param userNameList
	 * @return 图片数据list，要入库
	 */
	public String EasemobUploadFile(final String username, final String fileName){
		File file = new File(fileName);
        if(!file.exists()){
        	logger.error(fileName+"文件不存在!");
        	return "fail";
        }
        if(file.length()>10*1024*1024){
        	logger.error(fileName+"文件超过限制!");
        	return "fail";
        }
        try{
	        FileUploadManager.FileUploadService service = FileUploadManager.retrofit().create(FileUploadManager.FileUploadService.class);
	
	        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
	
	        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
	
	        Call<ResponseBody> call = service.upload(body);
	        try {  
	            String result = call.execute().body().string();//4.获得返回结果  
	            String userkey = new Sha256Hash(username+fileName).toHex();
	            getRedisTemplateBean().opsForValue().set(userkey, result);
	        } catch (Exception e) {
	        	e.printStackTrace();
	        }  
        }catch(Exception e){
        	return "fail";
        }
        return "ok";
	}
	
	/**
	 * @Info 上传用户视频
	 * @param userNameList
	 * @return 图片数据list，要入库
	 */
	public String EasemobUploadVedioFile(final String username, final String fileName){
		File file = new File(fileName);
        if(!file.exists()){
        	logger.error(fileName+"文件不存在!");
        	return "fail";
        }
        if(file.length()>10*1024*1024){
        	logger.error(fileName+"文件超过限制!");
        	return "fail";
        }
        try{
	        FileUploadManager.FileUploadService service = FileUploadManager.retrofit().create(FileUploadManager.FileUploadService.class);
	
	        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
	
	        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
	
	        Call<ResponseBody> call = service.upload(body); 
	        call.enqueue(new retrofit2.Callback<ResponseBody>() {
	            @Override
	            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {        
	                if (response.isSuccessful()) {
	                	//获取返回值
	                	String result = null;
	                	try{
	                		result=response.body().string();
	                		String userkey = new Sha256Hash(username+fileName).toHex();
	                		getRedisTemplateBean().opsForValue().set(userkey, result);
	                	}catch(Exception e){
	                		
	                	}
	                	
	                }else{
	                	logger.error(fileName+"文件上传到环信失败!");
	                }
	            }
	
	            @Override
	            public void onFailure(Call<ResponseBody> call, Throwable throwable) {}
	        });
        }catch(Exception e){
        	return "fail";
        }
        return "ok";
	}
	
	/**
	 * @Info 上传用户视频
	 * @param userNameList
	 * @return 图片数据list，要入库
	 */
	public String EasemobUploadVedioFile2(final String username, final String fileName){
		File file = new File(fileName);
        if(!file.exists()){
        	logger.error(fileName+"文件不存在!");
        	return "fail";
        }
        if(file.length()>10*1024*1024){
        	logger.error(fileName+"文件超过限制!");
        	return "fail";
        }
        try{
	        FileUploadManager.FileUploadService service = FileUploadManager.retrofit().create(FileUploadManager.FileUploadService.class);
	
	        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
	
	        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
	
	        Call<ResponseBody> call = service.upload(body); 
	        call.enqueue(new retrofit2.Callback<ResponseBody>() {
	            @Override
	            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {        
	                if (response.isSuccessful()) {
	                	//获取返回值
	                	String result = null;
	                	try{
	                		result=response.body().string();
	                		String userkey = new Sha256Hash(username+fileName).toHex();
	                		getRedisTemplateBean().opsForValue().set(userkey, result);
	                	}catch(Exception e){
	                		
	                	}
	                	
	                }else{
	                	logger.error(fileName+"文件上传到环信失败!");
	                }
	            }
	
	            @Override
	            public void onFailure(Call<ResponseBody> call, Throwable throwable) {}
	        });
        }catch(Exception e){
        	return "fail";
        }
        return "ok";
	}
	
	/**
	 * @Info 环信用户批量下线
	 */
	public static void EasemobLoginout(List<User> userList){
		//下线用户
		try{
		  for(int i=0;i<userList.size();i++){
			easemobIMUsers.disconnectIMUser(userList.get(i).getUsername());
		  }
		}catch(Exception e){}
	}
	
    /**
	 * @Info 给群发送聊天消息
	 * @param userNameList
	 * @return
	 */
    public static void EasemobBatchSendInfo(List<SendInfo> sendInfoList){
    	//发送消息
    	for(int i=0;i<sendInfoList.size();i++){
    		SendInfo sendInfo = sendInfoList.get(i);
	    	if(sendInfo.getMsg().get("type").toString().equals("txt")){//发送文本与拓展消息
	    		Msg msg = new Msg();
    	        MsgContent msgContent = new MsgContent();
    	        msgContent.type(MsgContent.TypeEnum.TXT)
    	                  .msg(sendInfo.getMsg().get("msg").toString());
    	        UserName userName = new UserName();
    	        userName.add(sendInfo.getTarget().get(0));
    	        
    	        if(sendInfo.getExt()!=null){
    	        	SharingExt sharingExt = new SharingExt();
    	        	sharingExt.setTitle(sendInfo.getExt().get("title").toString());
    	        	sharingExt.setMessage(sendInfo.getExt().get("message").toString());
    	        	sharingExt.setUrl(sendInfo.getExt().get("url").toString());
    	        	sharingExt.setThumburl(sendInfo.getExt().get("thumburl").toString());
    	        	EasemobUserSend easemobUserSend = new EasemobUserSend();
    	        	easemobUserSend.setUserName(sendInfo.getNickName());
    	        	easemobUserSend.setUserIco(sendInfo.getHeadIcon());
    	        	Map<String, Object> ext = new HashMap<String, Object>();
    	        	ext.put("H5message", sharingExt);
    	        	ext.put("userInfo", easemobUserSend);
        	        msg.from(sendInfo.getFrom()).target(userName).targetType("chatgroups").msg(msgContent).ext(ext);
    	        }else{
    	        	Map<String, Object> ext = new HashMap<String, Object>();
    	        	EasemobUserSend easemobUserSend = new EasemobUserSend();
    	        	easemobUserSend.setUserName(sendInfo.getNickName());
    	        	easemobUserSend.setUserIco(sendInfo.getHeadIcon());
    	        	ext.put("userInfo", easemobUserSend);
    	        	msg.from(sendInfo.getFrom()).target(userName).targetType("chatgroups").msg(msgContent).ext(ext);
    	        }       
    	        try {
    	        	easemobSendMessage.sendMessage(msg);
					Thread.sleep(100);
			    } catch (InterruptedException e) {}
	    	}else if(sendInfoList.get(i).getMsg().get("type").toString().equals("img")){
	    		Msg msg = new Msg();
	            ImageMsgContent msgContent = new ImageMsgContent();
	            Map<String, Object> size = new HashMap<String, Object>();
                size.put("width", 480);
                size.put("height", 720);
	            Map<String, Object> map = new HashMap<String, Object>();
	            map.put("type", sendInfo.getMsg().get("type"));
	            map.put("url", sendInfo.getMsg().get("url"));
	            map.put("filename", sendInfo.getMsg().get("filename"));
	            map.put("secret", sendInfo.getMsg().get("secret"));
	            map.put("size", size);
	            msgContent.setFrom(sendInfo.getFrom());
	            msgContent.setMsg(map);
	            msgContent.setTarget(sendInfo.getTarget());
	            msgContent.setTarget_type("chatgroups");
	            UserName userName = new UserName();
	            userName.add(msgContent.getTarget().get(0));
	            Map<String, Object> ext = new HashMap<String, Object>();
	        	EasemobUserSend easemobUserSend = new EasemobUserSend();
	        	easemobUserSend.setUserName(sendInfo.getNickName());
	        	easemobUserSend.setUserIco(sendInfo.getHeadIcon());
	        	ext.put("userInfo", easemobUserSend);
	            msg.from(msgContent.getFrom()).target(userName).targetType(msgContent.getTarget_type()).msg(msgContent.getMsg()).ext(ext);	            
	            try {
	            	easemobSendMessage.sendMessage(msg);
					Thread.sleep(100);
			    } catch (InterruptedException e) {}
	    	}else if(sendInfoList.get(i).getMsg().get("type").toString().equals("audio")){
	    		Msg msg = new Msg();
	            ImageMsgContent msgContent = new ImageMsgContent();
	            msgContent.setMsg(sendInfo.getMsg());
	            msgContent.setFrom(sendInfo.getFrom());
	            msgContent.setTarget(sendInfo.getTarget());
	            msgContent.setTarget_type("chatgroups");
	            UserName userName = new UserName();
	            userName.add(msgContent.getTarget().get(0));
	            Map<String, Object> ext = new HashMap<String, Object>();
	        	EasemobUserSend easemobUserSend = new EasemobUserSend();
	        	easemobUserSend.setUserName(sendInfo.getNickName());
	        	easemobUserSend.setUserIco(sendInfo.getHeadIcon());
	        	ext.put("userInfo", easemobUserSend);
	            msg.from(msgContent.getFrom()).target(userName).targetType(msgContent.getTarget_type()).msg(msgContent.getMsg()).ext(ext);	            
	            try {
	            	easemobSendMessage.sendMessage(msg);
					Thread.sleep(100);
			    } catch (InterruptedException e) {}
	    	}else if(sendInfoList.get(i).getMsg().get("type").toString().equals("video")){
	    		Msg msg = new Msg();
	            ImageMsgContent msgContent = new ImageMsgContent();
	            msgContent.setMsg(sendInfo.getMsg());
	            msgContent.setFrom(sendInfo.getFrom());
	            msgContent.setTarget(sendInfo.getTarget());
	            msgContent.setTarget_type("chatgroups");
	            UserName userName = new UserName();
	            userName.add(msgContent.getTarget().get(0));
	            Map<String, Object> ext = new HashMap<String, Object>();
	        	EasemobUserSend easemobUserSend = new EasemobUserSend();
	        	easemobUserSend.setUserName(sendInfo.getNickName());
	        	easemobUserSend.setUserIco(sendInfo.getHeadIcon());
	        	ext.put("userInfo", easemobUserSend);
	            msg.from(msgContent.getFrom()).target(userName).targetType(msgContent.getTarget_type()).msg(msgContent.getMsg()).ext(ext);	            
	            try {
	            	easemobSendMessage.sendMessage(msg);
					Thread.sleep(100);
			    } catch (InterruptedException e) {}
	    	}else if(sendInfoList.get(i).getMsg().get("type").toString().equals("cmd")){
	    		
	    	}
	    	 
    	}
	}
    
    /**
	 * @Info 好友数据
	 */
	public static Object EasemobFriendData(String userName){
		return easemobIMUsers.getFriends(userName);
	}
	
	/**
	 * @Info user 群组数据
	 */
	/*public static Object EasemobGroupData(String userName){
		return easemobChatGroup.getGroups(userName);
	}*/
	/*public void Testtt(){
		 String path = "d:/test.jpg";
	        File file = new File(path);
	        Object result = easemobFile.uploadFile(file);
	        System.out.println(result);
	}*/
	
	 public static void EasemobAppSendInfo(SendInfo sendInfo){
	    	//发送消息
    		Msg msg = new Msg();
	        MsgContent msgContent = new MsgContent();
	        msgContent.type(MsgContent.TypeEnum.TXT)
	                  .msg(sendInfo.getMsg().get("msg").toString());
	        UserName userName = new UserName();
	        userName.add(sendInfo.getTarget().get(0));
        	Map<String, Object> ext = new HashMap<String, Object>();
        	ext.put("GroupNotice", sendInfo.getExt().get("GroupNotice").toString());
	        msg.from(sendInfo.getFrom()).target(userName).targetType("chatgroups").msg(msgContent).ext(ext);    
	        try {
	        	easemobSendMessage.sendMessage(msg);
				Thread.sleep(100);
		    } catch (InterruptedException e) {}
	 }
	 
	 private RedisTemplate<String, Object> getRedisTemplateBean(){
		 return (RedisTemplate<String, Object>)SpringContextUtils.getBean("redisTemplate");
	 }
}

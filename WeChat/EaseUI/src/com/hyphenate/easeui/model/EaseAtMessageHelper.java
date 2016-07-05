package com.hyphenate.easeui.model;

import android.text.TextUtils;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessage.ChatType;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.utils.EaseUserUtils;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EaseAtMessageHelper {
    private List<String> toAtUserList = new ArrayList<String>();
    private Set<String> atMeGroupList = null;
    private static EaseAtMessageHelper instance = null;
    public synchronized static EaseAtMessageHelper get(){
        if(instance == null){
            instance = new EaseAtMessageHelper();
        }
        return instance;
    }
    
    
    private EaseAtMessageHelper(){
        atMeGroupList = EasePreferenceManager.getInstance().getAtMeGroups();
        if(atMeGroupList == null)
            atMeGroupList = new HashSet<String>();
        
    }
    
    /**
     * add user you want to @
     * @param username
     */
    public void addAtUser(String username){
        synchronized (toAtUserList) {
            if(!toAtUserList.contains(username)){
                toAtUserList.add(username);
            }
        }
        
    }
    
    /**
     * check if be mentioned(@) in the content
     * @param content
     * @return
     */
    public boolean containsAtUsername(String content){
        if(TextUtils.isEmpty(content)){
            return false;
        }
        synchronized (toAtUserList) {
            for(String username : toAtUserList){
                String nick = username;
                if(EaseUserUtils.getUserInfo(username) != null){
                    nick = EaseUserUtils.getUserInfo(username).getNick();
                }
                if(content.contains(nick)){
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * get the users be mentioned(@) 
     * @param content
     * @return
     */
    public List<String> getAtMessageUsername(String content){
        if(TextUtils.isEmpty(content)){
            return null;
        }
        synchronized (toAtUserList) {
            List<String> list = null;
            for(String username : toAtUserList){
                String nick = username;
                if(EaseUserUtils.getUserInfo(username) != null){
                    nick = EaseUserUtils.getUserInfo(username).getNick();
                }
                if(content.contains(nick)){
                    if(list == null){
                        list = new ArrayList<String>();
                    }
                    list.add(username);
                }
            }
            return list;
        }
    }
    
    /**
     * parse the message, get and save group id if I was mentioned(@)
     * @param messages
     */
    public void parseMessages(List<EMMessage> messages) {
        int size = atMeGroupList.size();
        EMMessage[] msgs = messages.toArray(new EMMessage[]{});
        for(EMMessage msg : msgs){
            if(msg.getChatType() == ChatType.GroupChat){
                String groupId = msg.getTo();
                String usernameStr = msg.getStringAttribute(EaseConstant.MESSAGE_ATTR_AT_MSG, null);
                if(usernameStr != null){
                    String[] usernames = usernameStr.split(",");
                    for(String username : usernames){
                        if(EMClient.getInstance().getCurrentUser().equals(username)){
                            if(!atMeGroupList.contains(groupId)){
                                atMeGroupList.add(groupId);
                                break;
                            }
                        }
                    }
                    if(atMeGroupList.size() != size){
                        EasePreferenceManager.getInstance().setAtMeGroups(atMeGroupList);
                    }
                }
            }
        }
    }
    
    /**
     * get groups which I was mentioned
     * @return
     */
    public Set<String> getAtMeGroups(){
        return atMeGroupList;
    }
    
    /**
     * remove group from the list
     * @param groupId
     */
    public void removeAtMeGroup(String groupId){
        if(atMeGroupList.contains(groupId)){
            atMeGroupList.remove(groupId);
            EasePreferenceManager.getInstance().setAtMeGroups(atMeGroupList);
        }
    }
    
    /**
     * check if the input groupId in atMeGroupList
     * @param groupId
     * @return
     */
    public boolean hasAtMeMsg(String groupId){
        return atMeGroupList.contains(groupId);
    }
    
    public boolean isAtMeMsg(EMMessage message){
        EaseUser user = EaseUserUtils.getUserInfo(message.getFrom());
        if(user != null){
            String atUsername = message.getStringAttribute(EaseConstant.MESSAGE_ATTR_AT_MSG, null);
            if(atUsername != null){
                String[] atUsernames = atUsername.split(",");
                for(String username : atUsernames){
                    if(username.equals(EMClient.getInstance().getCurrentUser())){
                        return true;
                    }
                }
            }
            
        }
        return false;
    }
    
    public String atListToString(List<String> atList){
        StringBuffer sb = new StringBuffer();
        int size = atList.size();
        for(int i = 0; i < size; i++){
            String username = atList.get(i);
            sb.append(username);
            if(i != size-1){
                sb.append(",");
            }
        }
        return sb.toString();
    }
}

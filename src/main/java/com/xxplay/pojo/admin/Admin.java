package com.xxplay.pojo.admin;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.xxplay.core.base.AppContextUtils;
import com.xxplay.core.utils.DateUtils;

/**
 * 管理员
 * @Author:chenssy
 * @date:2016年4月9日
 * 
 */
public class Admin {
    private String id;

    private String userName;

    private String password;

    private String realName;

    private Date createTime;

    private String roleId;
    
    private String status;
    
    private String roleName;
    
    private String statusTxt;
    
    private String createTimeTxt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName == null ? null : realName.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId == null ? null : roleId.trim();
    }

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatusTxt() {
		if(StringUtils.isNotBlank(status)){
			statusTxt = AppContextUtils.getSysAprName("ADMIN_STATUS", status);
		}
		return statusTxt;
	}

	public String getCreateTimeTxt() {
		if(createTime != null){
			this.createTimeTxt = DateUtils.date2String(createTime, DateUtils.DATE_FORMAT2);
		}
		return createTimeTxt;
	}
}
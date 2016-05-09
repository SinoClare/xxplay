package com.xxplay.service.gameCenter.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.xxplay.core.utils.DateUtils;
import com.xxplay.core.utils.StringUtils;
import com.xxplay.dao.app.IAppInfosDao;
import com.xxplay.pojo.admin.Admin;
import com.xxplay.pojo.app.AppInfos;
import com.xxplay.pojo.app.PackInfo;
import com.xxplay.service.gameCenter.IGameApkService;
import com.xxplay.service.gameCenter.IGamePicListService;
import com.xxplay.service.gameCenter.IGameService;
import com.xxplay.utils.IConstant;

/**
 * 游戏服务 实现类
 * 
 * @file:GameServiceImpl.java
 * @package_name:com.xxplay.service.gameCenter.impl
 * @Project:xxplay
 *
 * @Author:陈明
 * @Copyright 陈明   2016 All Rights Reserved.
 * 
 * @data:2016年4月19日
 */
@Service("gameService")
public class GameServiceImpl implements IGameService{
	private final static Logger LOGGER = Logger.getLogger(GameServiceImpl.class);
	
	@Resource
	private IGameApkService gameApkService;
	
	@Resource
	private IAppInfosDao appInfosDao;
	
	@Resource
	private IGamePicListService gamePicListService;
	
	@Override
	public int addGame(Admin admin,Map<String, String> appInfoMap,
			Map<String, String> params, List<String> picList, PackInfo packInfo) {
		LOGGER.info("开始保存游戏信息");
		//构建游戏基本信息
		AppInfos appInfos = new AppInfos();
		appInfos.setAppName(appInfoMap.get("appName"));
		appInfos.setShowName(appInfoMap.get("showName"));
		appInfos.setSearchKeys(appInfoMap.get("searchKeys"));
		appInfos.setAppType(appInfoMap.get("categoryId"));
		appInfos.setIsonline(appInfoMap.get("isOnlie"));
		appInfos.setAppdesc(appInfoMap.get("appdesc"));
		appInfos.setIssueType("1");	
		appInfos.setEvilLevel(Integer.valueOf(params.get("evilLevel")));
		appInfos.setDownTimes(0);		//下载量：默认值0
		appInfos.setDownTimesreal(0);	//真实下载量：默认值0
		appInfos.setRecommFlag(params.get("recommFlag"));
		appInfos.setRecommLevel(Integer.valueOf(StringUtils.toString(params.get("recommLevel"), "1")));
		appInfos.setRecommWord(params.get("recommWord"));
		appInfos.setRemarks(params.get("remarks"));
		appInfos.setCreatTime(DateUtils.getCurrentDate());
		appInfos.setStatus(IConstant.GAME_STATUS_USING);		//游戏状态启用
		
		//如果APK为主安装包，则设置主安装包信息
		if(org.apache.commons.lang3.StringUtils.isBlank(packInfo.getIsMainver()) 
		  ||packInfo.getIsMainver().equals("0")){
			appInfos.setMainIconpicurl(packInfo.getIconPicurl());
			/*
			 * 这里存在一个循环依赖的关系：如果是增加游戏信息，那么这个时候就不会存在id（id是自增的），那么就没法设置pack的appId了，
			 * 但是pack也没保存，也没有packid，所以也就没法设置mainPackid了，所以这里的处理方式是先设置mainPackId为空值，
			 * 保存pack后反过来修改app,设置mainPackid
			 */
			appInfos.setMainPackid(packInfo.getPackid());		
			appInfos.setMainPacksize(Integer.valueOf(packInfo.getPackSize()));
			appInfos.setMainSigncode(packInfo.getSignCode());
			appInfos.setMainVercode(packInfo.getVerCode().toString());
			appInfos.setMainVername(packInfo.getVerName());
		}
		
		//保存id
		appInfosDao.insertSelective(appInfos);
		LOGGER.info("保存游戏基本信息成功....");
		
		//设置pak
		packInfo.setAppinfoId(appInfos.getId().toString());
		packInfo.setUpdateDesc(params.get("updateDesc"));
		packInfo.setIsMainver("1");	
		//保存apk信息
		gameApkService.addGameApkInfo(packInfo);
		LOGGER.info("保存游戏apk信息成功...");
		
		//保存游戏列表信息
		gamePicListService.saveGamePickList(picList,appInfos.getId());
		LOGGER.info("保存游戏图片列表成功...");
		
		return appInfos.getId();
	}

}
package com.bangqu.yinwan.shop.bean;

/**
 *用户实体类
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import com.alibaba.fastjson.JSON;
import com.bangqu.yinwan.shop.internet.SystemException;

public class UserBean implements Serializable {

	private static final long serialVersionUID = 1L;

	String id;
	String addTime;
	String version;
	String updateTime;
	String type;
	String username;
	String password;
	String nickname;
	String mobile;
	String score;
	String photo;
	String realname;
	String age;
	String male;
	String birthday;
	String constellation;
	String birthAttrib;
	String marital;
	String bloodType;
	String intro;
	String accountExpired;
	String accountLocked;
	String credentialsExpired;
	String enabled;
	String accountNonExpired;
	String accountNonLocked;
	String authorities;
	String credentialsNonExpired;
	String roleList;
	String alipay;// 支付宝

	/**
	 * 登陆时缓存对象,调用登陆接口时缓存
	 * 
	 * @param id
	 * @param username
	 * @param password
	 * @param nickname
	 * @param mobile
	 * @param photo
	 * @param realname
	 * @param age
	 * @param male
	 * @param intro
	 */
	public UserBean(String id, String username, String password,
			String nickname, String mobile, String photo, String age,
			String male, String intro) {
		super();

		this.id = id;
		this.username = username;
		this.password = password;
		this.nickname = nickname;
		this.photo = photo;
		this.age = age;
		this.male = male;
		this.intro = intro;
	}

	public UserBean() {
		super();
	}

	/**
	 * 将多个UserBean封装近list里面，返回出来
	 * 
	 * @param jsonArray
	 * @return
	 * @throws SystemException
	 */
	public static List<UserBean> constractList(JSONArray jsonArray)
			throws SystemException {
		try {
			List<UserBean> list = new ArrayList<UserBean>();
			for (int i = 0; i < jsonArray.length(); i++) {
				list.add(JSON.parseObject(
						jsonArray.getJSONObject(i).toString(), UserBean.class));
			}
			return list;

		} catch (JSONException je) {
			throw new SystemException(je.getMessage());
		}
	}

	public String getAlipay() {
		return alipay;
	}

	public void setAlipay(String alipay) {
		this.alipay = alipay;
	}

	public String getAuthorities() {
		return authorities;
	}

	public void setAuthorities(String authorities) {
		this.authorities = authorities;
	}

	public String getCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	public void setCredentialsNonExpired(String credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}

	public String getRoleList() {
		return roleList;
	}

	public void setRoleList(String roleList) {
		this.roleList = roleList;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAddTime() {
		return addTime;
	}

	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getMale() {
		return male;
	}

	public void setMale(String male) {
		this.male = male;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getConstellation() {
		return constellation;
	}

	public void setConstellation(String constellation) {
		this.constellation = constellation;
	}

	public String getBirthAttrib() {
		return birthAttrib;
	}

	public void setBirthAttrib(String birthAttrib) {
		this.birthAttrib = birthAttrib;
	}

	public String getMarital() {
		return marital;
	}

	public void setMarital(String marital) {
		this.marital = marital;
	}

	public String getBloodType() {
		return bloodType;
	}

	public void setBloodType(String bloodType) {
		this.bloodType = bloodType;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getAccountExpired() {
		return accountExpired;
	}

	public void setAccountExpired(String accountExpired) {
		this.accountExpired = accountExpired;
	}

	public String getAccountLocked() {
		return accountLocked;
	}

	public void setAccountLocked(String accountLocked) {
		this.accountLocked = accountLocked;
	}

	public String getCredentialsExpired() {
		return credentialsExpired;
	}

	public void setCredentialsExpired(String credentialsExpired) {
		this.credentialsExpired = credentialsExpired;
	}

	public String getEnabled() {
		return enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

	public String getAccountNonExpired() {
		return accountNonExpired;
	}

	public void setAccountNonExpired(String accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}

	public String getAccountNonLocked() {
		return accountNonLocked;
	}

	public void setAccountNonLocked(String accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}
}

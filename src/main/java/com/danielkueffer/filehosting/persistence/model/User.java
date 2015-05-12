package com.danielkueffer.filehosting.persistence.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

/**
 * The user entity
 * 
 * @author dkueffer
 * 
 */
@Entity
@Table(name = "user")
public class User implements Serializable {

	private static final long serialVersionUID = 345234523457645899L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@NotNull
	private String username;
	
	@NotNull
	private String password;
	
	private String email;
	private String language;

	@Column(name = "profile_image")
	private String profileImage;

	@Column(name = "display_name")
	private String displayName;

	@Column(name = "disk_quota")
	private int diskQuota;

	private int active;

	@Column(name = "notification_disk_full")
	private int notificationDiskFull;

	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name = "group_user", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "group_id"))
	private List<Group> groups;

	@Column(name = "date_created")
	private Timestamp dateCreated;

	@Column(name = "last_login")
	private Timestamp lastLogin;
	
	@Transient
	private boolean checkboxActive;
	
	@Transient
	private boolean checkboxDiskFull;
	
	@Transient
	private List<String> groupIds;
	
	@Transient
	private boolean isAdmin;

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the language
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * @param language
	 *            the language to set
	 */
	public void setLanguage(String language) {
		this.language = language;
	}

	/**
	 * @return the profileImage
	 */
	public String getProfileImage() {
		return profileImage;
	}

	/**
	 * @param profileImage
	 *            the profileImage to set
	 */
	public void setProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}

	/**
	 * @return the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * @param displayName
	 *            the displayName to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * @return the diskQuota
	 */
	public int getDiskQuota() {
		return diskQuota;
	}

	/**
	 * @param diskQuota
	 *            the diskQuota to set
	 */
	public void setDiskQuota(int diskQuota) {
		this.diskQuota = diskQuota;
	}

	/**
	 * @return the active
	 */
	public int getActive() {
		return active;
	}

	/**
	 * @param active
	 *            the active to set
	 */
	public void setActive(int active) {
		this.active = active;
	}

	/**
	 * @return the notificationDiskFull
	 */
	public int getNotificationDiskFull() {
		return notificationDiskFull;
	}

	/**
	 * @param notificationDiskFull
	 *            the notificationDiskFull to set
	 */
	public void setNotificationDiskFull(int notificationDiskFull) {
		this.notificationDiskFull = notificationDiskFull;
	}

	/**
	 * @return the groups
	 */
	public List<Group> getGroups() {
		return groups;
	}

	/**
	 * @param groups
	 *            the groups to set
	 */
	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	/**
	 * @return the dateCreated
	 */
	public Timestamp getDateCreated() {
		return dateCreated;
	}

	/**
	 * @param dateCreated
	 *            the dateCreated to set
	 */
	public void setDateCreated(Timestamp dateCreated) {
		this.dateCreated = dateCreated;
	}

	/**
	 * @return the lastLogin
	 */
	public Timestamp getLastLogin() {
		return lastLogin;
	}

	/**
	 * @param lastLogin
	 *            the lastLogin to set
	 */
	public void setLastLogin(Timestamp lastLogin) {
		this.lastLogin = lastLogin;
	}

	/**
	 * @return the checkboxActive
	 */
	public boolean isCheckboxActive() {
		return checkboxActive;
	}

	/**
	 * @param checkboxActive the checkboxActive to set
	 */
	public void setCheckboxActive(boolean checkboxActive) {
		this.checkboxActive = checkboxActive;
	}

	/**
	 * @return the groupIds
	 */
	public List<String> getGroupIds() {
		return groupIds;
	}

	/**
	 * @param groupIds the groupIds to set
	 */
	public void setGroupIds(List<String> groupIds) {
		this.groupIds = groupIds;
	}

	/**
	 * @return the isAdmin
	 */
	public boolean isAdmin() {
		return isAdmin;
	}

	/**
	 * @param isAdmin the isAdmin to set
	 */
	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	/**
	 * @return the checkboxDiskFull
	 */
	public boolean isCheckboxDiskFull() {
		return checkboxDiskFull;
	}

	/**
	 * @param checkboxDiskFull the checkboxDiskFull to set
	 */
	public void setCheckboxDiskFull(boolean checkboxDiskFull) {
		this.checkboxDiskFull = checkboxDiskFull;
	}
}

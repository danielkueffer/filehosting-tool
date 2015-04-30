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

/**
 * The group entity
 * 
 * @author dkueffer
 * 
 */
@Entity
@Table(name = "`group`")
public class Group implements Serializable {

	private static final long serialVersionUID = -3165312403093453404L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String title;

	@Column(name = "is_admin")
	private int isAdmin;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "group_user", joinColumns = @JoinColumn(name = "group_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
	private List<User> members;

	@Column(name = "date_created")
	private Timestamp dateCreated;

	@Column(name = "last_updated")
	private Timestamp lastUpdated;

	@Column(name = "updated_user")
	private String updatedUser;

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
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the isAdmin
	 */
	public int getIsAdmin() {
		return isAdmin;
	}

	/**
	 * @param isAdmin
	 *            the isAdmin to set
	 */
	public void setIsAdmin(int isAdmin) {
		this.isAdmin = isAdmin;
	}

	/**
	 * @return the members
	 */
	public List<User> getMembers() {
		return members;
	}

	/**
	 * @param members
	 *            the members to set
	 */
	public void setMembers(List<User> members) {
		this.members = members;
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
	 * @return the lastUpdated
	 */
	public Timestamp getLastUpdated() {
		return lastUpdated;
	}

	/**
	 * @param lastUpdated
	 *            the lastUpdated to set
	 */
	public void setLastUpdated(Timestamp lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	/**
	 * @return the updatedUser
	 */
	public String getUpdatedUser() {
		return updatedUser;
	}

	/**
	 * @param updatedUser
	 *            the updatedUser to set
	 */
	public void setUpdatedUser(String updatedUser) {
		this.updatedUser = updatedUser;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Group [id=" + id + ", title=" + title + ", isAdmin=" + isAdmin
				+ ", members=" + members + ", dateCreated=" + dateCreated
				+ ", lastUpdated=" + lastUpdated + ", updatedUser="
				+ updatedUser + "]";
	}

}

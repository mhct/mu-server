package com.muframe.server;

import java.net.URL;

import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Metadata representing a Photo
 * 
 * @author mario
 *
 */
public class Photo {
	private String user;

	@Id
	private String id;
	private URL url;

	public Photo() {}
	
	public Photo(String user, String id, URL url) {
		this.user = user;
		this.id = id;
		this.url = url;
	}

	@JsonProperty
	public String getUser() {
		return user;
	}

	@JsonProperty
	public String getId() {
		return id;
	}

	@JsonProperty
	public URL getUrl() {
		return url;
	}

	@Override
	public String toString() {
		return "Photo [user=" + user + ", id=" + id + ", url=" + url + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Photo other = (Photo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}
	
	
	
	
}

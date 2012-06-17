package net.dahanne.memorama.client.model;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonProperty;

//@JsonIgnoreProperties(ignoreUnknown = true)
public class PhotoElement {

    private int id;
	private String file;
    private Date timestamp;
	private int camera;
	private int shot;
	private int likes;
	private int slider;
	
    public static final String MEMORAMA_BASE_URL = "http://www.memorama.me/";

    public int getId() {
        return id;
	}

    @JsonProperty("ID")
    public void setId(int id) {
        this.id = id;
	}
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}

    public Date getTimestamp() {
        return timestamp;
	}

    public void setTimestamp(Date timeStamp) {
        this.timestamp = timeStamp;
	}
	public int getCamera() {
		return camera;
	}
	public void setCamera(int camera) {
		this.camera = camera;
	}
	public int getShot() {
		return shot;
	}
	public void setShot(int shot) {
		this.shot = shot;
	}

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
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
        PhotoElement other = (PhotoElement) obj;
        if (id != other.id)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "PhotoElement [id=" + id + ", file=" + file + ", timestamp=" + timestamp + ", camera=" + camera + ", shot=" + shot + ", likes="
                + likes + ", slider=" + slider + "]";
    }

    public int getLikes() {
		return likes;
	}
	public void setLikes(int likes) {
		this.likes = likes;
	}
	public int getSlider() {
		return slider;
	}
	public void setSlider(int slider) {
		this.slider = slider;
	}
	public String getFullUrl() {
        return MEMORAMA_BASE_URL + "images/" + file + ".jpeg";
	}
	
	
}

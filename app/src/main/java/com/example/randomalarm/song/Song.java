package com.example.randomalarm.song;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Song {
	/**歌曲名*/
	private String name;
	/**路径*/
	private String path;
	/**所属专辑*/
	private String album;
	/**艺术家(作者)*/
	private String artist;
	/**文件大小*/
	private long size;
	/**时长*/
	private int duration;

	@Generated(hash = 744490993)
	public Song(String name, String path, String album, String artist, long size, int duration) {
		this.name = name;
		this.path = path;
		this.album = album;
		this.artist = artist;
		this.size = size;
		this.duration = duration;
	}

	@Generated(hash = 87031450)
	public Song() {
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	@Override
	public String toString() {
		return "Song{" +
				"name='" + name + '\'' +
				", path='" + path + '\'' +
				", album='" + album + '\'' +
				", artist='" + artist + '\'' +
				", size=" + size +
				", duration=" + duration +
				'}';
	}
}

package com.vaannila.domain;

import java.util.List;

import com.vaannila.dao.ComentariDAO;
import com.vaannila.dao.ComentariDAOImpl;


public class Llibre {

	private String isbn;
	private String title;
	private String subtitle;
	private String author;
	private String publisher;
	private String description;
	private String genre;
    private String series;
    private String thumb;
    private String cover;
    private List<Comentari> commentList;
    private String puntuacio;
    private String numVots;
    private String preview;
    private String year;
    private String language;
    private List<Llibre> similars;
    
	public Llibre() {
		super();
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getSeries() {
		return series;
	}

	public void setSeries(String series) {
		this.series = series;
	}

	public String getThumb() {
		return thumb;
	}

	public void setThumb(String thumb) {
		this.thumb = thumb;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	
    public List<Comentari> getCommentList() {
		return commentList;
	}

	public void setCommentList(List<Comentari> commentList) {
		this.commentList = commentList;
	}

	public String getPuntuacio() {
		return puntuacio;
	}

	public void setPuntuacio(String puntuacio) {
		this.puntuacio = puntuacio;
	}

	public String getNumVots() {
		return numVots;
	}

	public void setNumVots(String numVots) {
		this.numVots = numVots;
	}
	
	public String getPreview() {
		return preview;
	}

	public void setPreview(String preview) {
		this.preview = preview;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public List<Llibre> getSimilars() {
		return similars;
	}

	public void setSimilars(List<Llibre> similars) {
		this.similars = similars;
	}

	/**
     * Merges book info from s into p this
     * @param s Secondary- the info source
     */
    public void mergeBookInfo(Llibre s) {
    	if (this.getIsbn()==null) this.setIsbn(s.getIsbn());
    	if (this.getTitle()==null) this.setTitle(s.getTitle());
    	if (this.getSubtitle()==null) this.setSubtitle(s.getSubtitle());
    	if (this.getAuthor()==null) this.setAuthor(s.getAuthor());
    	if (this.getPublisher()==null) this.setPublisher(s.getPublisher());
    	if (this.getDescription()==null) this.setDescription(s.getDescription());
    	if (this.getGenre()==null) this.setGenre(s.getGenre());
    	if (this.getSeries()==null) this.setSeries(s.getSeries());
    	if (this.getThumb()==null) this.setThumb(s.getThumb());
    	//if (this.getCover()==null) this.setCover(s.getCover());
    	if (s.getCover()!=null) this.setCover(s.getCover());
    }
}

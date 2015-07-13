package com.sprinklr.harvester.model;

/**
 * POJO for review data content from site as well as Json file
 * 
 * @author Rohan.Pandhare date 09/07/2015
 */
public class ReviewData {

	private String authorId;
	private String mentionedDate;
	private String ratings;
	private String comment;
	private String harvesterID;

	// getter & setters
	public String getAuthorId() {
		return authorId;
	}

	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}

	public String getMentionedDate() {
		return mentionedDate;
	}

	public void setMentionedDate(String mentionedDate) {
		this.mentionedDate = mentionedDate;
	}

	public String getRatings() {
		return ratings;
	}

	public void setRatings(String ratings) {
		this.ratings = ratings;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getHarvesterID() {
		return harvesterID;
	}

	public void setHarvesterID(String harvesterID) {
		this.harvesterID = harvesterID;
	}

	// Overrided equals method for content checking
	@Override
	public boolean equals(Object obj) {
		ReviewData rd = (obj instanceof ReviewData ? (ReviewData) obj : null);
		if (rd != null && this.getAuthorId().equals(rd.getAuthorId())
				&& this.getComment().equals(rd.getComment())
				&& this.getMentionedDate().equals(rd.getMentionedDate())
				&& this.getRatings().equals(rd.getRatings())) {
			return true;
		} else {
			return false;
		}
	}

	// Overrided hashcode method to return custom hashcode
	@Override
	public int hashCode() {
		return this.getAuthorId().hashCode() + this.getComment().hashCode()
				+ this.getMentionedDate().hashCode()
				+ this.getRatings().hashCode();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "AuthorId        : " + this.getAuthorId()
				+ "\nMentionDate     : " + this.getMentionedDate()
				+ "\nOverall Ratings : " + this.getRatings()
				+ "\nComment         : " + this.getComment()
				+ "\nHarvesterID     : " + this.getHarvesterID();
	}
}
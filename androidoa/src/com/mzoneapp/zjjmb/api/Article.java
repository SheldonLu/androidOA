package com.mzoneapp.zjjmb.api;

import java.io.Serializable;

import android.os.Bundle;

public class Article implements Serializable {

	private static final long serialVersionUID = 2842941592921408125L;
	public String id;
	public String title;
	public String author;
	public String issuedate;
	public String type;
	public String desc;
	public String[] images;

	public static Article fromBundleToArticle(Bundle bd) {
		if(null == bd) return null;
		Article article = new Article();
		article.id = bd.getString("id");
		article.title = bd.getString("title");
		article.author = bd.getString("author");
		article.issuedate = bd.getString("issuedate");
		article.desc = bd.getString("desc");
		article.images = bd.getStringArray("images");
		article.type = bd.getString("type");
		return article;
	}

	public static Bundle convertArticleToBundle(Article article) {
		if(null == article) return null;
		Bundle bd = new Bundle();
		bd.putString("id", article.id);
		bd.putString("title", article.title);
		bd.putString("author", article.author);
		bd.putString("issuedate", article.issuedate);
		bd.putString("desc", article.desc);
		bd.putString("type", article.type);
		bd.putStringArray("images", article.images);
		return bd;
	}
}

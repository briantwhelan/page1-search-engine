package com.example.fbisparser;

import java.util.Objects;

public class Fbis95Structure {

	private String docno;
	private String title;
	private String text;

	public Fbis95Structure(String docno, String title, String text) {
		this.docno = docno;
		this.title = title;
		this.text = text;
	}

	public String getDocno() {
		return docno;
	}

	public String getTitle() {
		return title;
	}

	public String getText() {
		return text;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Fbis95Structure that = (Fbis95Structure) o;
		return Objects.equals(docno, that.docno) && Objects.equals(title, that.title)
				&& Objects.equals(text, that.text);
	}

	@Override
	public int hashCode() {
		return Objects.hash(docno, title, text);
	}

	@Override
	public String toString() {
		return "Current Doc: " + docno + "\n" + "Title: " + title + "\n" + "Text: " + text;
	}
}

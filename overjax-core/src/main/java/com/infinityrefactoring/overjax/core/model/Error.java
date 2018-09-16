package com.infinityrefactoring.overjax.core.model;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class Error implements Serializable {

	private static final long serialVersionUID = 5704451060745457882L;

	private UUID id;
	private String code;
	private String title;
	private String detail;
	private ErrorSource source;
	private Object meta;

	@Override
	public boolean equals(Object obj) {
		return ((obj instanceof Error) && Objects.equals(id, ((Error) obj).id));
	}

	public String getCode() {
		return code;
	}

	public String getDetail() {
		return detail;
	}

	public UUID getId() {
		return id;
	}

	public Object getMeta() {
		return meta;
	}

	public ErrorSource getSource() {
		return source;
	}

	public String getTitle() {
		return title;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public void setMeta(Object meta) {
		this.meta = meta;
	}

	public void setSource(ErrorSource source) {
		this.source = source;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		return new StringBuilder()
				.append("Error [id=").append(id)
				.append(", code=").append(code)
				.append(", title=").append(title)
				.append(", detail=").append(detail)
				.append(", source=").append(source)
				.append(", meta=").append(meta)
				.append("]").toString();
	}

}

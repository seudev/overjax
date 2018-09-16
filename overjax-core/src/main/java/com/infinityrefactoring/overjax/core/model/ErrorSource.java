package com.infinityrefactoring.overjax.core.model;

import java.io.Serializable;

public class ErrorSource implements Serializable {

	private static final long serialVersionUID = -341643486836634374L;

	private String pointer;

	public String getPointer() {
		return pointer;
	}

	public void setPointer(String pointer) {
		this.pointer = pointer;
	}

	@Override
	public String toString() {
		return "ErrorSource [pointer=" + pointer + "]";
	}

}

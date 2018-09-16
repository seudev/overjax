package com.infinityrefactoring.overjax.core.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.json.bind.annotation.JsonbTransient;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

public class ResponseWrapper implements Serializable {

	private static final long serialVersionUID = -4873934144266548013L;

	private Object data;
	private List<Error> errors;
	private Object meta;

	public static ResponseWrapper empty() {
		return new ResponseWrapper();
	}

	public <E> ResponseWrapper addAllDataElement(Collection<E> elements) {
		getDataCollection(true).addAll(elements);
		return this;
	}

	public ResponseWrapper addAllErrors(ConstraintViolationException ex) {
		return addAllErrors(ex.getConstraintViolations());
	}

	public ResponseWrapper addAllErrors(Set<ConstraintViolation<?>> constraintViolations) {
		constraintViolations.forEach(this::addError);
		return this;
	}

	public <E> ResponseWrapper addDataElement(E element) {
		getDataCollection(true).add(element);
		return this;
	}

	public ResponseWrapper addError(ConstraintViolation<?> constraintViolation) {
		Error error = toError(constraintViolation);
		getErrors(true).add(error);
		return this;
	}

	public ResponseWrapper addError(Error error) {
		getErrors(true).add(error);
		return this;
	}

	public Object getData() {
		return data;
	}

	@JsonbTransient
	@SuppressWarnings("unchecked")
	public <E> Collection<E> getDataCollection() {
		if (data instanceof Collection) {
			return (Collection<E>) data;
		}
		return null;
	}

	@JsonbTransient
	@SuppressWarnings("unchecked")
	public <E> Collection<E> getDataCollection(boolean createCollection) {
		if (data == null) {
			if (createCollection) {
				data = new ArrayList<>();
			} else {
				return null;
			}
		}
		if (data instanceof Collection) {
			return (Collection<E>) data;
		}
		throw new UnsupportedOperationException("The \"data\" instance is not a Collection.");
	}

	public List<Error> getErrors() {
		return errors;
	}

	@JsonbTransient
	public List<Error> getErrors(boolean createList) {
		if ((errors == null) && createList) {
			errors = new ArrayList<>();
		}
		return errors;
	}

	public Object getMeta() {
		return meta;
	}

	public <E> boolean removeDataElement(E element) {
		if (data != null) {
			getDataCollection(true).remove(element);
		}
		return false;
	}

	public <E> boolean removeError(Error error) {
		return ((errors != null) && errors.remove(error));
	}

	public void setData(Object data) {
		this.data = data;
	}

	public void setErrors(List<Error> errors) {
		this.errors = errors;
	}

	public void setMeta(Object meta) {
		this.meta = meta;
	}

	@Override
	public String toString() {
		return "ResponseWrapper [data=" + data + ", errors=" + errors + ", meta=" + meta + "]";
	}

	protected Error toError(ConstraintViolation<?> constraintViolation) {
		String pointer = toPointer(constraintViolation);

		ErrorSource errorSource = new ErrorSource();
		errorSource.setPointer(pointer);

		String code = constraintViolation.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName();

		Error error = new Error();
		error.setCode(code);
		error.setTitle(constraintViolation.getMessage());
		error.setSource(errorSource);
		return error;
	}

	protected String toPointer(ConstraintViolation<?> constraintViolation) {
		String source = constraintViolation.getPropertyPath().toString();
		return ("/" + source.replaceAll("[\\.\\[\\]]", "/").replaceAll("[\\/]{2,}", "/").replaceAll("\\/+$", ""));
	}

}

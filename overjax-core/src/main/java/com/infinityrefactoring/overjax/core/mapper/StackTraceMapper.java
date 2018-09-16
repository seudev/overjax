package com.infinityrefactoring.overjax.core.mapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.StatusType;

public interface StackTraceMapper {

	Response toResponse(Throwable ex, StatusType status, String exceptionDescription);

}

package com.infinityrefactoring.overjax.core.mapper;

import static com.infinityrefactoring.overjax.config.ConfigProperties.STACK_TRACE_TEMPLATE_KEY;
import static com.infinityrefactoring.overjax.config.ConfigProperties.STACK_TRACE_TEMPLATE_TYPE;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Instant;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.StatusType;
import javax.ws.rs.core.UriInfo;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.infinityrefactoring.overjax.annotation.RequestBody;
import com.infinityrefactoring.util.message.MessageInterpolator;

@RequestScoped
public class DefaultStackTraceMapper implements StackTraceMapper {

	@Context
	private UriInfo uriInfo;

	@Context
	private ResourceInfo resourceInfo;

	@Context
	private HttpHeaders httpHeaders;

	@Inject
	private HttpServletRequest request;

	@Inject
	@RequestBody
	private byte[] requestBody;

	@Inject
	@ConfigProperty(name = STACK_TRACE_TEMPLATE_KEY, defaultValue = "overjax.stack.trace.template")
	private String templateKey;

	@Inject
	@ConfigProperty(name = STACK_TRACE_TEMPLATE_TYPE, defaultValue = "text/markdown")
	private String templateType;

	@Inject
	private MessageInterpolator messageInterpolator;

	@Override
	public Response toResponse(Throwable ex, StatusType status, String exceptionDescription) {
		String message = messageInterpolator.add("httpStatusCode", status.getStatusCode())
				.add("httpStatusReason", status.getReasonPhrase())
				.add("dateOfOcurrence", Instant.now())
				.add("requestMethod", request.getMethod())
				.add("requestAbsolutePath", uriInfo.getAbsolutePath())
				.add("resourceMethod", resourceInfo.getResourceMethod())
				.add("requestHeaders", getRequestHeaders())
				.add("requestParams", getRequestParams())
				.add("requestBody", getRequestBody())
				.add("exceptionDescription", ((exceptionDescription == null) ? "" : exceptionDescription))
				.add("stackTrace", getStackTrace(ex))
				.get(templateKey);

		return Response.status(status)
				.type(templateType)
				.entity(message)
				.build();
	}

	protected String getRequestBody() {
		return ((requestBody == null) ? "" : new String(requestBody));
	}

	protected String getRequestHeaders() {
		StringBuilder builder = new StringBuilder();
		httpHeaders.getRequestHeaders().forEach((key, listValue) -> {
			listValue.forEach(value -> {
				builder.append('|').append(key).append('|').append(value).append('|');
				if (builder.length() > 0) {
					builder.append("\n");
				}
			});
		});
		return builder.toString();
	}

	protected String getRequestParams() {
		StringBuilder builder = new StringBuilder();
		request.getParameterMap().forEach((key, arrayValue) -> {
			for (String value : arrayValue) {
				builder.append('|').append(key).append('|').append(value).append('|');
				if (builder.length() > 0) {
					builder.append("\n");
				}
			}
		});
		return builder.toString();
	}

	protected String getStackTrace(Throwable ex) {
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		ex.printStackTrace(printWriter);
		return stringWriter.toString();
	}

}

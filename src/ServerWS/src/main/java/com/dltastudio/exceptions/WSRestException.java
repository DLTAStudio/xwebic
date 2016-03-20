package com.dltastudio.exceptions;

import org.json.JSONException;
import org.json.JSONObject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Exception for REST Service
 */
public class WSRestException extends WebApplicationException {

	/**
	 * Error message
	 */
	private final String errorMessage;

	/**
	 * HTTP status code
	 */
	private final Response.Status statusCode;
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 4881549621478616739L;

	/**
	 * Build JSON Content from WSException content
	 * @param errorMessage Error message
	 * @param statusCode HTTP status code
     * @return String containing JSON content
     */
	private static String JSONObjectBuilder(final String errorMessage,final int statusCode) {
		try {
			JSONObject jsonResponse = new JSONObject();
			jsonResponse.put("message",errorMessage);
			jsonResponse.put("status",statusCode);

			return new JSONObject().put("message", errorMessage).toString();
		}
		catch(JSONException e) {
			return null;
		}
	}

	public WSRestException(final String errorMessage, final Response.Status statusCode)
	{
		super(Response.status(statusCode).entity(JSONObjectBuilder(errorMessage,statusCode.getStatusCode())).type(MediaType.APPLICATION_JSON).build());
		this.errorMessage = errorMessage;
		this.statusCode = statusCode;

	}

	public WSRestException(final Response.Status statusCode) {
		super(Response.status(statusCode)
				.entity("").type(MediaType.APPLICATION_JSON).build());
		this.errorMessage="";
		this.statusCode=statusCode;
	}

	public WSRestException(WSRestException exception) {
		super(Response.status(exception.getStatusCode())
				.entity(JSONObjectBuilder(exception.getErrorMessage(),exception.getStatusCode().getStatusCode())).type(MediaType.APPLICATION_JSON).build());
		this.errorMessage=exception.getErrorMessage();
		this.statusCode=exception.getStatusCode();
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public Response.Status getStatusCode() {
		return statusCode;
	}
}
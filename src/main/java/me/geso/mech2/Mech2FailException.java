package me.geso.mech2;

import lombok.ToString;

@ToString
public class Mech2FailException extends Exception {

	private static final long serialVersionUID = 1L;

	private final Mech2Result webSimpleResult;

	public Mech2FailException(Mech2Result webSimpleResult) {
		super(String.format("%s", webSimpleResult.getResponse()
			.getStatusLine().toString()));
		this.webSimpleResult = webSimpleResult;
	}

}

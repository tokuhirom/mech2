package me.geso.mech2;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;

import lombok.ToString;

/**
 * This class contains {@link Mech2Result} and JSON type information.
 *
 * @param <T>
 */
public abstract class Mech2JSONResult<T> {

	private final Mech2Result result;

	private Mech2JSONResult(Mech2Result result) {
		this.result = result;
	}

	/**
	 * Create new instance. But you shouldn't call this directly.
	 *
	 * @param result
	 * @param klass
	 * @return
	 */
	public static <T> Mech2JSONResult<T> of(Mech2Result result,
			TypeReference<T> klass) {
		return new Mech2JSONResultTypeReference<T>(result, klass);
	}

	/**
	 * Create new instance. But you shouldn't call this directly.
	 *
	 * @param result
	 * @param klass
	 * @return
	 */
	public static <T> Mech2JSONResult<T> of(Mech2Result result,
			Class<T> klass) {
		return new Mech2JSONResultClass<T>(result, klass);
	}

	/**
	 * Get Mech2Result instance.
	 *
	 * @return
	 */
	public Mech2Result getResult() {
		return this.result;
	}

	/**
	 * Throw exception if the response doens't contains 2XX.
	 *
	 * @return
	 * @throws Mech2FailException
	 */
	public Mech2JSONResult<T> orDie() throws Mech2FailException {
		if (this.result.isSuccess()) {
			return this;
		} else {
			throw new Mech2FailException(this.result);
		}
	}

	public boolean isSuccess() {
		return this.result.isSuccess();
	}

	/**
	 * Parse JSON as object.
	 *
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	abstract public T parseJSON() throws JsonParseException,
			JsonMappingException,
			IOException;

	@ToString
	public static class Mech2JSONResultClass<T> extends
			Mech2JSONResult<T> {

		private final Class<T> klass;

		public Mech2JSONResultClass(Mech2Result result,
				Class<T> klass) {
			super(result);
			this.klass = klass;
		}

		@Override
		public T parseJSON() throws JsonParseException, JsonMappingException,
				IOException {
			return this.getResult().parseJSON(this.klass);
		}

	}

	@ToString
	public static class Mech2JSONResultTypeReference<T> extends
			Mech2JSONResult<T> {

		private final TypeReference<T> typeref;

		public Mech2JSONResultTypeReference(Mech2Result result,
				TypeReference<T> typeref) {
			super(result);
			this.typeref = typeref;
		}

		@Override
		public T parseJSON() throws JsonParseException, JsonMappingException,
				IOException {
			return this.getResult().parseJSON(this.typeref);
		}
	}

}

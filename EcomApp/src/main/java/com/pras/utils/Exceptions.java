/*
 * Copyright (C) 2025- Prasanta Paul, https://github.com/paul-prasanta
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pras.utils;

public class Exceptions {

	/**
	 * Error Codes for client side visibility and tracking
	 */
	public static enum ErrorCode {
		SYSTEM_ERROR(100), INVALID_TRANSACTION(101), INVALID_ACCOUNT(102), ACCESS_DENIED(103);
		
		int code;
		ErrorCode(int code) {
			this.code = code;
		}
		
		public int getCode() {
			return code;
		}
	}
	
	public static class OperationException extends RuntimeException {
		protected ErrorCode code;

		public OperationException() {
			super();
			this.code = ErrorCode.INVALID_TRANSACTION;
		}

		public OperationException(String message) {
			super(message);
			this.code = ErrorCode.INVALID_TRANSACTION;
		}
		
		public OperationException(String message, ErrorCode code) {
			super(message);
			this.code = code;
		}

		public ErrorCode getCode() {
			return code;
		}
	}
	
	public static class AccessException extends OperationException {
		
		public AccessException() {
			super();
			this.code = ErrorCode.ACCESS_DENIED;
		}

		public AccessException(String message) {
			super(message);
			this.code = ErrorCode.ACCESS_DENIED;
		}
	}
	
	public static class RegistrationException extends OperationException {
		
		public RegistrationException() {
			super();
			this.code = ErrorCode.INVALID_ACCOUNT;
		}

		public RegistrationException(String message) {
			super(message);
			this.code = ErrorCode.INVALID_ACCOUNT;
		}
	}
}

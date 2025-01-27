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

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.pras.utils.Exceptions.ErrorCode;
import com.pras.utils.Exceptions.OperationException;

@RestControllerAdvice
public class ExceptionHandlers {

	Logger logger = LoggerFactory.getLogger(getClass());
	
//	@ExceptionHandler
//	public ResponseEntity<Object> handleDefaultExceptions(Exception ex) {
//		return createExceptionResponse(ex, 1000, HttpStatus.INTERNAL_SERVER_ERROR);
//	}
	
	@ExceptionHandler
	public ResponseEntity<Object> handleRuntimeExceptions(RuntimeException ex) {
		return createExceptionResponse(ex, ErrorCode.SYSTEM_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler
	public ResponseEntity<Object> handleTransactionExceptions(OperationException ex) {
		return createExceptionResponse(ex, ex.getCode(), HttpStatus.BAD_REQUEST);
	}
	
	private ResponseEntity<Object> createExceptionResponse(Exception ex, ErrorCode code, HttpStatus status) {
		logger.info("Failed "+ ex.getMessage());
		//ex.printStackTrace();
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", code.getCode());
		map.put("type", ex.getClass().getCanonicalName());
		map.put("message", ex.getMessage());
		
		return new ResponseEntity<Object>(map, status);
	}
}

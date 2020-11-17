package org.mkgroup.zaga.authorizationservice.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExceptionResponseDTO {

	Date timestamp;
	String message;
	String details;
}

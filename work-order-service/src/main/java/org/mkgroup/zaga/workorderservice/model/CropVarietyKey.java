package org.mkgroup.zaga.workorderservice.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.Data;

@Data
@Embeddable
public class CropVarietyKey implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long crop_id;
	
	private Long variety_id;
	
}

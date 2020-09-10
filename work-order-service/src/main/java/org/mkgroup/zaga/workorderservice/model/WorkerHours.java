package org.mkgroup.zaga.workorderservice.model;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class WorkerHours {

	@Id
	@Column(columnDefinition = "BINARY(16)")
	private UUID id = UUID.randomUUID();
	
	private Date date;
	
	private double dayWorkPeriod;
	
	private double dayNightPeriod;
	
	private double workPeriod;
	
	private UUID operationId;
	
	private UUID workerId;
}

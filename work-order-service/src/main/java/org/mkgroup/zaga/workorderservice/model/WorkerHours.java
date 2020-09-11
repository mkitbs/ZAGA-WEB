package org.mkgroup.zaga.workorderservice.model;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Data
@Entity
public class WorkerHours {

	@Id
	@Column(columnDefinition = "BINARY(16)")
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	private UUID id;
	
	private Date date;
	
	private double dayWorkPeriod;
	
	private double dayNightPeriod;
	
	private double workPeriod;
	
	private UUID operationId;
	
	private UUID workerId;
}

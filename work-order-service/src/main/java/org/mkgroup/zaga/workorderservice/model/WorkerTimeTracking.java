package org.mkgroup.zaga.workorderservice.model;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkerTimeTracking {

	@Id
	@Column(columnDefinition = "BINARY(16)")
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	private UUID id;
	
	private Date startTime;
	
	private Date endTime;
	
	@Enumerated(EnumType.STRING)
	private TimeTrackingType type;
	
	@ManyToOne
	WorkOrderWorker workOrderWorker;

}

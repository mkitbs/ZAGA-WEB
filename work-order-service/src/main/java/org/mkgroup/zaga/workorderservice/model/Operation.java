package org.mkgroup.zaga.workorderservice.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;
import org.mkgroup.zaga.workorderservice.dto.OperationDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class Operation {
	

	@Id
	@Column(columnDefinition = "BINARY(16)")
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	private UUID id;
	
	private String kind;
	
	private boolean status;
	
	private String name;
	
	@Enumerated(EnumType.STRING)
	private OperationType type;
	
	@ManyToOne
	private OperationGroup operationGroup;
	
	@OneToMany(mappedBy = "operation")
	private List<WorkOrder> workOrders = new ArrayList<WorkOrder>();
	
	public Operation(OperationDTO op) {
		this.kind = op.getKind();
		this.name = op.getName();
	}
}

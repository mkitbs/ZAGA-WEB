package org.mkgroup.zaga.workorderservice.model;

import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;
import org.mkgroup.zaga.workorderservice.dto.OperationGroupDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "operation_group", uniqueConstraints = {@UniqueConstraint(columnNames ={"erpId"})})
public class OperationGroup {
	
	@Id
	@Column(columnDefinition = "BINARY(16)")
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	private UUID id;
	
	private String name;
	
	private Long erpId;
	
	@OneToMany(mappedBy = "operationGroup")
	private List<Operation> operations;
	
	public OperationGroup(OperationGroupDTO operation) {
		this.name = operation.getName();
		this.erpId = operation.getId();
	}

}

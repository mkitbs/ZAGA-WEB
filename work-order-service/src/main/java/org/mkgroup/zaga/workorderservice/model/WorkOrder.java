package org.mkgroup.zaga.workorderservice.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class WorkOrder {
	
	@Id
	@Column(columnDefinition = "BINARY(16)")
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	private UUID id;
	
	private Date date;
	
	private Date creationDate;
	
	private boolean closed = false;
	
	@Column(nullable = true)
	private double treated;
	
	@Column(nullable = true)
	private Long erpId;
	
	@Enumerated(EnumType.STRING)
	private WorkOrderStatus status;
	
	@Column(nullable = false)
	private Long userCreatedSapId;
	
	@Column(nullable = false)
	private Long tenantId;
	
	private boolean noOperationOutput = false;
	
	@ManyToOne
	@JoinColumn(name="responsible_id", nullable=true)
	private User responsible;
	
	@ManyToOne
	@JoinColumn(name="operation_id", nullable=true)
	private Operation operation;
	
	@ManyToOne
	@JoinColumn(name="crop_id", nullable=true)
	private Crop crop;
	
	@OneToMany(mappedBy = "workOrder", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<WorkOrderWorker> workers = new ArrayList<WorkOrderWorker>();
	
	@OneToMany(mappedBy = "workOrder", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<SpentMaterial> materials = new ArrayList<SpentMaterial>();

	@Override
    public String toString() { 
        return String.format(""); 
    } 
}

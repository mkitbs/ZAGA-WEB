package org.mkgroup.zaga.workorderservice.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.mkgroup.zaga.workorderservice.dto.OperationsTodayDTO;
import org.mkgroup.zaga.workorderservice.dto.WorkOrderForEmployeeReportDTO;
import org.mkgroup.zaga.workorderservice.dto.WorkOrderForMachineReportDTO;
import org.mkgroup.zaga.workorderservice.dto.WorkOrderForMaterialReportDTO;
import org.mkgroup.zaga.workorderservice.model.Culture;
import org.mkgroup.zaga.workorderservice.model.WorkOrder;
import org.mkgroup.zaga.workorderservice.model.WorkOrderStatus;
import org.mkgroup.zaga.workorderservice.model.WorkOrderWorker;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkOrderRepository extends JpaRepository<WorkOrder, UUID>{

	@Query(value = "SELECT * FROM work_order w WHERE w.tenant_id=?1 AND w.erp_id IS NOT NULL AND (w.org_unit='PIKB' OR w.org_unit='BIPR') ORDER BY w.creation_date ASC", nativeQuery = true)
	List<WorkOrder> findAllOrderByCreationDate(Long tenantId);
	
	@Query(value = "SELECT * FROM work_order w WHERE w.tenant_id=?1 AND w.no_operation_output=false AND w.status!='CANCELLATION' AND w.erp_id IS NOT NULL AND (w.org_unit='PIKB' OR w.org_unit='BIPR') ORDER BY w.creation_date ASC", nativeQuery = true)
	List<WorkOrder> findAllForATMReport(Long tenantId);
	
	@Query(value = "SELECT * FROM work_order w WHERE w.user_created_sap_id=?1 AND w.tenant_id=?2 AND w.erp_id IS NOT NULL ORDER BY w.creation_date ASC", nativeQuery = true)
	List<WorkOrder> findMyOrderByCreationDate(Long sapUserId, Long tenantId);
	
	@Query(value = "SELECT * FROM work_order w WHERE w.tenant_id=?1 AND w.status=?2 AND w.erp_id IS NOT NULL", nativeQuery = true)
	List<WorkOrder> findWoByStatus(Long tenantId, String status);
	
	@Query(value = "SELECT * FROM work_order w WHERE w.tenant_id=?1 AND w.user_created_sap_id=?2 AND w.status=?3 AND w.erp_id IS NOT NULL", nativeQuery = true)
	List<WorkOrder> findMyWoByStatus(Long tenantId, Long sapUserId, String status);
	
	@Query(value = "SELECT op.name AS operation, SUM(wo.treated) AS treated, ROUND(SUM(co.area-wo.treated), 2) AS area FROM work_order AS wo INNER JOIN operation AS op ON wo.operation_id=op.id INNER JOIN crop AS co ON wo.crop_id=co.id WHERE wo.tenant_id=?1 AND left(wo.date, 10)=curdate() AND co.area IS NOT NULL AND wo.treated IS NOT NULL GROUP BY wo.operation_id", nativeQuery = true)
	List<OperationsTodayDTO> findAllOperationsForToday(Long tenantId);
	
	Optional<WorkOrder> findByErpId(Long id);
	
	boolean existsByErpId(Long erpId);
	
	@Query(value = "SELECT wo.erp_id AS sapId, op.name AS atm, wo.date AS workOrderDate, SUBSTRING_INDEX(cr.name, ',', -1) AS crop, f.name AS field, CASE sm.quantity WHEN -1 THEN 'Nije uneto' ELSE sm.quantity END AS quantity, CASE sm.spent WHEN -1 THEN 'Nije uneto' ELSE sm.spent END AS spent, CASE wo.status WHEN 'NEW' THEN 'Novi' WHEN 'IN_PROGRESS' THEN 'U radu' WHEN 'CLOSED' THEN 'Zatvoren' ELSE 'Storniran' END AS woStatus FROM work_order wo INNER JOIN spent_material sm ON wo.id = sm.work_order_id INNER JOIN operation op ON wo.operation_id=op.id INNER JOIN crop cr ON wo.crop_id=cr.id INNER JOIN field f ON cr.field_id=f.id WHERE sm.material_id=?1 AND wo.tenant_id=?2 AND sm.deleted=false AND wo.status!='CANCELLATION' AND wo.erp_id IS NOT NULL", nativeQuery = true)
	List<WorkOrderForMaterialReportDTO> findAllByMaterial(UUID materialId, Long tenantId);
	
	@Query(value = "SELECT wo.erp_id as sapId, op.name as atm, wo.date workOrderDate, SUBSTRING_INDEX(cr.name, ',', -1) as crop, f.name as field, case wow.day_period when -1 then 'Nije uneto' else wow.day_period end as dayWork, case wow.night_period when -1 then 'Nije uneto' else wow.night_period end as nightWork, case wo.status when 'NEW' then 'Novi' when 'IN_PROGRESS' then 'U radu' when 'CLOSED' then 'Zatvoren' else 'Storniran' end as woStatus FROM work_order wo inner join work_order_worker wow on wo.id = wow.work_order_id inner join operation op on wo.operation_id=op.id inner join crop cr on wo.crop_id=cr.id inner join field f on cr.field_id=f.id where wow.worker_id=?1 AND wo.tenant_id=?2 AND wow.deleted = false AND wo.status!='CANCELLATION' AND wo.erp_id IS NOT NULL", nativeQuery = true)
	List<WorkOrderForEmployeeReportDTO> findAllByEmployee(UUID employeeId, Long tenantId);
	
	
	@Query(value = "SELECT wo.erp_id as sapId, op.name as atm, wo.date workOrderDate, SUBSTRING_INDEX(cr.name, ',', -1) as crop, f.name as field, worker.name as worker, case wow.sum_state when -1 then 'Nije uneto' else wow.sum_state end as machineState, case wow.fuel when -1 then 'Nije uneto' else wow.fuel end as fuel, case wo.status when 'NEW' then 'Novi' when 'IN_PROGRESS' then 'U radu' when 'CLOSED' then 'Zatvoren' else 'Storniran' end as woStatus, case when machine.name is null then 'BEZ PRIKLJUČNE MAŠINE' else machine.name end as couplingMachine, wow.machine_id as machineId FROM work_order wo left join work_order_worker wow on wo.id = wow.work_order_id left join operation op on wo.operation_id=op.id left join crop cr on wo.crop_id=cr.id left join field f on cr.field_id=f.id left join users as worker on wow.worker_id=worker.id left join machine on wow.connecting_machine_id=machine.id where wo.tenant_id=?1 and wow.deleted = false", nativeQuery = true)
	List<WorkOrderForMachineReportDTO> findAllByMachine(Long tenantId);
	
	@Query(value = "SELECT * FROM work_order wo left join work_order_worker wow on wo.id = wow.work_order_id where wo.tenant_id=?1 and wow.machine_id=?2 and wow.deleted = false", nativeQuery = true)
	List<WorkOrder> findAllByMachineId(Long tenantId, UUID machineId);
	
	
}

package org.mkgroup.zaga.workorderservice.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.mkgroup.zaga.workorderservice.dto.MachineSumFuelPerCultureDTO;
import org.mkgroup.zaga.workorderservice.dto.MachineSumStatePerCultureDTO;
import org.mkgroup.zaga.workorderservice.dto.NumOfEmployeesPerOperationDTO;
import org.mkgroup.zaga.workorderservice.dto.SpentHourOfWorkerPerCultureDTO;
import org.mkgroup.zaga.workorderservice.dto.WorkOrderTractorDriverDTO;
import org.mkgroup.zaga.workorderservice.model.WorkOrderWorker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface WorkOrderWorkerRepository extends JpaRepository<WorkOrderWorker, UUID> {

	@Query(value = "SELECT * FROM work_order_worker AS wow INNER JOIN work_order AS wo ON wow.work_order_id=wo.id WHERE wo.tenant_id=:tenantId AND wo.status!='CANCELLATION' AND wo.erp_id IS NOT NULL AND wow.deleted=false AND (wo.org_unit='PIKB' OR wo.org_unit='BIPR') ORDER BY wow.worker_id", nativeQuery = true)
	List<WorkOrderWorker> findAllByOrderByWorkerId(@Param("tenantId") Long tenantId);
	
	@Query(value = "SELECT * FROM work_order_worker AS wow INNER JOIN work_order AS wo ON wow.work_order_id=wo.id WHERE wo.tenant_id=:tenantId AND wo.status!='CANCELLATION' AND wo.erp_id IS NOT NULL AND wow.deleted=false AND (wo.org_unit='PIKB' OR wo.org_unit='BIPR') ORDER BY wow.machine_id", nativeQuery = true)
	List<WorkOrderWorker> findAllByOrderByMachineId(@Param("tenantId") Long tenantId);
	
	@Query(value = "SELECT * FROM work_order_worker w ORDER BY w.machine_id", nativeQuery = true)
	List<WorkOrderWorker> findAllByOrderByMachineId();

	@Modifying
	@Transactional
	@Query(value = "UPDATE work_order_worker SET deleted = true WHERE id = ?1", nativeQuery = true)
	void deleteWorker(UUID id);
	
	@Query(value = "SELECT * FROM work_order_worker WHERE erp_id = ?1 AND work_order_id = ?2", nativeQuery = true)
	Optional<WorkOrderWorker> findByErpIdAndWorkOrderId(int erpId, UUID workOrderId);
	
	@Query(value = "SELECT * FROM work_order_worker AS wow INNER JOIN work_order AS wo ON wow.work_order_id=wo.id WHERE wow.worker_id=?1 AND wow.deleted=false AND wo.tenant_id=:?2 AND wow.deleted=false AND (wo.org_unit='PIKB' OR wo.org_unit='BIPR')", nativeQuery = true)
	List<WorkOrderWorker> findAllWoWByWorker(UUID workerId, Long tentanId);
	
	@Query(value = "SELECT op.name AS operation, count(*) AS numOfEmployees FROM work_order_worker AS wow INNER JOIN work_order AS wo ON wow.work_order_id=wo.id INNER JOIN operation AS op ON wow.operation_id=op.id WHERE wow.deleted=false AND wo.tenant_id=:tenantId AND left(wo.date, 10)=curdate() AND (wo.org_unit='PIKB' OR wo.org_unit='BIPR') GROUP BY wow.operation_id", nativeQuery = true)
	List<NumOfEmployeesPerOperationDTO> findNumOfOperation(@Param("tenantId") Long tenantId);

	@Query(value = "SELECT SUM(wow.work_period) AS hour, c.name AS crop, u.name AS worker, cul.name AS culture FROM work_order_worker AS wow INNER JOIN work_order AS wo ON wow.work_order_id=wo.id INNER JOIN crop AS c ON wo.crop_id=c.id INNER JOIN users AS u ON wow.worker_id=u.id INNER JOIN culture AS cul ON c.culture_id=cul.id WHERE wow.work_period!=-1 AND wow.deleted=false AND wo.tenant_id=?1 AND (wo.org_unit='PIKB' OR wo.org_unit='BIPR') GROUP BY c.id, u.id ORDER BY c.name ASC", nativeQuery = true)
	List<SpentHourOfWorkerPerCultureDTO> getHourOfWorkersPerCulture(Long tenantId);
	
	@Query(value = "SELECT SUM(wow.sum_state) AS state, m.name AS machine, c.name AS crop, cul.name AS culture FROM work_order_worker AS wow INNER JOIN machine AS m ON wow.machine_id=m.id INNER JOIN work_order AS wo ON wow.work_order_id=wo.id INNER JOIN crop AS c ON wo.crop_id=c.id INNER JOIN culture AS cul ON c.culture_id=cul.id WHERE wow.sum_state!=-1 AND wow.deleted=false AND wo.tenant_id=?1 AND (wo.org_unit='PIKB' OR wo.org_unit='BIPR') GROUP BY c.id, wow.machine_id ORDER BY c.name ASC", nativeQuery = true)
	List<MachineSumStatePerCultureDTO> getMachineSumStatePerCulture(Long tenantId);
	
	@Query(value = "SELECT SUM(wow.sum_state) AS state, m.name AS machine FROM work_order_worker AS wow INNER JOIN machine AS m ON wow.machine_id=m.id INNER JOIN work_order AS wo ON wow.work_order_id=wo.id WHERE wow.sum_state!=-1 AND wow.deleted=false AND wo.tenant_id=?1 AND (wo.org_unit='PIKB' OR wo.org_unit='BIPR') GROUP BY wow.machine_id", nativeQuery = true)
	List<MachineSumStatePerCultureDTO> getMachineSumState(Long tenantId);
	
	@Query(value = "SELECT SUM(wow.fuel) AS fuel, m.name AS machine, c.name AS crop, cul.name AS culture FROM work_order_worker AS wow INNER JOIN machine AS m ON wow.machine_id=m.id INNER JOIN work_order AS wo ON wow.work_order_id=wo.id INNER JOIN crop AS c ON wo.crop_id=c.id INNER JOIN culture AS cul ON c.culture_id=cul.id WHERE wow.fuel!=-1 AND wow.deleted=false AND wo.tenant_id=?1 AND (wo.org_unit='PIKB' OR wo.org_unit='BIPR') GROUP BY c.id, wow.machine_id ORDER BY c.name ASC", nativeQuery = true)
	List<MachineSumFuelPerCultureDTO> getMachineSumFuelPerCultureDTO(Long tenantId);
	
	@Query(value = "SELECT SUM(REPLACE(wow.final_state, -1, 0)) FROM work_order_worker wow WHERE wow.work_order_id=?1 AND wow.deleted=false", nativeQuery = true)
	int sumAllFilanStates(UUID workOrderId);
}

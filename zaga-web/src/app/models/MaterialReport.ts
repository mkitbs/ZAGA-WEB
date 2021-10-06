import { Material } from './Material';
import { WorkOrder } from './WorkOrder';
import { WorkOrderForMaterialReportDTO } from './WorkOrderForMaterialReportDTO';

export class MaterialReport {
    material: Material;
    workOrders: WorkOrderForMaterialReportDTO[];
}
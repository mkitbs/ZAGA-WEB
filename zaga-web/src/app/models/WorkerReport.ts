import { Employee } from './Employee';
import { WorkOrder } from './WorkOrder';
import { WorkOrderForEmployeeReportDTO } from './WorkOrderForEmployeeReportDTO';

export class WorkerReport {
    worker: Employee;
    workOrders: WorkOrderForEmployeeReportDTO[];
    
}
import { Employee } from './Employee';
import { WorkOrder } from './WorkOrder';

export class WorkerReport {
    worker: Employee;
    workOrders: WorkOrder[];
    dayPeriodSum;
    nightPeriodSum;
    workPeriodSum;
}
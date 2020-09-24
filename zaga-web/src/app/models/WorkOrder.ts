import { Employee } from "./Employee";
import { SpentMaterial } from "./SpentMaterial";
import { WorkOrderMachine } from "./WorkOrderMachine";
import { WorkOrderWorker } from "./WorkOrderWorker";

export class WorkOrder {
  id: string;
  start;
  end;
  status;
  cropId: string;
  operationId: string;
  responsibleId: string;
  materials: SpentMaterial[] = [];
  machines: WorkOrderMachine[] = [];
  workers: WorkOrderWorker[] = [];
  assignedUsers: Employee[] = [];
  operationName;
}

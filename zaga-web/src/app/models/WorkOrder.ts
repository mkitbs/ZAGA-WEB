import { Employee } from "./Employee";
import { SpentMaterial } from "./SpentMaterial";
import { WorkOrderMachine } from "./WorkOrderMachine";
import { WorkOrderWorker } from "./WorkOrderWorker";

export class WorkOrder {
  id: string;
  area;
  date;
  status;
  cropId: string;
  tableId;
  operationId: string;
  responsibleId: string;
  materials: SpentMaterial[] = [];
  workers: WorkOrderWorker[] = [];
  operationName;
  treated;
  year;
  sapId;
  table;
  userCreatedId;
  cancellation;
  noOperationOutput;
  numOfRefOrder;
  note;
  cropName;
  responsibleName;
}

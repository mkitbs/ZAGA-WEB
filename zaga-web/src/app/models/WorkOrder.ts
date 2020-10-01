import { Employee } from "./Employee";
import { SpentMaterial } from "./SpentMaterial";
import { WorkOrderMachine } from "./WorkOrderMachine";
import { WorkOrderWorker } from "./WorkOrderWorker";

export class WorkOrder {
  id: string;
  area;
  start;
  end;
  status;
  cropId: string;
  tableId;
  cultureId;
  operationId: string;
  responsibleId: string;
  materials: SpentMaterial[] = [];
  workers: WorkOrderWorker[] = [];
  operationName;
}

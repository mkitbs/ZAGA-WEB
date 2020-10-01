import { Employee } from "./Employee";
import { Machine } from "./Machine";
import { Operation } from "./Operation";

export class WorkOrderWorker {
  id;
  user: Employee = new Employee();
  operation: Operation = new Operation();
  machine: Machine = new Machine();
  connectingMachine: Machine = new Machine();
  date;
  dayWorkPeriod;
  dayNightPeriod;
  workPeriod;
  initialState;
  finalState;
  sumState;
  fuel;
  wowObjectId;
}

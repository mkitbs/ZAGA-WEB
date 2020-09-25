import { Employee } from "./Employee";
import { Machine } from "./Machine";

export class WorkOrderMachine {
  id;
  user: Employee;
  machine: Machine;
  initialState;
  finalState;
  sumState;
  workPeriod;
  date;
}

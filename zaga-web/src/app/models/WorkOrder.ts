import { SpentMaterial } from './SpentMaterial';
import { MachineState } from './MachineState';
import { Worker } from './Worker';

export class WorkOrder{
    id:string;
    start;
    end;
    status;
    cropId:string;
    operationId:string;
    responsibleId:string;
    materials:SpentMaterial [] = [];
    machines:MachineState [] = [];
    workers: Worker [] = [];
    operationName;
}
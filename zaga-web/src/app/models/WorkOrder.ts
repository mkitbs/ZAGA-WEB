import { Material } from './Material';
import { Machine } from './Machine';
import { Employee } from './Employee';

export class WorkOrder{
    id:string;
    start;
    end;
    status;
    cropId:string;
    operationId:string;
    responsibleId:string;
    materials:Material [] = [];
    machines:Machine [] = [];
    workers: Employee [] = [];
    operationName;
}
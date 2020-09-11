import { Material } from './Material';
import { Machine } from './Machine';
import { Employee } from './Employee';

export class WorkOrder{
    id:string;
    start:Date;
    end:Date;
    status:string;
    cropId:string;
    operationId:string;
    materials:Material [] = [];
    machines:Machine [] = [];
    employees: Employee [] = [];
}
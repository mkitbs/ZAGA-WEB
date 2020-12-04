import { Roles } from './Roles';

export class User{
    id;
    name;
    surname;
    addressId;
    email;
    password;
    enabled;
    nonLocked;
    roles:Roles [];
    telephone;
}
import { Roles } from './Roles';

export class SignupRequest{
    name;
    surname;
    email;
    password;
    confirmPassword;
    roles:Roles [];
    telephone;
    dateOfBirth;
    sapUserId;
}
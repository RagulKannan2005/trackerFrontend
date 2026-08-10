import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";

@Injectable({
    providedIn:'root',
})
export class Auth{


    private http=inject(HttpClient);

    private apiurl='http://localhost:8081/api/v1/auth';

    register(user:any){
        return this.http.post(this.apiurl+'/register',user);
    }

    login(user:any){
        return this.http.post(this.apiurl+'/authenticate',user);
    }
    getuser(){
        const user=localStorage.getItem('user');
        return user?JSON.parse(user):null;
    }
    getusername(){
        return this.getuser().userName;
    }
}
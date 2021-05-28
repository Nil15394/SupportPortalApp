import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment' 
import { HttpClient, HttpErrorResponse, HttpResponse } from '@angular/common/http'
import { Observable } from 'rxjs';

import { JwtHelperService } from "@auth0/angular-jwt";
import { User } from '../Model/user';



@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  public host = environment.apiurl;
  private token!: string;
  private loggedInUser!: string;
  private jwtHelper = new JwtHelperService();

  constructor(private http : HttpClient) { }

  login(user: User): Observable<HttpResponse<User>>{
    return this.http.post<User>
              (`${this.host}/user/login`, user, {observe: 'response'});
  }

  register(user: User): Observable<User>{
    return this.http.post<User>(`${this.host}/user/register`,user);
  }

  logOut(): void{
    this.token = '';
    this.loggedInUser = '';
    localStorage.removeItem('user');
    localStorage.removeItem('token');
    localStorage.removeItem('users');
  }
  
   saveToken(token: string): void {
    this.token = token;
    localStorage.setItem('token', token);
  }

   addUserToLocalCache(user: User): void {
    localStorage.setItem('user', JSON.stringify(user));
  }

   getUserFromLocalCache(): User {
    return JSON.parse(localStorage.getItem('user')!);
  }

   loadToken(): void {
    this.token = localStorage.getItem('token')!;
  }

   getToken(): string {
    return this.token;
  }

  isUserLoggedIn(): boolean {
    this.loadToken();
    if (this.token != null && this.token !== ''){
      if (this.jwtHelper.decodeToken(this.token).sub != null || '') {
        if (!this.jwtHelper.isTokenExpired(this.token)) {
          this.loggedInUser = this.jwtHelper.decodeToken(this.token).sub;
          return true;
        }
      }
    } else {
      this.logOut();
      return false;
    }
    return false;
  }

}



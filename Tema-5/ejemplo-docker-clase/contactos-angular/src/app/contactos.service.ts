import { Injectable } from '@angular/core';
import {Contacto } from './contacto';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment } from '../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ContactosService {
  constructor(private http: HttpClient) { }

  getContactos(): Observable<Contacto []> {
    return this.http.get<Contacto []>(environment.baseURI);
  }

  addContacto(contacto: Contacto): Observable<Contacto> {
    return this.http.post<Contacto>(environment.baseURI, contacto);
  }

  editarContacto(contacto: Contacto): Observable<Contacto> {
    return this.http.put<Contacto>(environment.baseURI + '/' + contacto.id, contacto);
  }

  eliminarcContacto(id: number): Observable<HttpResponse<string>> {
    return this.http.delete(environment.baseURI + '/' + id, {observe: "response", responseType: 'text'});
  }
}

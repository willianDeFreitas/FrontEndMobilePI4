package com.example.cruddeusuarios.services;

import com.example.cruddeusuarios.dto.DtoCliente;
import com.example.cruddeusuarios.dto.DtoLogin;
import com.example.cruddeusuarios.dto.DtoUser;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiEndPoint {

    @POST("/users")
    Call<DtoUser> cadastraUsuario(@Body DtoUser dtoUser);

    @POST("/auth/login")
    Call<DtoLogin> login(@Body DtoLogin dtoLogin);

    @GET("/users")
    Call<List<DtoUser>> todosUsuarios(@Header("Authorization") String authorization);

    @GET("/clientes")
    Call<List<DtoCliente>> buscaClientes(@Header("Authorization") String authorization);

    @GET("/clientes")
    Call<List<DtoCliente>> buscaProdutos(@Header("Authorization") String authorization);

    @PUT("/users/{id}")
    Call<DtoUser> alteraUsuario(@Body DtoUser user, @Path("id") int id, @Header("Authorization") String authorization);

    @DELETE("/users/{id}")
    Call<Void> deletaUsuario(@Path("id") Integer id, @Header("Authorization") String authorization);
}

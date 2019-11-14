package com.example.frontpi4.services;

import com.example.frontpi4.dto.ClienteDTO;
import com.example.frontpi4.dto.LoginDTO;
import com.example.frontpi4.dto.ProdutoDTO;
import com.example.frontpi4.dto.UsuarioDTO;
import com.example.frontpi4.dto.VendaDTO;

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
    /*EndPoint de login*/
    @POST("/auth/login")
    Call<LoginDTO> login(@Body LoginDTO loginDTO);
    /////////////////////////

    /*EndPoints de Usuarios*/
    @POST("/usuarios")
    Call<UsuarioDTO> cadastraUsuario(@Body UsuarioDTO usuarioDTO, @Header("Authorization") String authorization);

    @GET("/usuarios")
    Call<List<UsuarioDTO>> todosUsuarios(@Header("Authorization") String authorization);

    @PUT("/usuarios/{id}")
    Call<UsuarioDTO> alteraUsuario(@Body UsuarioDTO usuario, @Path("id") int id, @Header("Authorization") String authorization);

    @DELETE("/usuarios/{id}")
    Call<Void> deletaUsuario(@Path("id") Integer id, @Header("Authorization") String authorization);
    /////////////////////////

    /*EndPoints de Clientes*/
    @GET("/clientes")
    Call<List<ClienteDTO>> buscaClientes(@Header("Authorization") String authorization);
    /////////////////////////

    /*EndPoints de Produtos*/
    @GET("/produtos")
    Call<List<ProdutoDTO>> buscaProdutos(@Header("Authorization") String authorization);
    /////////////////////////

    /*EndPoints de Pedidos de venda*/
    @POST("/vendas")
    Call<VendaDTO> cadastraPedidoDeVenda(@Body VendaDTO vendaDTO, @Header("Authorization") String authorization);
    /////////////////////////
}
package com.oesmanalie.it.angkot.networks;

import com.oesmanalie.it.angkot.models.PenumpangPosition;
import com.oesmanalie.it.angkot.models.Points;
import com.oesmanalie.it.angkot.models.PostResponse;
import com.oesmanalie.it.angkot.models.Routes;
import com.oesmanalie.it.angkot.models.Supir;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface DataService {
    @GET("/routes")
    Call<List<Points>> getLatLng( @Query(value="rute", encoded=true) String rute );
    @GET("/routesbyruteid")
    Call<List<Points>> getLatLngByRuteId( @Query(value="ruteid", encoded=true) String ruteid );
    @GET("/login")
    Call<Supir> loginRequest(@Query(value="user", encoded=true) String user, @Query(value="password", encoded=true) String password);
    @GET("/supir")
    Call<Supir> getSupirData(@Query(value="user", encoded=true) String user);
    @GET("/datarute")
    Call<Routes> getDataRute(@Query(value="rute", encoded=true) String rute);
    @FormUrlEncoded
    @POST("/postpenumpang")
    Call<PostResponse> postPenumpang(@Field("startPoint") String startPoint,
                                     @Field("destinationPoint") String destinationPoint,
                                     @Field("stopPoint") String stopPoint,
                                     @Field("namaRute") String namaRute, @Field("id") String id);
    @GET("/getpenumpang")
    Call<List<PenumpangPosition>> getPenumpang(@Query(value="supir", encoded=true) String supir);
}

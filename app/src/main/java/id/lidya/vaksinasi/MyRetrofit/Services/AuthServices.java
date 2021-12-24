package id.winata.vaksinasi.MyRetrofit.Services;

import id.winata.vaksinasi.MyRetrofit.ServerResponseObject.SpecificServerResponse.UserDataServerResponse;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface AuthServices {

    @FormUrlEncoded
    @POST("api/login")
    Call<UserDataServerResponse> login(
            @Field("email") String email,
            @Field("password") String password
    );

    @Multipart
    @POST("api/register")
    Call<UserDataServerResponse> registerImage(
            @Part("name") RequestBody name,
            @Part("username") RequestBody username,
            @Part("email") RequestBody email,
            @Part("password") RequestBody password,
            @Part("password_confirmation") RequestBody password_confirmation,
            @Part MultipartBody.Part user_pict
    );

    @FormUrlEncoded
    @POST("api/register")
    Call<UserDataServerResponse> registerNoImage(
            @Field("name") String name,
            @Field("username") String alamat,
            @Field("email") String jeniskelamin,
            @Field("password") String password,
            @Field("password_confirmation") String c_password
    );
}

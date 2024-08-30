package easy.sale.co.il.easysaleapp.dbusers.network;

import easy.sale.co.il.easysaleapp.dbusers.model.Root;
import easy.sale.co.il.easysaleapp.dbusers.model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @GET("users")
    Call<Root> getUsers(@Query("page") int page);

    @PUT("users/{id}")
    Call<User> updateUser(@Path("id") int id, @Body User user);

    @DELETE("users/{id}")
    Call<Void> deleteUser(@Path("id") int id);

    @POST("users")
    Call<User> createUser(@Body User user);
}

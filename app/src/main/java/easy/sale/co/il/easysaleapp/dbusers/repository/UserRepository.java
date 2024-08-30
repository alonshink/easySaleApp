package easy.sale.co.il.easysaleapp.dbusers.repository;

import android.content.Context;
import android.widget.Toast;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import easy.sale.co.il.easysaleapp.dbusers.model.User;
import easy.sale.co.il.easysaleapp.dbusers.model.Root;
import easy.sale.co.il.easysaleapp.dbusers.network.ApiService;
import easy.sale.co.il.easysaleapp.dbusers.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {

    private ApiService userApiService;
    private MutableLiveData<List<User>> usersLiveData;
    private List<User> allUsers;
    private int currentPage = 1;
    private Context context;

    public UserRepository(Context context) {
        userApiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        usersLiveData = new MutableLiveData<>();
        allUsers = new ArrayList<>();
        this.context = context;
    }

    public LiveData<List<User>> getUsersLiveData() {
        return usersLiveData;
    }

    public void loadUsers(int page) {
        userApiService.getUsers(page).enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {
                if (response.isSuccessful() && response.body() != null) {
                    allUsers.addAll(response.body().getData());
                    usersLiveData.setValue(allUsers);
                } else {
                    Toast.makeText(context, "Failed to load users.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Root> call, Throwable t) {
                Toast.makeText(context, "Error loading users: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                usersLiveData.setValue(null);
            }
        });
    }

    public void loadNextPage() {
        currentPage++;
        loadUsers(currentPage);
    }

    public boolean isLastPage(int totalPages) {
        return currentPage >= totalPages;
    }

    public void updateUser(User user, Callback<User> callback) {
        userApiService.updateUser(user.getId(), user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (int i = 0; i < allUsers.size(); i++) {
                        if (allUsers.get(i).getId() == user.getId()) {
                            allUsers.set(i, response.body());
                            usersLiveData.setValue(allUsers);
                            break;
                        }
                    }
                    if (callback != null) {
                        callback.onResponse(call, response);
                    }
                } else {
                    if (callback != null) {
                        callback.onFailure(call, new Throwable("Update failed"));
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                if (callback != null) {
                    callback.onFailure(call, t);
                }
            }
        });
    }

    public void deleteUser(int id) {
        userApiService.deleteUser(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    for (int i = 0; i < allUsers.size(); i++) {
                        if (allUsers.get(i).getId() == id) {
                            allUsers.remove(i);
                            usersLiveData.setValue(allUsers);
                            Toast.makeText(context, "User deleted successfully.", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
                } else {
                    Toast.makeText(context, "Failed to delete user.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Error deleting user: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void createUser(User user, Callback<User> callback) {
        userApiService.createUser(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    allUsers.add(response.body());
                    usersLiveData.setValue(allUsers);
                    if (callback != null) {
                        callback.onResponse(call, response);
                    }
                } else {
                    if (callback != null) {
                        callback.onFailure(call, new Throwable("Creation failed"));
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                if (callback != null) {
                    callback.onFailure(call, t);
                }
            }
        });
    }
}

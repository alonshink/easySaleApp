package easy.sale.co.il.easysaleapp.dbusers.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;
import easy.sale.co.il.easysaleapp.dbusers.model.User;
import easy.sale.co.il.easysaleapp.dbusers.repository.UserRepository;
import retrofit2.Callback;

public class UserViewModel extends AndroidViewModel {

    private UserRepository repository;
    private LiveData<List<User>> usersLiveData;

    public UserViewModel(Application application) {
        super(application);
        repository = new UserRepository(application);
        usersLiveData = repository.getUsersLiveData();
        repository.loadUsers(1);
    }

    public LiveData<List<User>> getUsersLiveData() {
        return usersLiveData;
    }

    public void loadNextPage() {
        repository.loadNextPage();
    }

    public void loadUsers(int page) {
        repository.loadUsers(page);
    }

    public boolean isLastPage(int totalPages) {
        return repository.isLastPage(totalPages);
    }

    public void updateUser(User user, Callback<User> callback) {
        repository.updateUser(user, callback);
    }

    public void deleteUser(int id) {
        repository.deleteUser(id);
    }

    public void createUser(User user, Callback<User> callback) {
        repository.createUser(user, callback);
    }
}

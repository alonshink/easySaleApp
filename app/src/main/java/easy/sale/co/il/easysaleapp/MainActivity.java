package easy.sale.co.il.easysaleapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import easy.sale.co.il.easysaleapp.databinding.ActivityMainBinding;
import easy.sale.co.il.easysaleapp.dbusers.adapters.UserAdapter;
import easy.sale.co.il.easysaleapp.dbusers.model.User;
import easy.sale.co.il.easysaleapp.dbusers.viewmodel.UserViewModel;
import easy.sale.co.il.easysaleapp.fragments.AddUserFragment;
import easy.sale.co.il.easysaleapp.fragments.EditUserFragment;

import java.util.List;

public class MainActivity extends AppCompatActivity implements UserAdapter.OnUserEditClickListener {

    private ActivityMainBinding binding;
    private UserViewModel userViewModel;
    private UserAdapter userAdapter;
    private LinearLayoutManager layoutManager;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int totalPages = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        layoutManager = new LinearLayoutManager(this);
        userAdapter = new UserAdapter(null, this);
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setAdapter(userAdapter);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        userViewModel.getUsersLiveData().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                if (users != null) {
                    userAdapter.updateUserList(users);
                    isLoading = false;
                    isLastPage = userViewModel.isLastPage(totalPages);
                }
            }
        });

        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if (!isLoading && !isLastPage) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0
                            && totalItemCount >= layoutManager.getItemCount()) {
                        isLoading = true;
                        userViewModel.loadNextPage();
                    }
                }
            }
        });

        binding.buttonAddUser.setOnClickListener(v -> openAddUserFragment());
    }

    @Override
    public void onUserEditClick(User user) {
        EditUserFragment editUserFragment = new EditUserFragment(user);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, editUserFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void openAddUserFragment() {
        AddUserFragment addUserFragment = new AddUserFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, addUserFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onUserDeleteClick(User user) {
        userViewModel.deleteUser(user.getId());
    }
}

package easy.sale.co.il.easysaleapp.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.bumptech.glide.Glide;
import easy.sale.co.il.easysaleapp.databinding.FragmentEditUserBinding;
import easy.sale.co.il.easysaleapp.dbusers.model.User;
import easy.sale.co.il.easysaleapp.dbusers.viewmodel.UserViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class EditUserFragment extends Fragment {

    private static final int PICK_IMAGE = 1;
    private FragmentEditUserBinding binding;
    private UserViewModel userViewModel;
    private User user;
    private Uri selectedImageUri;

    public EditUserFragment(User user) {
        this.user = user;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentEditUserBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        binding.editTextName.setText(user.getFirstName());
        binding.editTextEmail.setText(user.getEmail());
        Glide.with(this)
                .load(user.getAvatar())
                .into(binding.imageViewAvatar);

        binding.buttonChangeAvatar.setOnClickListener(v -> openImagePicker());
        binding.buttonSave.setOnClickListener(v -> saveUserDetails());
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            try {
                InputStream imageStream = requireActivity().getContentResolver().openInputStream(selectedImageUri);
                Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                binding.imageViewAvatar.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveUserDetails() {
        String newName = binding.editTextName.getText().toString();
        String newEmail = binding.editTextEmail.getText().toString();

        if (TextUtils.isEmpty(newName) || TextUtils.isEmpty(newEmail)) {
            Toast.makeText(getContext(), "Name and Email cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        user.setFirstName(newName);
        user.setEmail(newEmail);

        if (selectedImageUri != null) {
            user.setAvatar(selectedImageUri.toString());
        }

        userViewModel.updateUser(user, new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "User updated successfully", Toast.LENGTH_SHORT).show();
                    requireActivity().getSupportFragmentManager().popBackStack();
                } else {
                    Toast.makeText(getContext(), "Update failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getContext(), "Update failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

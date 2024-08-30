package easy.sale.co.il.easysaleapp.dbusers.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import easy.sale.co.il.easysaleapp.R;
import easy.sale.co.il.easysaleapp.dbusers.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> userList;
    private OnUserEditClickListener onUserEditClickListener;

    public UserAdapter(List<User> userList, OnUserEditClickListener listener) {
        this.userList = userList;
        this.onUserEditClickListener = listener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        String fullName = (user.getFirstName() != null ? user.getFirstName() : "") +
                (user.getLastName() != null ? " " + user.getLastName() : "");

        holder.userName.setText(fullName);
        holder.userEmail.setText(user.getEmail() != null ? user.getEmail() : ""); // ודא שאימייל אינו null

        Glide.with(holder.itemView.getContext())
                .load(user.getAvatar() != null ? user.getAvatar() : R.drawable.default_avatar) // שימוש באווטאר ברירת מחדל אם התמונה לא קיימת
                .into(holder.userAvatar);

        holder.editButton.setOnClickListener(v -> onUserEditClickListener.onUserEditClick(user));
        holder.deleteButton.setOnClickListener(v -> onUserEditClickListener.onUserDeleteClick(user));
    }

    @Override
    public int getItemCount() {
        return (userList != null) ? userList.size() : 0;
    }

    public void updateUserList(List<User> userList) {
        this.userList = (userList != null) ? userList : new ArrayList<>();
        notifyDataSetChanged();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {

        ImageView userAvatar;
        TextView userName;
        TextView userEmail;
        Button editButton;
        Button deleteButton;

        public UserViewHolder(View itemView) {
            super(itemView);
            userAvatar = itemView.findViewById(R.id.userAvatar);
            userName = itemView.findViewById(R.id.userName);
            userEmail = itemView.findViewById(R.id.userEmail);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }

    public interface OnUserEditClickListener {
        void onUserEditClick(User user);
        void onUserDeleteClick(User user);
    }
}

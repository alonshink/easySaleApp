package easy.sale.co.il.easysaleapp.dbusers.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;
import easy.sale.co.il.easysaleapp.dbusers.model.User;

@Dao
public interface UserDao {

    @Query("SELECT * FROM user_table ORDER BY id ASC")
    LiveData<List<User>> getAllUsers();

    @Insert
    void insertAll(List<User> users);
}

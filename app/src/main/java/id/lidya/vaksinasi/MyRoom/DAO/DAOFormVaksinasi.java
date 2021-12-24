package id.lidya.vaksinasi.MyRoom.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import id.lidya.vaksinasi.MyRoom.Entity.FormVaksinasi;

@Dao
public interface DAOFormVaksinasi {
    @Insert
    Long insertFormVaksinasi(FormVaksinasi entity);

    @Query("SELECT * FROM FormVaksinasi")
    List<FormVaksinasi> getAllFormVaksinasi();

    @Delete
    int deleteFormVaksinasiById(FormVaksinasi formVaksinasi);

    @Query("SELECT * FROM FormVaksinasi WHERE FormVaksinasi.id = :id")
    FormVaksinasi getFormVaksinasiById(Long id);

    @Update
    void updateFormVaksinasi(FormVaksinasi formVaksinasi);
}

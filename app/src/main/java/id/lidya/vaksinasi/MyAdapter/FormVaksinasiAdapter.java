package id.lidya.vaksinasi.MyAdapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import dev.shreyaspatil.MaterialDialog.AbstractDialog;
import dev.shreyaspatil.MaterialDialog.MaterialDialog;
import dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface;
import id.lidya.vaksinasi.FormUpdateActivity;
import id.lidya.vaksinasi.MyRoom.Database.MyDatabase;
import id.lidya.vaksinasi.MyRoom.Entity.FormVaksinasi;
import id.winata.vaksinasi.R;

public class FormVaksinasiAdapter extends RecyclerView.Adapter<FormVaksinasiAdapter.ViewHolder> {
    public List<FormVaksinasi> listFormVaksinasi;

    public FormVaksinasiAdapter(List<FormVaksinasi> listFormVaksinasi) {
        this.listFormVaksinasi = listFormVaksinasi;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.formvaksinasilist,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        FormVaksinasi formVaksinasi = this.listFormVaksinasi.get(position);

        String gender = "Jenis Kelamin : "+formVaksinasi.gender;
        String rating = "Rating : "+formVaksinasi.rating;
        String usia = "Usia : "+formVaksinasi.usia;
        String kebutuhan = "kebutuhan Khusus : "+formVaksinasi.kebutuhan;

        holder.fullname.setText(formVaksinasi.fullname);
        holder.username.setText(formVaksinasi.username);
        holder.email.setText(formVaksinasi.email);
        holder.kebutuhankhusus.setText(kebutuhan);
        holder.gender.setText(gender);
        holder.usia.setText(usia);
        holder.rating.setText(rating);

        try{
            holder.circleImageView.setImageBitmap(loadImageFromStorage(formVaksinasi.getImage()));
        }catch(Exception e){
            Log.e("ERROR",e.toString());
            holder.circleImageView.setImageResource(R.drawable.user_placeholder);
        }

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {
                MaterialDialog materialDialog = new MaterialDialog.Builder((AppCompatActivity) v.getContext())
                        .setTitle("PERINGATAN !")
                        .setMessage("Hapus Item Ini ?")
                        .setCancelable(false)
                        .setAnimation(R.raw.warning)
                        .setPositiveButton("OKE", new MaterialDialog.OnClickListener() {
                            @Override
                            public void onClick(dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {
                                MyDatabase myDatabase = MyDatabase.createDatabase(v.getContext());
                                myDatabase.daoFormVaksinasi().deleteFormVaksinasiById(formVaksinasi);
                                FormVaksinasiAdapter.this.listFormVaksinasi.remove(holder.getAdapterPosition());
                                FormVaksinasiAdapter.this.notifyDataSetChanged();
                                dialogInterface.dismiss();
                            }
                        })
                        .setNegativeButton("BACK", new AbstractDialog.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                dialogInterface.dismiss();
                            }
                        })
                        .build();
                materialDialog.show();
            }
        });

        holder.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), FormUpdateActivity.class);
                intent.putExtra(FormUpdateActivity.ID,formVaksinasi.id);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.listFormVaksinasi.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView fullname;
        public TextView username;
        public TextView email;
        public TextView kebutuhankhusus;
        public TextView gender;
        public TextView usia;
        public TextView rating;
        public CircleImageView circleImageView;
        public Button btnUpdate, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.fullname = itemView.findViewById(R.id.fullname);
            this.username = itemView.findViewById(R.id.username);
            this.email = itemView.findViewById(R.id.email);
            this.kebutuhankhusus = itemView.findViewById(R.id.kebutuhankhusus);
            this.gender = itemView.findViewById(R.id.gender);
            this.usia = itemView.findViewById(R.id.usia);
            this.rating = itemView.findViewById(R.id.rating_dari_user);
            this.circleImageView = itemView.findViewById(R.id.imageView);
            this.btnUpdate = itemView.findViewById(R.id.btnUpdate);
            this.btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    private Bitmap loadImageFromStorage(String path){
        Bitmap bitmap = null;

        try {
            File file = new File(path);
            bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        return bitmap;
    }
}

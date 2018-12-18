package com.example.huynhvinh.projectorderfood;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.huynhvinh.projectorderfood.DAO.LoaiMonAnDAO;

public class ThemLoaiMonAnActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnDongYThemLoaiMonAn;
    EditText edTenLoai;
    LoaiMonAnDAO loaiMonAnDAO;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_themloaimonan);

        loaiMonAnDAO = new LoaiMonAnDAO(this);

        btnDongYThemLoaiMonAn = (Button) findViewById(R.id.btnDongYThemLoaiMonAn);
        edTenLoai = (EditText) findViewById(R.id.edThemLoaiMonAn);

        btnDongYThemLoaiMonAn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String sTenLoaiMonAn = edTenLoai.getText().toString();
        if(sTenLoaiMonAn != null || sTenLoaiMonAn.equals("")){
            boolean kiemtra = loaiMonAnDAO.ThemLoaiMonAn(sTenLoaiMonAn);
            Intent iDuLieu = new Intent();
            iDuLieu.putExtra("kiemtraloaimonan",kiemtra);
            setResult(Activity.RESULT_OK,iDuLieu);
            finish();
        }else{
            Toast.makeText(this,getResources().getString(R.string.vuilongnhapdulieu),Toast.LENGTH_SHORT).show();
        }
    }
}


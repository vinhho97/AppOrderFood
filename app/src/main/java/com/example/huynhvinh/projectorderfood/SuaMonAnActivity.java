package com.example.huynhvinh.projectorderfood;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.huynhvinh.projectorderfood.CustomAdapter.AdapterHienThiLoaiMonAn;
import com.example.huynhvinh.projectorderfood.DAO.LoaiMonAnDAO;
import com.example.huynhvinh.projectorderfood.DAO.MonAnDAO;
import com.example.huynhvinh.projectorderfood.DTO.LoaiMonAnDTO;
import com.example.huynhvinh.projectorderfood.DTO.MonAnDTO;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class SuaMonAnActivity extends AppCompatActivity implements View.OnClickListener {

    public static int REQUEST_CODE_MOHINH = 123;
    Spinner spinLoaiThucDon;
    LoaiMonAnDAO loaiMonAnDAO;
    MonAnDAO monAnDAO;
    List<LoaiMonAnDTO> loaiMonAnDTOs;
    AdapterHienThiLoaiMonAn adapterHienThiLoaiMonAn;
    ImageView imHinhThucDon;
    Button btnDongYSuaMonAn, btnThoatSuaMonAn;

    EditText edTenMonAn,edGiaTien;
    int mamonan = 0;

    byte[] sDuongDanHinh;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_suamonan);

        monAnDAO = new MonAnDAO(this);
        loaiMonAnDAO = new LoaiMonAnDAO(this);
        mamonan = getIntent().getIntExtra("mamonan",0);

        spinLoaiThucDon = (Spinner) findViewById(R.id.spinLoaiMonAn);
        imHinhThucDon = (ImageView) findViewById(R.id.imHinhThucDon);
        btnDongYSuaMonAn = (Button) findViewById(R.id.btnDongYSuaMonAn);
        btnThoatSuaMonAn = (Button) findViewById(R.id.btnThoatSuaMonAn);
        edTenMonAn = (EditText) findViewById(R.id.edSuaTenMonAn);
        edGiaTien = (EditText) findViewById(R.id.edSuaGiaTien);
        HienThiSpinnerLoaiMonAn();

        if(mamonan != 0){

            MonAnDTO monAnDTO = monAnDAO.LayMonAnTheoMa(mamonan);

            edGiaTien.setText(monAnDTO.getGiaTien());
            edTenMonAn.setText(monAnDTO.getTenMonAn());
            int vitri = 0;
            for(int i =0 ; i < loaiMonAnDTOs.size();i++ )
            {
                if(loaiMonAnDTOs.get(i).getMaLoai() == monAnDTO.getMaLoai()) {
                    vitri = i;
                    break;
                }
            }
            spinLoaiThucDon.setSelection(vitri);
            Bitmap bitmap = BitmapFactory.decodeByteArray(monAnDTO.getHinhAnh(), 0, monAnDTO.getHinhAnh().length);

            imHinhThucDon.setImageBitmap(bitmap);
            sDuongDanHinh = monAnDTO.getHinhAnh();


        }

        imHinhThucDon.setOnClickListener(this);
        btnDongYSuaMonAn.setOnClickListener(this);
        btnThoatSuaMonAn.setOnClickListener(this);
    }

    private void HienThiSpinnerLoaiMonAn (){
        loaiMonAnDTOs = loaiMonAnDAO.LayDanhSachLoaiMonAn();
        adapterHienThiLoaiMonAn = new AdapterHienThiLoaiMonAn(SuaMonAnActivity.this,R.layout.custom_layout_spinloaimonan,loaiMonAnDTOs);
        spinLoaiThucDon.setAdapter(adapterHienThiLoaiMonAn);
        adapterHienThiLoaiMonAn.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.imHinhThucDon:
                Intent iMoHinh = new Intent();
                iMoHinh.setType("image/*");
                iMoHinh.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(iMoHinh, "Chọn hình món ăn"), REQUEST_CODE_MOHINH);
                ;
                break;
            case R.id.btnDongYSuaMonAn:
                int vitri = spinLoaiThucDon.getSelectedItemPosition();
                int maloai = loaiMonAnDTOs.get(vitri).getMaLoai();
                String tenmonan = edTenMonAn.getText().toString();
                String giatien = edGiaTien.getText().toString();

                if (tenmonan != null && giatien != null && !tenmonan.equals("") && !giatien.equals("")) {
                    MonAnDTO monAnDTO = new MonAnDTO();
                    monAnDTO.setMaMonAn(mamonan);
                    monAnDTO.setGiaTien(giatien);
                    monAnDTO.setMaLoai(maloai);
                    monAnDTO.setTenMonAn(tenmonan);
                    monAnDTO.setHinhAnh(sDuongDanHinh);


                    boolean kiemtra = monAnDAO.SuaMonAn(monAnDTO);
                    if (kiemtra) {
                        Toast.makeText(this, getResources().getString(R.string.suathanhcong), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, getResources().getString(R.string.loi), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(this, getResources().getString(R.string.loithemmonan), Toast.LENGTH_SHORT).show();
                }

                //Log.d("vitri",vitri + "");
                ;
                break;

            case R.id.btnThoatSuaMonAn:
                finish();
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(REQUEST_CODE_MOHINH == requestCode) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),data.getData());
                    imHinhThucDon.setImageBitmap(bitmap);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    sDuongDanHinh = byteArray;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}



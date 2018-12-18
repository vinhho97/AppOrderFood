package com.example.huynhvinh.projectorderfood;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.huynhvinh.projectorderfood.DAO.GoiMonDAO;
import com.example.huynhvinh.projectorderfood.DTO.ChiTietGoiMonDTO;

public class SoLuongActivity extends AppCompatActivity implements View.OnClickListener {
    int maban,mamonan;
    Button btnDongYThemSoLuong;
    EditText edSoLuong;
    GoiMonDAO goiMonDAO;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_themsoluong);

        btnDongYThemSoLuong = (Button) findViewById(R.id.btnDongYThemSoLuong);
        edSoLuong = (EditText) findViewById(R.id.edSoLuongMonAn);

        goiMonDAO = new GoiMonDAO(this);

        Intent intent = getIntent();
        maban = intent.getIntExtra("maban",0);
        mamonan = intent.getIntExtra("mamonan",0);

        btnDongYThemSoLuong.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(!edSoLuong.getText().toString().equals("")) {
            // láy mã gọi món tại bàn chưa thanh toán
            int magoimon = (int) goiMonDAO.LayMaGoiMonTheoMaBan(maban, "false");
            boolean kiemtra = goiMonDAO.KiemTraMonAnDaTonTai(magoimon, mamonan);
            if (kiemtra) {
                //tiến hành cập nhật món ăn đã tồn tại
                int soluongcu = goiMonDAO.LaySoLuongMonAnTheoMaGoiMon(magoimon, mamonan);

                int soluongmoi = Integer.parseInt(edSoLuong.getText().toString());

                int tongsoluong = soluongcu + soluongmoi;

                ChiTietGoiMonDTO chiTietGoiMonDTO = new ChiTietGoiMonDTO();
                chiTietGoiMonDTO.setMaGoiMon(magoimon);
                chiTietGoiMonDTO.setMaMonAn(mamonan);
                chiTietGoiMonDTO.setSoLuong(tongsoluong);

                boolean kiemtracapnhat = goiMonDAO.CapNhatSoLuong(chiTietGoiMonDTO);
                if (kiemtracapnhat) {
                    Toast.makeText(this, getResources().getString(R.string.themthanhcong), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, getResources().getString(R.string.themthatbai), Toast.LENGTH_SHORT).show();
                }

            } else {
                //thêm món ăn
                int soluonggoi = Integer.parseInt(edSoLuong.getText().toString());
                ChiTietGoiMonDTO chiTietGoiMonDTO = new ChiTietGoiMonDTO();
                chiTietGoiMonDTO.setMaGoiMon(magoimon);
                chiTietGoiMonDTO.setMaMonAn(mamonan);
                chiTietGoiMonDTO.setSoLuong(soluonggoi);

                boolean kiemtracapnhat = goiMonDAO.ThemChiTietGoiMon(chiTietGoiMonDTO);
                if (kiemtracapnhat) {
                    Toast.makeText(this, getResources().getString(R.string.themthanhcong), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, getResources().getString(R.string.themthatbai), Toast.LENGTH_SHORT).show();
                }
            }

            finish();
        }
    }
}
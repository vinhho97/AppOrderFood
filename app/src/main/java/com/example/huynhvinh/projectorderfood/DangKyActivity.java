package com.example.huynhvinh.projectorderfood;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.huynhvinh.projectorderfood.DAO.NhanVienDAO;
import com.example.huynhvinh.projectorderfood.DAO.QuyenDAO;
import com.example.huynhvinh.projectorderfood.DTO.NhanVienDTO;
import com.example.huynhvinh.projectorderfood.DTO.QuyenDTO;
import com.example.huynhvinh.projectorderfood.FragmentApp.DatePickerFragment;

import java.util.ArrayList;
import java.util.List;

public class DangKyActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {

    EditText edTenDangNhapDK, edMatKhauDK, edNgaySinhDK,edCMNDDK,edNhapLaiMatKhauDK;
    Button btnDongYDK, btnThoatDK;
    RadioGroup rgGioiTinh;
    RadioButton rdNam,rdNu;
    TextView txtTieuDeDangKy;
    String sGioiTinh;
    Spinner spinQuyen;
    NhanVienDAO nhanVienDAO;
    QuyenDAO quyenDAO;
    int manv = 0;
    int landautien = 0; // kiểm tra xem có phải người đăng ký lần đầu tiên hay không
    List<QuyenDTO> quyenDTOList;
    List<String> dataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dangky);

       AnhXa();

        btnDongYDK.setOnClickListener(this);
        btnThoatDK.setOnClickListener(this);
        edNgaySinhDK.setOnFocusChangeListener(this);

        nhanVienDAO = new NhanVienDAO(this);
        quyenDAO = new QuyenDAO(this);

        quyenDTOList = quyenDAO.LayDanhSachQuyen();
        dataAdapter = new ArrayList<String>();

        // Đưa tên quyền vào list String
        for (int i=0; i<quyenDTOList.size();i++){
            String tenquyen = quyenDTOList.get(i).getTenQuyen();
            dataAdapter.add(tenquyen);
        }



        manv = getIntent().getIntExtra("manv",0);   // để kiểm tra là đang sửa nhân viên hay thêm nhân viên
        landautien = getIntent().getIntExtra("landautien",0); // lấy giá trị lần đầu tiên để ktra từ trang đăng nhập

        if(landautien == 0){
            if(quyenDTOList.size() == 0) {
                quyenDAO.ThemQuyen("quản lý");
                quyenDAO.ThemQuyen("nhân viên");
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,dataAdapter);
            spinQuyen.setAdapter(adapter);      // Add quyền vào spiner
            adapter.notifyDataSetChanged();
        }else{
            spinQuyen.setVisibility(View.GONE);
        }

        if(manv != 0){
            txtTieuDeDangKy.setText(getResources().getString(R.string.capnhatnhanvien));
            NhanVienDTO nhanVienDTO = nhanVienDAO.LayDanhSachNhanVienTheoMa(manv);

            edTenDangNhapDK.setText(nhanVienDTO.getTENDN());
            edMatKhauDK.setText(nhanVienDTO.getMATKHAU());
            edNgaySinhDK.setText(nhanVienDTO.getNGAYSINH());
            edCMNDDK.setText(String.valueOf(nhanVienDTO.getCMND()));

            String gioitinh = nhanVienDTO.getGIOITINH();
            if(gioitinh.equals("Nam")){
                rdNam.setChecked(true);
            }else{
                rdNu.setChecked(true);
            }
        }

    }

    private void AnhXa() {
        edTenDangNhapDK = (EditText) findViewById(R.id.edTenDangNhapDK);
        edMatKhauDK = (EditText) findViewById(R.id.edMatKhauDK);
        edNgaySinhDK = (EditText) findViewById(R.id.edNgaySinhDK);
        txtTieuDeDangKy = (TextView) findViewById(R.id.txtTieuDeDangKy);
        rdNam = (RadioButton) findViewById(R.id.rdNam);
        rdNu = (RadioButton) findViewById(R.id.rdNu);
        edCMNDDK = (EditText) findViewById(R.id.edCMNDDK);
        btnDongYDK = (Button) findViewById(R.id.btnDongYDK);
        btnThoatDK = (Button) findViewById(R.id.btnThoatDK);
        rgGioiTinh = (RadioGroup) findViewById(R.id.rgGioiTinh);
        spinQuyen = (Spinner) findViewById(R.id.spinQuyen);
        edNhapLaiMatKhauDK = (EditText) findViewById(R.id.edNhapLaiMatKhauDK);
    }

    private void DongYThemNhanVien(){
        String sTenDangNhap = edTenDangNhapDK.getText().toString();
        String sMatKhau = edMatKhauDK.getText().toString();
        switch (rgGioiTinh.getCheckedRadioButtonId()){
            case R.id.rdNam:
                sGioiTinh = "Nam";
                break;
            case R.id.rdNu:
                sGioiTinh = "Nữ";
                break;
        }
        String sNgaySinh = edNgaySinhDK.getText().toString();

        String sNhapLaiMatKhau = edNhapLaiMatKhauDK.getText().toString();           // kiểm tra việc nhập lại mật khẩu
        if(sTenDangNhap==null || sTenDangNhap.equals("") || sMatKhau.equals("") || sMatKhau == null || sNgaySinh.equals("") || sNgaySinh == null || edCMNDDK.getText().toString().equals("") || edCMNDDK.getText().toString() == null )
        {
            Toast.makeText(this,getResources().getString(R.string.vuilongkhongduocdetrong), Toast.LENGTH_SHORT).show();
        }
        else
        {
            int sCMND = Integer.parseInt(edCMNDDK.getText().toString());    // vì giá trị rỗng không parse về int đc

            if(sNhapLaiMatKhau.equals(sMatKhau))
            {
                NhanVienDTO nhanVienDTO = new NhanVienDTO();
                nhanVienDTO.setTENDN(sTenDangNhap);
                nhanVienDTO.setMATKHAU(sMatKhau);
                nhanVienDTO.setCMND(sCMND);
                nhanVienDTO.setNGAYSINH(sNgaySinh);
                nhanVienDTO.setGIOITINH(sGioiTinh);
                if(landautien != 0){
                    //gán mặc định quyền nhân viên là admin
                    nhanVienDTO.setMAQUYEN(1);
                }else{
                    //gán quyền bằng quyền mà admin chọn khi tạo nhân viên
                    int vitri = spinQuyen.getSelectedItemPosition();
                    int maquyen = quyenDTOList.get(vitri).getMaQuyen();
                    nhanVienDTO.setMAQUYEN(maquyen);
                }


                long kiemtra = nhanVienDAO.ThemNhanVien(nhanVienDTO);
                if(kiemtra != 0){
                    Toast.makeText(DangKyActivity.this,getResources().getString(R.string.themthanhcong), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(DangKyActivity.this,getResources().getString(R.string.themthatbai), Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(this, getResources().getString(R.string.nhaplaimatkhaukhongchinhxac), Toast.LENGTH_SHORT).show();
            }
        }


    }

    private void SuaNhanVien(){
        String sTenDangNhap = edTenDangNhapDK.getText().toString();
        String sMatKhau = edMatKhauDK.getText().toString();
        String sNgaySinh = edNgaySinhDK.getText().toString();
        int sCMND = Integer.parseInt(edCMNDDK.getText().toString());
        switch (rgGioiTinh.getCheckedRadioButtonId()){
            case R.id.rdNam:
                sGioiTinh = "Nam";
                break;

            case R.id.rdNu:
                sGioiTinh = "Nữ";
                break;
        }

        NhanVienDTO nhanVienDTO = new NhanVienDTO();
        nhanVienDTO.setMANV(manv);
        nhanVienDTO.setTENDN(sTenDangNhap);
        nhanVienDTO.setMATKHAU(sMatKhau);
        nhanVienDTO.setCMND(sCMND);
        nhanVienDTO.setNGAYSINH(sNgaySinh);
        nhanVienDTO.setGIOITINH(sGioiTinh);


        boolean kiemtra = nhanVienDAO.SuaNhanVien(nhanVienDTO);
        if(kiemtra){
            Toast.makeText(DangKyActivity.this,getResources().getString(R.string.suathanhcong),Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(DangKyActivity.this,getResources().getString(R.string.loi),Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnDongYDK:
                if (manv != 0) {
                    // Thực hiện code sữa nhân viên
                    SuaNhanVien();
                } else {
                    // Thực hiện thêm mới nhân viên
                    DongYThemNhanVien();
                }
                break;

            case R.id.btnThoatDK:
                if(landautien==0) {
                    Intent intent = new Intent(DangKyActivity.this, TrangChuActivity.class);
                    startActivity(intent);
                }
                else
                {
                    Intent intent = new Intent(DangKyActivity.this, DangNhapActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        int id = v.getId();
        switch (id){
            case R.id.edNgaySinhDK:
                if(hasFocus){
                    DatePickerFragment datePickerFragment = new DatePickerFragment();
                    datePickerFragment.show(getSupportFragmentManager(),"Ngày Sinh");
                    //xuat popup ngaysinh
                }
                break;
        }
    }

}

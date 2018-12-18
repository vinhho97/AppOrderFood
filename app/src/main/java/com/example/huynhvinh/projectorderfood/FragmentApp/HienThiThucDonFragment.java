package com.example.huynhvinh.projectorderfood.FragmentApp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.huynhvinh.projectorderfood.CustomAdapter.AdapterHienThiDanhSachLoaiMonAn;
import com.example.huynhvinh.projectorderfood.CustomAdapter.Interface.LayHinhMonAnInterface;
import com.example.huynhvinh.projectorderfood.DAO.LoaiMonAnDAO;
import com.example.huynhvinh.projectorderfood.DTO.LoaiMonAnDTO;
import com.example.huynhvinh.projectorderfood.R;
import com.example.huynhvinh.projectorderfood.ThemThucDonActivity;
import com.example.huynhvinh.projectorderfood.TrangChuActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class HienThiThucDonFragment extends Fragment {

    GridView gridView;
    List<LoaiMonAnDTO> loaiMonAnDTOs;
    LoaiMonAnDAO loaiMonAnDAO;
    FragmentManager fragmentManager;
    int maban;
    int maquyen;
    SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_hienthithucdon, container, false);
        setHasOptionsMenu(true);

        ((TrangChuActivity) getActivity()).getSupportActionBar().setTitle(R.string.thucdon); // doi ten

        gridView = (GridView) view.findViewById(R.id.gvHienThiThucDon);

        fragmentManager = getActivity().getSupportFragmentManager();

        loaiMonAnDAO = new LoaiMonAnDAO(getActivity());
        loaiMonAnDTOs = loaiMonAnDAO.LayDanhSachLoaiMonAn();


        // Lấy quyền
        sharedPreferences = getActivity().getSharedPreferences("luuquyen", Context.MODE_PRIVATE);
        maquyen = sharedPreferences.getInt("maquyen", 0);

        // show ra toàn bộ danh sách loại món ăn
        final AdapterHienThiDanhSachLoaiMonAn adapdater = new AdapterHienThiDanhSachLoaiMonAn(getActivity(), R.layout.custom_layout_hienthiloaimonan, loaiMonAnDTOs);
        gridView.setAdapter(adapdater);
        adapdater.notifyDataSetChanged();


        Bundle bDuLieuThucDon = getArguments();
        if (bDuLieuThucDon != null) {
            maban = bDuLieuThucDon.getInt("maban");
        }

        // bắt sự kiện click vào từng item của gridview
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int maloai = loaiMonAnDTOs.get(position).getMaLoai();

                HienThiDanhSachMonAnFragment hienThiDanhSachMonAnFragment = new HienThiDanhSachMonAnFragment();

                Bundle bundle = new Bundle();
                bundle.putInt("maloai", maloai);
                bundle.putInt("maban", maban);
                hienThiDanhSachMonAnFragment.setArguments(bundle);

                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.content, hienThiDanhSachMonAnFragment).addToBackStack("hienthiloai");

                transaction.commit();
            }
        });

        //dăng ký context menu
        registerForContextMenu(gridView);
        return view;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.edit_context_dongbomenu,menu);
    }

    // Dùng để sửa đồng bộ món ăn
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();
        // Khởi tạo menuInfo để lấy vị trí
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int vitri = menuInfo.position;
        switch (id)
        {
            case R.id.itSuaDongBo:
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                HienThiSuaDanhSachMonAnFragment hienThiDanhSachMonAnSuaFragment = new HienThiSuaDanhSachMonAnFragment();
                int MaLoai = loaiMonAnDTOs.get(vitri).getMaLoai();
                Bundle bundle = new Bundle();
                bundle.putInt("maloai",MaLoai);
                hienThiDanhSachMonAnSuaFragment.setArguments(bundle);
                transaction.replace(R.id.content,hienThiDanhSachMonAnSuaFragment).addToBackStack("hienthisuadanhsachmonan");
                transaction.commit();
                break;
        }

        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (maquyen == 1) {
        MenuItem itThemBanAn = menu.add(1, R.id.itThemThucDon, 1, R.string.themthucdon);
        itThemBanAn.setIcon(R.drawable.logodangnhap);
        itThemBanAn.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.itThemThucDon:
                Intent iThemThucDon = new Intent(getActivity(), ThemThucDonActivity.class);
                startActivity(iThemThucDon);
                getActivity().overridePendingTransition(R.anim.hieuung_activity_vao,R.anim.hieuung_activity_ra);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
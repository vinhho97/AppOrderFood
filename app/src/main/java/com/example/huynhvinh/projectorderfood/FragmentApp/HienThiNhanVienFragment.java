package com.example.huynhvinh.projectorderfood.FragmentApp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.huynhvinh.projectorderfood.CustomAdapter.AdapterHienThiNhanVien;
import com.example.huynhvinh.projectorderfood.DAO.NhanVienDAO;
import com.example.huynhvinh.projectorderfood.DTO.NhanVienDTO;
import com.example.huynhvinh.projectorderfood.DangKyActivity;
import com.example.huynhvinh.projectorderfood.R;

import java.util.List;

public class HienThiNhanVienFragment extends Fragment {
    ListView listNhanVien;
    NhanVienDAO nhanVienDAO;
    List<NhanVienDTO> nhanVienDTOList;
    SharedPreferences sharedPreferences;
    int maquyen=0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_hienthinhanvien,container,false);
        setHasOptionsMenu(true);

        listNhanVien = (ListView) view.findViewById(R.id.listViewNhanVien);

        nhanVienDAO = new NhanVienDAO(getActivity());

        HienThiDanhSachNhanVien();

        sharedPreferences = getActivity().getSharedPreferences("luuquyen", Context.MODE_PRIVATE);
        maquyen = sharedPreferences.getInt("maquyen",0);

        if(maquyen == 1 ){
            registerForContextMenu(listNhanVien);   // Đăng ký sự kiện nay cho ListView. Để khi ta nhấn giữ vào 1 item thì nó sẽ hiện ra 1 cái menu
        }


        return view;
    }

    private void HienThiDanhSachNhanVien(){
        nhanVienDTOList = nhanVienDAO.LayDanhSachNhanVien();
        AdapterHienThiNhanVien adapterHienThiNhanVien = new AdapterHienThiNhanVien(getActivity(),R.layout.custom_layout_hienthinhanvien,nhanVienDTOList);
        listNhanVien.setAdapter(adapterHienThiNhanVien);
        adapterHienThiNhanVien.notifyDataSetChanged();
    }

    // Khởi tạo contextMenu
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.edit_context_menu,menu);
    }

    // Lắng nghe sự kiện click
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int vitri = menuInfo.position;
        int manhanvien = nhanVienDTOList.get(vitri).getMANV();

        switch (id){
            case R.id.itSua:
                Intent iDangKy = new Intent(getActivity(),DangKyActivity.class);
                iDangKy.putExtra("manv",manhanvien);
                startActivity(iDangKy);
                ;break;

            case R.id.itXoa:
                boolean kiemtra = nhanVienDAO.XoaNhanVien(manhanvien);
                if (kiemtra){
                    HienThiDanhSachNhanVien();
                    Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.xoathanhcong),Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.loi),Toast.LENGTH_SHORT).show();
                }
                ;break;
        }
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if(maquyen==1) {
            MenuItem itThemBanAn = menu.add(1, R.id.itThemNhanVien, 1, R.string.themnhanvien);
            itThemBanAn.setIcon(R.drawable.nhanvien);
            itThemBanAn.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.itThemNhanVien:
                Intent iDangKy = new Intent(getActivity(), DangKyActivity.class);
                startActivity(iDangKy);
                break;
        }
        return true;
    }
}
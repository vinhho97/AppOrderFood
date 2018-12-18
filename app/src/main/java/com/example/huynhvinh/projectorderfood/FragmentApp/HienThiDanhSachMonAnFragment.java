package com.example.huynhvinh.projectorderfood.FragmentApp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.huynhvinh.projectorderfood.CustomAdapter.AdapterHienThiDanhSachMonAnTheoLoai;
import com.example.huynhvinh.projectorderfood.DAO.MonAnDAO;
import com.example.huynhvinh.projectorderfood.DTO.MonAnDTO;
import com.example.huynhvinh.projectorderfood.R;
import com.example.huynhvinh.projectorderfood.SoLuongActivity;
import com.example.huynhvinh.projectorderfood.SuaMonAnActivity;

import java.util.List;

public class HienThiDanhSachMonAnFragment extends Fragment {
    GridView gridView;
    MonAnDAO monAnDAO;
    List<MonAnDTO> monAnDTOList;
    AdapterHienThiDanhSachMonAnTheoLoai adapterHienThiDanhSachMonAnTheoLoai;
    int maban;
    int maloai;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_hienthithucdon, container, false);
        gridView = (GridView) view.findViewById(R.id.gvHienThiThucDon);

        monAnDAO = new MonAnDAO(getActivity());

        Bundle bundle = getArguments();
        if(bundle !=  null) {
            maloai = bundle.getInt("maloai");
            maban = bundle.getInt("maban");

            HienThiDanhSachMonAn();

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if(maban !=0 ) {
                        Intent iSoLuong = new Intent(getActivity(), SoLuongActivity.class);
                        iSoLuong.putExtra("maban", maban);
                        iSoLuong.putExtra("mamonan", monAnDTOList.get(i).getMaMonAn());
                        startActivity(iSoLuong);
                    }
                }
            });

        }
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN){
                    getFragmentManager().popBackStack("hienthiloai", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
                return false;
            }
        });
        registerForContextMenu(gridView);
        return view;
    }

    private void HienThiDanhSachMonAn(){
        monAnDTOList = monAnDAO.LayDanhSachMonAnTheoLoai(maloai);
        adapterHienThiDanhSachMonAnTheoLoai = new AdapterHienThiDanhSachMonAnTheoLoai(getActivity(), R.layout.custom_layout_hienthidanhsachmonan, monAnDTOList);
        gridView.setAdapter(adapterHienThiDanhSachMonAnTheoLoai);
        adapterHienThiDanhSachMonAnTheoLoai.notifyDataSetChanged();
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.edit_context_menu,menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int vitri = menuInfo.position;
        int mamonan = monAnDTOList.get(vitri).getMaMonAn();

        switch (id){
            case R.id.itSua:
                Intent iSuaMon = new Intent(getActivity(),SuaMonAnActivity.class);
                iSuaMon.putExtra("mamonan",mamonan);
                startActivity(iSuaMon);
                ;break;

            case R.id.itXoa:
                boolean kiemtra = monAnDAO.XoaMonAn(mamonan);
                if (kiemtra){
                    HienThiDanhSachMonAn();
                    Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.xoathanhcong),Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.loi),Toast.LENGTH_SHORT).show();
                }
                ;break;
        }
        return true;
    }
}
package com.example.huynhvinh.projectorderfood.FragmentApp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.huynhvinh.projectorderfood.CustomAdapter.AdapterSuaDanhSachMonAn;
import com.example.huynhvinh.projectorderfood.DAO.MonAnDAO;
import com.example.huynhvinh.projectorderfood.DTO.MonAnDTO;
import com.example.huynhvinh.projectorderfood.R;
import com.example.huynhvinh.projectorderfood.TrangChuActivity;

import java.util.List;

public class HienThiSuaDanhSachMonAnFragment extends Fragment implements View.OnClickListener {
    ListView listMonAnSua;
    MonAnDAO monAnDAO;
    List<MonAnDTO> monAnDTOList;
    int MaLoai;
    Button btnDongY;
    Button btnThoat;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_sua_dongbomonan,container,false);

        ((TrangChuActivity) getActivity()).getSupportActionBar().setTitle(R.string.danhsachmonan); // doi ten
        listMonAnSua = (ListView) view.findViewById(R.id.listsua_danhsachmonan);
        btnDongY = (Button) view.findViewById(R.id.btnDongY);
        btnThoat = (Button) view.findViewById(R.id.btnThoat);
        monAnDAO = new MonAnDAO(getActivity());

        Bundle bundle = getArguments();
        if(bundle != null) {
            // l?y mã lo?i d? th?c hi?n câu truy v?n
            MaLoai = bundle.getInt("maloai");
            HienThiDanhSachMonAn();
        }

        btnDongY.setOnClickListener(this);
        btnThoat.setOnClickListener(this);
        return view;

    }

    private  void HienThiDanhSachMonAn(){
        monAnDTOList = monAnDAO.LayDanhSachMonAnTheoLoai(MaLoai);
        AdapterSuaDanhSachMonAn adapterHienThiDanhSachMonAnSua = new AdapterSuaDanhSachMonAn(getActivity(), R.layout.custom_layout_sualistmonan,monAnDTOList);
        //  Log.d("loai",MaLoai + "");
        listMonAnSua.setAdapter(adapterHienThiDanhSachMonAnSua);
        adapterHienThiDanhSachMonAnSua.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btnDongY:
                //Toast.makeText(getActivity(), getResources().getString(R.string.dongy), Toast.LENGTH_SHORT).show();
                boolean kiemtra = false;
                for(int i=0;i<monAnDTOList.size();i++){
                    View v  = listMonAnSua.getChildAt(i);
                    EditText editText=v.findViewById(R.id.edSuaGiaTien);
                    String giatien=editText.getText().toString();
                    if(!giatien.equals(""))
                    {
                        monAnDTOList.get(i).setGiaTien(giatien);
                    }

                    kiemtra = monAnDAO.SuaMonAn(monAnDTOList.get(i));
                    if(!kiemtra)
                    {
                        break;
                    }
                }
                if(kiemtra) {
                    Toast.makeText(getActivity(), "thành công", Toast.LENGTH_SHORT).show();
                    HienThiDanhSachMonAn();
                }
                else
                    Toast.makeText(getActivity(),"thất bại",Toast.LENGTH_SHORT).show();

                ;break;


            case R.id.btnThoat:
                getFragmentManager().popBackStack("hienthisuadanhsachmonan", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                ;break;
        }

    }
}

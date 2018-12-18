package com.example.huynhvinh.projectorderfood.CustomAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.huynhvinh.projectorderfood.DTO.NhanVienDTO;
import com.example.huynhvinh.projectorderfood.R;

import java.util.List;

public class AdapterHienThiNhanVien extends BaseAdapter {

    Context context;
    int layout;
    List<NhanVienDTO> nhanVienDTOList;
    ViewHolderNhanVien viewHolderNhanVien;

    public AdapterHienThiNhanVien(Context context, int layout,List<NhanVienDTO> nhanVienDTOList){
        this.context = context;
        this.layout = layout;
        this.nhanVienDTOList = nhanVienDTOList;
    }

    @Override
    public int getCount() {
        return nhanVienDTOList.size();
    }

    @Override
    public Object getItem(int i) {
        return nhanVienDTOList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return nhanVienDTOList.get(i).getMANV();
    }

    public class ViewHolderNhanVien{
        ImageView imHinhNhanVien;
        TextView txtTenNhanVien,txtCMND;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;
        if(v == null){
            viewHolderNhanVien = new ViewHolderNhanVien();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(layout,viewGroup,false);

            viewHolderNhanVien.imHinhNhanVien = (ImageView) v.findViewById(R.id.imHinhNhanVien);
            viewHolderNhanVien.txtTenNhanVien = (TextView) v.findViewById(R.id.txtTenNhanVien);
            viewHolderNhanVien.txtCMND = (TextView) v.findViewById(R.id.txtCMND);

            v.setTag(viewHolderNhanVien);
        }else{
            viewHolderNhanVien = (ViewHolderNhanVien) v.getTag();
        }

        NhanVienDTO nhanVienDTO = nhanVienDTOList.get(i);

        viewHolderNhanVien.txtTenNhanVien.setText(nhanVienDTO.getTENDN());
        viewHolderNhanVien.txtCMND.setText(String.valueOf(nhanVienDTO.getCMND()));
        return v;
    }
}


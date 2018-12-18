package com.example.huynhvinh.projectorderfood.CustomAdapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.huynhvinh.projectorderfood.DTO.MonAnDTO;
import com.example.huynhvinh.projectorderfood.R;

import java.util.List;

public class AdapterHienThiDanhSachMonAnTheoLoai extends BaseAdapter {

    Context context;
    int layout;
    List<MonAnDTO> monAnDTOList;
    ViewHolderHienThiDanhSachMonAn viewHolderHienThiDanhSachMonAn;

    public AdapterHienThiDanhSachMonAnTheoLoai(Context context, int layout, List<MonAnDTO> monAnDTOList){
        this.context = context;
        this.layout = layout;
        this.monAnDTOList = monAnDTOList;
    }

    @Override
    public int getCount() {
        return monAnDTOList.size();
    }

    @Override
    public Object getItem(int i) {
        return monAnDTOList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return monAnDTOList.get(i).getMaMonAn();
    }

    public class ViewHolderHienThiDanhSachMonAn{
        ImageView imHinhMonAn;
        TextView txtTenMonAn,txtGiaTien;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;
        if(v == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            viewHolderHienThiDanhSachMonAn = new ViewHolderHienThiDanhSachMonAn();
            v = inflater.inflate(layout,viewGroup,false);

            viewHolderHienThiDanhSachMonAn.imHinhMonAn = (ImageView) v.findViewById(R.id.imHienThiDSMonAn);
            viewHolderHienThiDanhSachMonAn.txtTenMonAn = (TextView) v.findViewById(R.id.txtTenDSMonAn);
            viewHolderHienThiDanhSachMonAn.txtGiaTien = (TextView) v.findViewById(R.id.txtGiaTienDSMonAn);

            v.setTag(viewHolderHienThiDanhSachMonAn);

        }else{
            viewHolderHienThiDanhSachMonAn = (ViewHolderHienThiDanhSachMonAn) v.getTag();
        }

        MonAnDTO monAnDTO = monAnDTOList.get(i);
        byte[] hinhanh = monAnDTO.getHinhAnh();
        if(hinhanh == null || hinhanh.equals("")){
            viewHolderHienThiDanhSachMonAn.imHinhMonAn.setImageResource(R.drawable.backgroundheader1);
        }else{
            Bitmap bitmap = BitmapFactory.decodeByteArray(hinhanh, 0, hinhanh.length);

            viewHolderHienThiDanhSachMonAn.imHinhMonAn.setImageBitmap(bitmap);
        }

        viewHolderHienThiDanhSachMonAn.txtTenMonAn.setText(monAnDTO.getTenMonAn());
        viewHolderHienThiDanhSachMonAn.txtGiaTien.setText(context.getResources().getString(R.string.gia) + monAnDTO.getGiaTien());

        return v;
    }
}

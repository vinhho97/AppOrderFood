package com.example.huynhvinh.projectorderfood.CustomAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.huynhvinh.projectorderfood.DTO.ThanhToanDTO;
import com.example.huynhvinh.projectorderfood.R;

import java.util.List;

public class AdapterHienThiThanhToan extends BaseAdapter {

    Context context;
    int layout;
    List<ThanhToanDTO> thanhToanDTOs;
    ViewHolderThanhToan viewHolderThanhToan;

    public AdapterHienThiThanhToan(Context context, int layout, List<ThanhToanDTO> thanhToanDTOs){
        this.context = context;
        this.layout = layout;
        this.thanhToanDTOs = thanhToanDTOs;
    }

    @Override
    public int getCount() {
        return thanhToanDTOs.size();
    }

    @Override
    public Object getItem(int i) {
        return thanhToanDTOs.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public class ViewHolderThanhToan{
        TextView txtTenMonAn,txtSoLuong,txtGiaTien;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;
        if(v == null){
            viewHolderThanhToan = new ViewHolderThanhToan();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(layout,viewGroup,false);

            viewHolderThanhToan.txtTenMonAn = (TextView) v.findViewById(R.id.txtTenMonAnThanhToan);
            viewHolderThanhToan.txtGiaTien = (TextView) v.findViewById(R.id.txtGiaTienThanhToan);
            viewHolderThanhToan.txtSoLuong = (TextView) v.findViewById(R.id.txtSoLuongThanhToan);

            v.setTag(viewHolderThanhToan);
        }else{
            viewHolderThanhToan = (ViewHolderThanhToan) v.getTag();
        }

        ThanhToanDTO thanhToanDTO = thanhToanDTOs.get(i);

        viewHolderThanhToan.txtTenMonAn.setText(thanhToanDTO.getTenMonAn());
        viewHolderThanhToan.txtSoLuong.setText(String.valueOf(thanhToanDTO.getSoLuong()));
        viewHolderThanhToan.txtGiaTien.setText(String.valueOf(thanhToanDTO.getGiaTien()));

        return v;
    }
}

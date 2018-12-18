package com.example.huynhvinh.projectorderfood.CustomAdapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.huynhvinh.projectorderfood.DTO.MonAnDTO;
import com.example.huynhvinh.projectorderfood.R;

import java.util.List;

public class AdapterSuaDanhSachMonAn extends BaseAdapter {

    Context context;
    int layout;
    List<MonAnDTO> monAnDTOList;
    ViewHolderHienThiDanhSachMonAnSua viewHolderHienThiDanhSachMonAnSua;

    public  AdapterSuaDanhSachMonAn(Context context, int layout, List<MonAnDTO> monAnDTOList)
    {
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

    public class ViewHolderHienThiDanhSachMonAnSua{
        ImageView imgHinhMonAnSua;
        TextView txtTenMonAnSua;
        EditText txtGiaTienSua;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;
        if(v == null)
        {
            viewHolderHienThiDanhSachMonAnSua = new ViewHolderHienThiDanhSachMonAnSua();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.custom_layout_sualistmonan,viewGroup,false);

            viewHolderHienThiDanhSachMonAnSua.imgHinhMonAnSua = (ImageView) v.findViewById(R.id.imgHinhSuaMonAn);
            viewHolderHienThiDanhSachMonAnSua.txtTenMonAnSua = (TextView) v.findViewById(R.id.txtTenSuaMonAn);
            viewHolderHienThiDanhSachMonAnSua.txtGiaTienSua = (EditText) v.findViewById(R.id.edSuaGiaTien);

            v.setTag(viewHolderHienThiDanhSachMonAnSua);

        }
        else
        {
            viewHolderHienThiDanhSachMonAnSua  = (ViewHolderHienThiDanhSachMonAnSua) v.getTag();
        }

        MonAnDTO monAnDTO = monAnDTOList.get(i);

        String hinhanh = monAnDTO.getHinhAnh().toString();
        Log.d("hinhanh",hinhanh);
        if(hinhanh == null || hinhanh.equals(""))
        {
            viewHolderHienThiDanhSachMonAnSua.imgHinhMonAnSua.setImageResource(R.drawable.backgroundheader1);

        }
        else
        {
            Uri uri = Uri.parse(monAnDTO.getHinhAnh().toString());
            viewHolderHienThiDanhSachMonAnSua.imgHinhMonAnSua.setImageURI(uri);
        }
        viewHolderHienThiDanhSachMonAnSua.txtTenMonAnSua.setText(monAnDTO.getTenMonAn());
        viewHolderHienThiDanhSachMonAnSua.txtGiaTienSua.setText(monAnDTO.getGiaTien());

        return v;
    }


}


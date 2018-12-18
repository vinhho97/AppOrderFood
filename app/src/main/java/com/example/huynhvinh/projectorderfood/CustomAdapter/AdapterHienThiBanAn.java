package com.example.huynhvinh.projectorderfood.CustomAdapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.huynhvinh.projectorderfood.DAO.BanAnDAO;
import com.example.huynhvinh.projectorderfood.DAO.GoiMonDAO;
import com.example.huynhvinh.projectorderfood.DTO.BanAnDTO;
import com.example.huynhvinh.projectorderfood.DTO.GoiMonDTO;
import com.example.huynhvinh.projectorderfood.FragmentApp.HienThiThucDonFragment;
import com.example.huynhvinh.projectorderfood.R;
import com.example.huynhvinh.projectorderfood.ThanhToanActivity;
import com.example.huynhvinh.projectorderfood.TrangChuActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class AdapterHienThiBanAn extends BaseAdapter implements View.OnClickListener {

    Context context;
    int layout;
    List<BanAnDTO> banAnDTOList;
    ViewHolderBanAn viewHolderBanAn;
    BanAnDAO banAnDAO;
    GoiMonDAO goiMonDAO;
    FragmentManager fragmentManager;

    public AdapterHienThiBanAn(Context context,int layout,List<BanAnDTO> banAnDTOList){
        this.context = context;
        this.layout = layout;
        this.banAnDTOList = banAnDTOList;

        banAnDAO = new BanAnDAO(context);
        goiMonDAO = new GoiMonDAO(context);
        fragmentManager = ((TrangChuActivity)context).getSupportFragmentManager();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        viewHolderBanAn = (ViewHolderBanAn) ((View)v.getParent()).getTag();
        int vitri1 = (int) viewHolderBanAn.imBanAn.getTag();    // get vị trí bàn ăn lúc ta setTag ở phần create View
        int maban = banAnDTOList.get(vitri1).getMaBan();
        switch (id) {
            case R.id.imBanAn:
                //String tenban = viewHolderBanAn.txtTenBanAn.getText().toString();
                int vitri = (int) v.getTag();
                banAnDTOList.get(vitri).setDuocChon(true);  // set được chọn
                HienThiButton();
                ;break;
            case R.id.imGoiMon:
                Intent layITrangChu = ((TrangChuActivity)context).getIntent();
                int manhanvien = layITrangChu.getIntExtra("manhanvien", 0);

                String tinhtrang = banAnDAO.LayTinhTrangBanTheoMa(maban);
                if(tinhtrang.equals("false")){

                    // thực hiện code thêm bảng gọi món và cập nhật lại tình trạng bàn
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy");
                    String ngaygoi = dateFormat.format(calendar.getTime());

                    GoiMonDTO goiMonDTO = new GoiMonDTO();
                    goiMonDTO.setMaBan(maban);
                    goiMonDTO.setMaNV(manhanvien);
                    goiMonDTO.setNgayGoi(ngaygoi);
                    goiMonDTO.setTinhTrang("false");

                    long kiemtra = goiMonDAO.ThemGoiMon(goiMonDTO);
                    banAnDAO.CapNhatLaiTinhTrangBan(maban,"true");
                    if(kiemtra == 0){
                        Toast.makeText(context,context.getResources().getString(R.string.themthatbai),Toast.LENGTH_SHORT).show();
                    }

                }

                FragmentTransaction tranThucDonTransaction = fragmentManager.beginTransaction();
                HienThiThucDonFragment hienThiThucDonFragment = new HienThiThucDonFragment();

                Bundle bDuLieuThucDon = new Bundle();
                bDuLieuThucDon.putInt("maban",maban);

                hienThiThucDonFragment.setArguments(bDuLieuThucDon);

                tranThucDonTransaction.replace(R.id.content,hienThiThucDonFragment).addToBackStack("hienthibanan");
                tranThucDonTransaction.commit();

                break;

            case R.id.imThanhToan:

                Intent iThanhToan = new Intent(context, ThanhToanActivity.class);

                iThanhToan.putExtra("maban",maban);
                context.startActivity(iThanhToan);
                break;

        }
    }

    @Override
    public int getCount() {
        return banAnDTOList.size();
    }

    @Override
    public Object getItem(int i) {
        return banAnDTOList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return banAnDTOList.get(i).getMaBan();
    }

    private void HienThiButton(){

        viewHolderBanAn.imGoiMon.setVisibility(View.VISIBLE);
        viewHolderBanAn.imThanhToan.setVisibility(View.VISIBLE);
        viewHolderBanAn.imAnButton.setVisibility(View.VISIBLE);

        /*
        Animation animation = AnimationUtils.loadAnimation(context,R.anim.hieuung_hienthi_button_banan);
        viewHolderBanAn.imGoiMon.startAnimation(animation);
        viewHolderBanAn.imThanhToan.startAnimation(animation);
        viewHolderBanAn.imAnButton.startAnimation(animation);*/
    }
    private void AnButton(boolean hieuung){
        /*if(hieuung){
            Animation animation = AnimationUtils.loadAnimation(context,R.anim.hieuung_anbutton_banan);
            viewHolderBanAn.imGoiMon.startAnimation(animation);
            viewHolderBanAn.imThanhToan.startAnimation(animation);
            viewHolderBanAn.imAnButton.startAnimation(animation);
        }*/

        viewHolderBanAn.imGoiMon.setVisibility(View.INVISIBLE);
        viewHolderBanAn.imThanhToan.setVisibility(View.INVISIBLE);
        viewHolderBanAn.imAnButton.setVisibility(View.INVISIBLE);
    }

    public class ViewHolderBanAn{
        ImageView imBanAn,imGoiMon,imThanhToan,imAnButton;
        TextView txtTenBanAn;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;
        if(v == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            viewHolderBanAn = new ViewHolderBanAn();
            v = inflater.inflate(R.layout.custom_layout_hienthibanan,viewGroup,false);
            viewHolderBanAn.imBanAn = (ImageView) v.findViewById(R.id.imBanAn);
            viewHolderBanAn.imGoiMon = (ImageView) v.findViewById(R.id.imGoiMon);
            viewHolderBanAn.imThanhToan = (ImageView) v.findViewById(R.id.imThanhToan);
            viewHolderBanAn.imAnButton = (ImageView) v.findViewById(R.id.imAnButton);
            viewHolderBanAn.txtTenBanAn = (TextView) v.findViewById(R.id.txtTenBanAn);

            v.setTag(viewHolderBanAn);
        }else{
            viewHolderBanAn = (ViewHolderBanAn) v.getTag();
        }

        // Nếu bàn đc click vào sẽ show ra button
        if(banAnDTOList.get(i).isDuocChon()){
            HienThiButton();
        }else{
            AnButton(false);
        }

        BanAnDTO banAnDTO = banAnDTOList.get(i);
        // kiểm tra tình trạng bàn
        String kttinhtrang = banAnDAO.LayTinhTrangBanTheoMa(banAnDTO.getMaBan());
        if(kttinhtrang.equals("true")){
            viewHolderBanAn.imBanAn.setImageResource(R.drawable.banantrue);
        }else{
            viewHolderBanAn.imBanAn.setImageResource(R.drawable.banan);
        }

        viewHolderBanAn.txtTenBanAn.setText(banAnDTO.getTenBan());
        viewHolderBanAn.imBanAn.setTag(i); // luu vi tri  của bàn ăn

        viewHolderBanAn.imBanAn.setOnClickListener(this);
        viewHolderBanAn.imGoiMon.setOnClickListener(this);
        viewHolderBanAn.imThanhToan.setOnClickListener(this);
        viewHolderBanAn.imAnButton.setOnClickListener(this);
        return v;
    }
}

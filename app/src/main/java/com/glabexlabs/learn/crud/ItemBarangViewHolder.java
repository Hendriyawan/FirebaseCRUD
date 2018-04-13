package com.glabexlabs.learn.crud;

import android.support.v7.widget.*;
import android.view.*;
import android.widget.*;

public class ItemBarangViewHolder extends RecyclerView.ViewHolder
{
	public TextView namaBarang;
	public TextView merkBarang;
	public TextView hargaBarang;
	public View view;
	
	public ItemBarangViewHolder(View view){
		super(view);
		
		namaBarang = (TextView)view.findViewById(R.id.nama_barang);
		merkBarang = (TextView)view.findViewById(R.id.merk_barang);
		hargaBarang = (TextView)view.findViewById(R.id.harga_barang);
		this.view = view;
	}
}

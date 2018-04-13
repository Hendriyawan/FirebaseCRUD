package com.glabexlabs.learn.crud;

public class Barang
{
	private String key;
	private String nama;
	private String merk;
	private String harga;
	
	
	public Barang() {
		
	}
	public void setKey(String key){
		this.key = key;
	}
	
	public String getKey(){
		return key;
	}
	
	public void setNama(String nama){
		this.nama = nama;
	}
	
	public String getNama(){
		return nama;
	}
	
	public void setMerk(String merk){
		this.merk = merk;
	}
	
	public String getMerk(){
		return merk;
	}
	
	public void setHarga(String harga){
		this.harga = harga;
	}
	
	public String getHarga(){
		return harga;
	}
	
	public Barang(String namaBarang, String merkBarang, String hargaBarang){
		nama = namaBarang;
		merk = merkBarang;
		harga = hargaBarang;
	}
}

package com.sunmi.trans;

import android.os.Parcel;
import android.os.Parcelable;

public class TransBean implements Parcelable {

	private byte type = 0;
	private String text = "";
	private byte[] data = null;
	private int datalength = 0;
	
	public TransBean(){
		type = 0;
		data = null;
		text = "";
		datalength = 0;		
	};
	
	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		if(data != null){
			datalength = data.length;
			this.data = new byte[datalength];
			System.arraycopy(data, 0, this.data, 0, datalength);
		}
	}

	public TransBean(Parcel source){
		this.type = source.readByte();
		this.datalength = source.readInt();
		this.text = source.readString();
		if(datalength > 0){
			this.data = new byte[datalength];
			source.readByteArray(data);
		}
	}
	
	public TransBean(byte type, String text, byte[] data){
		this.type = type;
		this.text = text;
		if(data != null){
			this.datalength = data.length;
			this.data = new byte[datalength];
			System.arraycopy(data, 0, this.data, 0, datalength);
		}
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeByte(type);
		dest.writeInt(datalength);
		dest.writeString(text);
		if(data != null){
			dest.writeByteArray(data);
		}
	}
	
	public static Creator<TransBean> CREATOR = new Creator<TransBean>(){

		@Override
		public TransBean createFromParcel(Parcel source) {
			return new TransBean(source);
		}

		@Override
		public TransBean[] newArray(int size) {
			return new TransBean[size];
		}		
	};

}

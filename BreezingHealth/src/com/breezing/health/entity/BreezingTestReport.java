package com.breezing.health.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class BreezingTestReport implements Parcelable {

	private int metabolism;
	private int sport;
	private int digest;
	
	public int getMetabolism() {
		return metabolism;
	}
	public void setMetabolism(int metabolism) {
		this.metabolism = metabolism;
	}
	public int getSport() {
		return sport;
	}
	public void setSport(int sport) {
		this.sport = sport;
	}
	public int getDigest() {
		return digest;
	}
	public void setDigest(int digest) {
		this.digest = digest;
	}
	
	public int getTotalEnerge() {
		return metabolism + digest + sport;
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(metabolism);
		dest.writeInt(sport);
		dest.writeInt(digest);
	}
	
	public static final Parcelable.Creator<BreezingTestReport> CREATOR = new Creator<BreezingTestReport>() {

		@Override
		public BreezingTestReport createFromParcel(Parcel in) {
			BreezingTestReport report = new BreezingTestReport();
			report.setMetabolism(in.readInt());
			report.setSport(in.readInt());
			report.setDigest(in.readInt());
			return report;
		}

		@Override
		public BreezingTestReport[] newArray(int size) {
			return new BreezingTestReport[size];
		}
		
	};
	
}

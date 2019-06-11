package com.example.bankapp.Helpers;

import android.os.Parcel;
import android.os.Parcelable;



public class CurrentUser implements Parcelable {
    private String mName;
    private String mAge;
    private String mZipcode;
    private String mMail;
    private String mBudget;
    private String mDefault;
    private String mBusiness;
    private String mSavings;
    private String mPension;
    private String mNemID;



    public CurrentUser(String name, String age, String Zipcode, String mail, String Budget, String Default, String Business, String Savings, String Pension, String NemID){
        mName = name;
        mAge = age;
        mZipcode = Zipcode;
        mMail = mail;
        mBudget = Budget;
        mDefault = Default;
        mBusiness = Business;
        mSavings = Savings;
        mPension = Pension;
        mNemID = NemID;

    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmZipcode() {
        return mZipcode;
    }

    public void setmZipcode(String mZipcode) {
        this.mZipcode = mZipcode;
    }

    public void setmAge(String mAge) {
        this.mAge = mAge;
    }

    public void setmMail(String mMail) {
        this.mMail = mMail;
    }

    public void setmBudget(String mBudget) {
        this.mBudget = mBudget;
    }

    public void setmDefault(String mDefault) {
        this.mDefault = mDefault;
    }

    public void setmBusiness(String mBusiness) {
        this.mBusiness = mBusiness;
    }

    public void setmSavings(String mSavings) {
        this.mSavings = mSavings;
    }

    public void setmPension(String mPension) {
        this.mPension = mPension;
    }

    public String getmName() {
        return mName;
    }

    public String getmAge() {
        return mAge;
    }

    public String getmMail() {
        return mMail;
    }

    public String getmBudget() {
        return mBudget;
    }

    public String getmDefault() {
        return mDefault;
    }

    public String getmBusiness() {
        return mBusiness;
    }

    public String getmSavings() {
        return mSavings;
    }

    public String getmPension() {
        return mPension;
    }

    public String getmNemID() {
        return mNemID;
    }

    public void setmNemID(String mNemID) {
        this.mNemID = mNemID;
    }

    protected CurrentUser(Parcel in){
        mName = in.readString();
        mAge = in.readString();
        mZipcode = in.readString();
        mMail = in.readString();
        mBudget = in.readString();
        mDefault = in.readString();
        mBusiness = in.readString();
        mSavings = in.readString();
        mPension = in.readString();
        mNemID = in.readString();


    }

    public static final Creator<CurrentUser> CREATOR = new Creator<CurrentUser>() {
        @Override
        public CurrentUser createFromParcel(Parcel in) {
            return new CurrentUser(in);
        }

        @Override
        public CurrentUser[] newArray(int size) {
            return new CurrentUser[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeString(mAge);
        dest.writeString(mZipcode);
        dest.writeString(mMail);
        dest.writeString(mBudget);
        dest.writeString(mBusiness);
        dest.writeString(mDefault);
        dest.writeString(mSavings);
        dest.writeString(mPension);
        dest.writeString(mNemID);

    }
}

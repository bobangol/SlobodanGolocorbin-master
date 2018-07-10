package com.example.androiddevelopment.slobodangolocorbin.db.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;


@DatabaseTable(tableName = Prijava.TABLE_NAME_USERS)
public class Prijava {

    public static final String TABLE_NAME_USERS = "prijava";
    public static final String FIELD_NAME_ID     = "id";
    public static final String TABLE_PRIJAVA_NAZIV = "naziv";
    public static final String TABLE_PRIJAVA_OPIS = "opis";
    public static final String TABLE_PRIJAVA_STATUS = "status";
    public static final String TABLE_PRIJAVA_DATUM = "datum";
    public static final String TABLE_STAVKA_STAVKE = "stavke";

    @DatabaseField(columnName = FIELD_NAME_ID, generatedId = true)
    private int mId;

    @DatabaseField(columnName = TABLE_PRIJAVA_NAZIV)
    private String mIme;

    @DatabaseField(columnName = TABLE_PRIJAVA_OPIS)
    private String mOpis;

    @DatabaseField(columnName = TABLE_PRIJAVA_STATUS)
    private String mStatus;

    @DatabaseField(columnName = TABLE_PRIJAVA_DATUM)
    private String mDatum;

    @ForeignCollectionField(columnName = Prijava.TABLE_STAVKA_STAVKE, eager = true)
    private ForeignCollection<Stavka> stavke;

    public Prijava(){
    }

    public static String getTableNamePrijava() {
        return TABLE_NAME_USERS;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmIme() {
        return mIme;
    }

    public void setmIme(String mIme) {
        this.mIme = mIme;
    }

    public String getmOpis() {
        return mOpis;
    }

    public void setmOpis(String mOpis) {
        this.mOpis = mOpis;
    }

    public String getmStatus() {
        return mStatus;
    }

    public void setmStatus(String mStatus) {
        this.mStatus = mStatus;
    }

    public String getmDatum() {
        return mDatum;
    }

    public void setmDatum(String mDatum) {
        this.mDatum = mDatum;
    }

    public ForeignCollection<Stavka> getStavke() {
        return stavke;
    }

    public void setStavke(ForeignCollection<Stavka> stavke) {
        this.stavke = stavke;
    }

    @Override
    public String toString() {
        return mIme;
    }
}

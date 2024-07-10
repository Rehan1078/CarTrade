package com.example.mycarshoppingapp.OfflineDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.mycarshoppingapp.MainActivity;
import com.example.mycarshoppingapp.ModelClasses.CarDataModel;

import java.util.ArrayList;
import java.util.List;

public class DtHandler extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "mycarshoppingapp.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_CARS = "cars";
    private static final String COLUMN_MODEL = "model";
    private static final String COLUMN_ENGINE_CAPACITY = "engineCapacity";
    private static final String COLUMN_BODY_TYPE = "bodyType";
    private static final String COLUMN_REGISTERED_IN = "registeredIn";
    private static final String COLUMN_COLOR = "color";
    private static final String COLUMN_IMAGE_URL = "imageUrl";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_PHONE_NO = "phoneno";
    private static final String COLUMN_ID_OF_USER = "Id_of_user";


    private Context context;

    public DtHandler(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CARS_TABLE = "CREATE TABLE " + TABLE_CARS + "("
                + COLUMN_MODEL + " TEXT,"
                + COLUMN_ENGINE_CAPACITY + " REAL,"
                + COLUMN_BODY_TYPE + " TEXT,"
                + COLUMN_REGISTERED_IN + " TEXT,"
                + COLUMN_COLOR + " TEXT,"
                + COLUMN_IMAGE_URL + " TEXT,"
                + COLUMN_PRICE + " TEXT,"
                + COLUMN_PHONE_NO + " TEXT,"
                + COLUMN_ID_OF_USER + " TEXT"
                + ")";
        db.execSQL(CREATE_CARS_TABLE);
//        Toast.makeText(context, "ONCREATE OF DTHandler", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CARS);
        onCreate(db);
//        Toast.makeText(context, "ONUpgrade OF DTHandler", Toast.LENGTH_SHORT).show();
    }

    // Adding new car
    public void addCar(CarDataModel car) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_MODEL, car.getModel());
        values.put(COLUMN_ENGINE_CAPACITY, car.getEngineCapacity());
        values.put(COLUMN_BODY_TYPE, car.getBodyType());
        values.put(COLUMN_REGISTERED_IN, car.getRegisteredIn());
        values.put(COLUMN_COLOR, car.getColor());
        values.put(COLUMN_IMAGE_URL, car.getImageUrl());
        values.put(COLUMN_PRICE, car.getPrice());
        values.put(COLUMN_PHONE_NO, car.getPhoneno());
        values.put(COLUMN_ID_OF_USER, car.getId_of_user());

        db.insert(TABLE_CARS, null, values);
        db.close();
    }

    // Getting single car
    public CarDataModel getCar(String model) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CARS, new String[] { COLUMN_MODEL, COLUMN_ENGINE_CAPACITY, COLUMN_BODY_TYPE, COLUMN_REGISTERED_IN, COLUMN_COLOR, COLUMN_IMAGE_URL, COLUMN_PRICE, COLUMN_PHONE_NO, COLUMN_ID_OF_USER }, COLUMN_MODEL + "=?", new String[] { model }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        CarDataModel car = new CarDataModel(
                cursor.getString(0),
                cursor.getDouble(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getString(6),
                cursor.getString(7),
                cursor.getString(8));
        cursor.close();
        return car;
    }

    // Getting all cars
    public List<CarDataModel> getAllCars() {
        List<CarDataModel> carList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_CARS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                CarDataModel car = new CarDataModel();
                car.setModel(cursor.getString(0));
                car.setEngineCapacity(cursor.getDouble(1));
                car.setBodyType(cursor.getString(2));
                car.setRegisteredIn(cursor.getString(3));
                car.setColor(cursor.getString(4));
                car.setImageUrl(cursor.getString(5));
                car.setPrice(cursor.getString(6));
                car.setPhoneno(cursor.getString(7));
                car.setId_of_user(cursor.getString(8));
                carList.add(car);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return carList;
    }

    // Updating single car
    public int updateCar(CarDataModel car) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_MODEL, car.getModel());
        values.put(COLUMN_ENGINE_CAPACITY, car.getEngineCapacity());
        values.put(COLUMN_BODY_TYPE, car.getBodyType());
        values.put(COLUMN_REGISTERED_IN, car.getRegisteredIn());
        values.put(COLUMN_COLOR, car.getColor());
        values.put(COLUMN_IMAGE_URL, car.getImageUrl());
        values.put(COLUMN_PRICE, car.getPrice());
        values.put(COLUMN_PHONE_NO, car.getPhoneno());
        values.put(COLUMN_ID_OF_USER, car.getId_of_user());

        return db.update(TABLE_CARS, values, COLUMN_MODEL + " = ?", new String[] { car.getModel() });
    }

    // Deleting single car
    public void deleteCar(CarDataModel car) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CARS, COLUMN_MODEL + " = ?", new String[] { car.getModel() });
        db.close();
    }

    // Getting cars count
    public int getCarsCount() {
        String countQuery = "SELECT * FROM " + TABLE_CARS;
        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursorr = db.query(countQuery,null,null,null,null,null,null);
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();

        return count;
    }

}

package dao.user;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UserDAO {
    private static final String TABLE_NAME = "user";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_TOKEN = "token";
    private static final String[] COLUMNS = {COLUMN_NAME, COLUMN_USER_ID, COLUMN_TOKEN};

    private SQLiteDatabase db;

    /**
     * コンストラクタ
     * @param db
     */
    public UserDAO(SQLiteDatabase db) {
            this.db = db;
    }

    /**
     * 全データの取得
     * @return List<UserEntity>
     */
    public List<UserEntity> get() {
            List<UserEntity> entityList = new  ArrayList<UserEntity>();
            Cursor cursor = db.query(
                            TABLE_NAME,
                            COLUMNS,
                            null,
                            null,
                            null,
                            null,
                            null);

            while(cursor.moveToNext()) {
                    UserEntity entity = new UserEntity();
                    entity.setName(cursor.getString(0));
                    entity.setUserId(cursor.getInt(1));
                    entity.setToken(cursor.getString(2));
                    entityList.add(entity);
            }

            return entityList;
    }

    /**
     * データの登録
     * @param data
     * @return
     */
    public long insert(UserEntity entity) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME, entity.getName());
            values.put(COLUMN_USER_ID, entity.getUserId());
            values.put(COLUMN_TOKEN, entity.getToken());
            return db.insert(TABLE_NAME, null, values);
    }

    /**
     * データの更新
     * @param rowid
     * @param date
     * @return
     */
    public int update(UserEntity entity) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME, entity.getName());
            values.put(COLUMN_USER_ID, entity.getUserId());
            values.put(COLUMN_TOKEN, entity.getToken());
            //String whereClause = COLUMN_ID + "=" + entity.getRowId();
            return db.update(TABLE_NAME, values, null, null);
    }

    /**
     * データの削除
     * @return int
     */
    public int delete() {
            //String whereClause = COLUMN_ID + "=" + rowId;
            return db.delete(TABLE_NAME, null, null);
    }
}

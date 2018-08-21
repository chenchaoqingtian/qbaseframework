package net.cc.qbaseframework.coreutils;

import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @Description 数据库工具类
 */
public class DatabaseUtils
{
	public static final String BIGINT = "BIGINT";
	public static final String BOOLEAN = "BOOLEAN";
	public static final String BOOLEAN_NOT_NULL_DEFAULT_0 = "BOOLEAN NOT NULL DEFAULT 0";
	public static final String INTEGER = "INTEGER";
	public static final String INTEGER_DEFAULT_0 = "INTEGER DEFAULT 0";
	public static final String INTEGER_DEFAULT__1 = "INTEGER DEFAULT -1";
	public static final String INTEGER_KEY = "INTEGER PRIMARY KEY AUTOINCREMENT";
	public static final String INTEGER_NOT_NULL = "INTEGER NOT NULL";
	public static final String INTEGER_NOT_NULL_DEFAULT_0 = "INTEGER NOT NULL DEFAULT 0";
	public static final String INTEGER_NOT_NULL_DEFAULT_1 = "INTEGER NOT NULL DEFAULT 1";
	public static final String TEXT = "TEXT";
	public static final String TEXT_NOT_NULL = "TEXT NOT NULL";
	private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS %s (%s)";
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS %s";

	public static void createTable(SQLiteDatabase sqlDb, String tableName, Map<String, String> fieldMap)
	{
		String str = buildList(fieldMap);
		if (TextUtils.isEmpty(str)) throw new IllegalArgumentException("The field list can not build");
		sqlDb.execSQL(String.format(CREATE_TABLE, new Object[] { tableName, str }));
	}

	public static void dropTable(SQLiteDatabase sqlDb, String tableName)
	{
		sqlDb.execSQL(String.format(DROP_TABLE, tableName));
	}
	
	public static void addColumn(SQLiteDatabase sqlDb, String tableName, String columnName, String coloumType)
	{
		sqlDb.execSQL("ALTER TABLE " + tableName + " ADD COLUMN " + columnName + " " + coloumType);
	}
	
	private static String buildList(Map<String, String> fieldMap)
	{
		Iterator<Entry<String, String>> itertor = fieldMap.entrySet().iterator();
		StringBuilder sb = new StringBuilder();
		String result = null;
		while (itertor.hasNext()) {
			Entry<String, String> entry = (Entry<String, String>) itertor.next();
			sb.append(entry.getKey()).append(" ");
			sb.append(entry.getValue()).append(",");
		}
		if (sb.length() > 0) result = sb.substring(0, sb.length() - 1);
		return result;
	}
}

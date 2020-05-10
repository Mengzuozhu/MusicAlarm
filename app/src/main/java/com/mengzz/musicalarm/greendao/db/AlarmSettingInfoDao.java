package com.mengzz.musicalarm.greendao.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.mengzz.musicalarm.setting.AlarmCalendar.Converter;
import com.mengzz.musicalarm.setting.AlarmRepeatConverter;
import com.mengzz.musicalarm.setting.PlayedMode;
import com.mengzz.musicalarm.setting.PlayedMode.SongPlayedModeConverter;
import com.mengzz.musicalarm.song.SongInfo.SongInfoListConverter;
import java.util.ArrayList;

import com.mengzz.musicalarm.setting.AlarmSettingInfo;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "ALARM_SETTING_INFO".
*/
public class AlarmSettingInfoDao extends AbstractDao<AlarmSettingInfo, Long> {

    public static final String TABLENAME = "ALARM_SETTING_INFO";

    /**
     * Properties of entity AlarmSettingInfo.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Hour = new Property(1, int.class, "hour", false, "HOUR");
        public final static Property Minute = new Property(2, int.class, "minute", false, "MINUTE");
        public final static Property IsOpenStatus = new Property(3, Boolean.class, "isOpenStatus", false, "IS_OPEN_STATUS");
        public final static Property Interval = new Property(4, int.class, "interval", false, "INTERVAL");
        public final static Property RepeatFrequency = new Property(5, int.class, "repeatFrequency", false, "REPEAT_FREQUENCY");
        public final static Property Duration = new Property(6, int.class, "duration", false, "DURATION");
        public final static Property IsVibrated = new Property(7, boolean.class, "isVibrated", false, "IS_VIBRATED");
        public final static Property Remark = new Property(8, String.class, "remark", false, "REMARK");
        public final static Property AlarmRepeatMode = new Property(9, String.class, "alarmRepeatMode", false, "ALARM_REPEAT_MODE");
        public final static Property SongInfos = new Property(10, String.class, "songInfos", false, "SONG_INFOS");
        public final static Property PlayedMode = new Property(11, Integer.class, "playedMode", false, "PLAYED_MODE");
        public final static Property AlarmCalendars = new Property(12, String.class, "alarmCalendars", false, "ALARM_CALENDARS");
    }

    private final AlarmRepeatConverter alarmRepeatModeConverter = new AlarmRepeatConverter();
    private final SongInfoListConverter songInfosConverter = new SongInfoListConverter();
    private final SongPlayedModeConverter playedModeConverter = new SongPlayedModeConverter();
    private final Converter alarmCalendarsConverter = new Converter();

    public AlarmSettingInfoDao(DaoConfig config) {
        super(config);
    }
    
    public AlarmSettingInfoDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"ALARM_SETTING_INFO\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"HOUR\" INTEGER NOT NULL ," + // 1: hour
                "\"MINUTE\" INTEGER NOT NULL ," + // 2: minute
                "\"IS_OPEN_STATUS\" INTEGER," + // 3: isOpenStatus
                "\"INTERVAL\" INTEGER NOT NULL ," + // 4: interval
                "\"REPEAT_FREQUENCY\" INTEGER NOT NULL ," + // 5: repeatFrequency
                "\"DURATION\" INTEGER NOT NULL ," + // 6: duration
                "\"IS_VIBRATED\" INTEGER NOT NULL ," + // 7: isVibrated
                "\"REMARK\" TEXT," + // 8: remark
                "\"ALARM_REPEAT_MODE\" TEXT," + // 9: alarmRepeatMode
                "\"SONG_INFOS\" TEXT," + // 10: songInfos
                "\"PLAYED_MODE\" INTEGER," + // 11: playedMode
                "\"ALARM_CALENDARS\" TEXT);"); // 12: alarmCalendars
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"ALARM_SETTING_INFO\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, AlarmSettingInfo entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getHour());
        stmt.bindLong(3, entity.getMinute());
 
        Boolean isOpenStatus = entity.getIsOpenStatus();
        if (isOpenStatus != null) {
            stmt.bindLong(4, isOpenStatus ? 1L: 0L);
        }
        stmt.bindLong(5, entity.getInterval());
        stmt.bindLong(6, entity.getRepeatFrequency());
        stmt.bindLong(7, entity.getDuration());
        stmt.bindLong(8, entity.getIsVibrated() ? 1L: 0L);
 
        String remark = entity.getRemark();
        if (remark != null) {
            stmt.bindString(9, remark);
        }
 
        ArrayList alarmRepeatMode = entity.getAlarmRepeatMode();
        if (alarmRepeatMode != null) {
            stmt.bindString(10, alarmRepeatModeConverter.convertToDatabaseValue(alarmRepeatMode));
        }
 
        ArrayList songInfos = entity.getSongInfos();
        if (songInfos != null) {
            stmt.bindString(11, songInfosConverter.convertToDatabaseValue(songInfos));
        }
 
        PlayedMode playedMode = entity.getPlayedMode();
        if (playedMode != null) {
            stmt.bindLong(12, playedModeConverter.convertToDatabaseValue(playedMode));
        }
 
        ArrayList alarmCalendars = entity.getAlarmCalendars();
        if (alarmCalendars != null) {
            stmt.bindString(13, alarmCalendarsConverter.convertToDatabaseValue(alarmCalendars));
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, AlarmSettingInfo entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getHour());
        stmt.bindLong(3, entity.getMinute());
 
        Boolean isOpenStatus = entity.getIsOpenStatus();
        if (isOpenStatus != null) {
            stmt.bindLong(4, isOpenStatus ? 1L: 0L);
        }
        stmt.bindLong(5, entity.getInterval());
        stmt.bindLong(6, entity.getRepeatFrequency());
        stmt.bindLong(7, entity.getDuration());
        stmt.bindLong(8, entity.getIsVibrated() ? 1L: 0L);
 
        String remark = entity.getRemark();
        if (remark != null) {
            stmt.bindString(9, remark);
        }
 
        ArrayList alarmRepeatMode = entity.getAlarmRepeatMode();
        if (alarmRepeatMode != null) {
            stmt.bindString(10, alarmRepeatModeConverter.convertToDatabaseValue(alarmRepeatMode));
        }
 
        ArrayList songInfos = entity.getSongInfos();
        if (songInfos != null) {
            stmt.bindString(11, songInfosConverter.convertToDatabaseValue(songInfos));
        }
 
        PlayedMode playedMode = entity.getPlayedMode();
        if (playedMode != null) {
            stmt.bindLong(12, playedModeConverter.convertToDatabaseValue(playedMode));
        }
 
        ArrayList alarmCalendars = entity.getAlarmCalendars();
        if (alarmCalendars != null) {
            stmt.bindString(13, alarmCalendarsConverter.convertToDatabaseValue(alarmCalendars));
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public AlarmSettingInfo readEntity(Cursor cursor, int offset) {
        AlarmSettingInfo entity = new AlarmSettingInfo( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getInt(offset + 1), // hour
            cursor.getInt(offset + 2), // minute
            cursor.isNull(offset + 3) ? null : cursor.getShort(offset + 3) != 0, // isOpenStatus
            cursor.getInt(offset + 4), // interval
            cursor.getInt(offset + 5), // repeatFrequency
            cursor.getInt(offset + 6), // duration
            cursor.getShort(offset + 7) != 0, // isVibrated
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // remark
            cursor.isNull(offset + 9) ? null : alarmRepeatModeConverter.convertToEntityProperty(cursor.getString(offset + 9)), // alarmRepeatMode
            cursor.isNull(offset + 10) ? null : songInfosConverter.convertToEntityProperty(cursor.getString(offset + 10)), // songInfos
            cursor.isNull(offset + 11) ? null : playedModeConverter.convertToEntityProperty(cursor.getInt(offset + 11)), // playedMode
            cursor.isNull(offset + 12) ? null : alarmCalendarsConverter.convertToEntityProperty(cursor.getString(offset + 12)) // alarmCalendars
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, AlarmSettingInfo entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setHour(cursor.getInt(offset + 1));
        entity.setMinute(cursor.getInt(offset + 2));
        entity.setIsOpenStatus(cursor.isNull(offset + 3) ? null : cursor.getShort(offset + 3) != 0);
        entity.setInterval(cursor.getInt(offset + 4));
        entity.setRepeatFrequency(cursor.getInt(offset + 5));
        entity.setDuration(cursor.getInt(offset + 6));
        entity.setIsVibrated(cursor.getShort(offset + 7) != 0);
        entity.setRemark(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setAlarmRepeatMode(cursor.isNull(offset + 9) ? null : alarmRepeatModeConverter.convertToEntityProperty(cursor.getString(offset + 9)));
        entity.setSongInfos(cursor.isNull(offset + 10) ? null : songInfosConverter.convertToEntityProperty(cursor.getString(offset + 10)));
        entity.setPlayedMode(cursor.isNull(offset + 11) ? null : playedModeConverter.convertToEntityProperty(cursor.getInt(offset + 11)));
        entity.setAlarmCalendars(cursor.isNull(offset + 12) ? null : alarmCalendarsConverter.convertToEntityProperty(cursor.getString(offset + 12)));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(AlarmSettingInfo entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(AlarmSettingInfo entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(AlarmSettingInfo entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
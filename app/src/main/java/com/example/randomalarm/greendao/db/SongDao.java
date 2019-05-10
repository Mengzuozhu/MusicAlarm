package com.example.randomalarm.greendao.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.example.randomalarm.song.Song;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "SONG".
*/
public class SongDao extends AbstractDao<Song, Void> {

    public static final String TABLENAME = "SONG";

    /**
     * Properties of entity Song.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Name = new Property(0, String.class, "name", false, "NAME");
        public final static Property Path = new Property(1, String.class, "path", false, "PATH");
        public final static Property Album = new Property(2, String.class, "album", false, "ALBUM");
        public final static Property Artist = new Property(3, String.class, "artist", false, "ARTIST");
        public final static Property Size = new Property(4, long.class, "size", false, "SIZE");
        public final static Property Duration = new Property(5, int.class, "duration", false, "DURATION");
    }


    public SongDao(DaoConfig config) {
        super(config);
    }
    
    public SongDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"SONG\" (" + //
                "\"NAME\" TEXT," + // 0: name
                "\"PATH\" TEXT," + // 1: path
                "\"ALBUM\" TEXT," + // 2: album
                "\"ARTIST\" TEXT," + // 3: artist
                "\"SIZE\" INTEGER NOT NULL ," + // 4: size
                "\"DURATION\" INTEGER NOT NULL );"); // 5: duration
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"SONG\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Song entity) {
        stmt.clearBindings();
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(1, name);
        }
 
        String path = entity.getPath();
        if (path != null) {
            stmt.bindString(2, path);
        }
 
        String album = entity.getAlbum();
        if (album != null) {
            stmt.bindString(3, album);
        }
 
        String artist = entity.getArtist();
        if (artist != null) {
            stmt.bindString(4, artist);
        }
        stmt.bindLong(5, entity.getSize());
        stmt.bindLong(6, entity.getDuration());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Song entity) {
        stmt.clearBindings();
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(1, name);
        }
 
        String path = entity.getPath();
        if (path != null) {
            stmt.bindString(2, path);
        }
 
        String album = entity.getAlbum();
        if (album != null) {
            stmt.bindString(3, album);
        }
 
        String artist = entity.getArtist();
        if (artist != null) {
            stmt.bindString(4, artist);
        }
        stmt.bindLong(5, entity.getSize());
        stmt.bindLong(6, entity.getDuration());
    }

    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    @Override
    public Song readEntity(Cursor cursor, int offset) {
        Song entity = new Song( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // name
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // path
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // album
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // artist
            cursor.getLong(offset + 4), // size
            cursor.getInt(offset + 5) // duration
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Song entity, int offset) {
        entity.setName(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setPath(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setAlbum(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setArtist(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setSize(cursor.getLong(offset + 4));
        entity.setDuration(cursor.getInt(offset + 5));
     }
    
    @Override
    protected final Void updateKeyAfterInsert(Song entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    @Override
    public Void getKey(Song entity) {
        return null;
    }

    @Override
    public boolean hasKey(Song entity) {
        // TODO
        return false;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}

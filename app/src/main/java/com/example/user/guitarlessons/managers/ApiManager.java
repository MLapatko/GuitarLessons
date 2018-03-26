package com.example.user.guitarlessons.managers;

import android.text.TextUtils;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.example.user.guitarlessons.model.Course;
import com.example.user.guitarlessons.model.Genre;
import com.example.user.guitarlessons.model.Lesson;
import com.example.user.guitarlessons.model.Song;
import com.example.user.guitarlessons.model.UsersLessons;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 16.02.2018.
 */

public class ApiManager {

    private static ApiManager instance;

    public static ApiManager getInstance() {
        if (instance == null) {
            return instance = new ApiManager();
        }

        return instance;
    }

    private ApiManager() {
        Backendless.Data.mapTableToClass(LESSONS, Lesson.class);
        Backendless.Data.mapTableToClass(COURSES_TABLE, Course.class);
        Backendless.Data.mapTableToClass(SONGS_TABLE, Song.class);
        Backendless.Data.mapTableToClass(GENRES_TABLE, Genre.class);
        Backendless.Data.mapTableToClass(USERS_LESSON_TABLE, UsersLessons.class);
    }

    public static final String LESSONS = "Lessons";
    public static final String COURSES_TABLE = "Courses";
    public static final String GENRES_TABLE = "Genres";
    public static final String SONGS_TABLE = "Songs";
    public static final String USERS_LESSON_TABLE = "Users_lessons";
    public static final String COLUMN_COURSE_ID = "courseId";
    public static final String COLUMN_GENRE_ID = "genreId";
    public static final String COLUMN_FAVORITE_SONGS = "favoriteSongs";
    public static final String COLUMN_FAVORITE = "favorite";
    public static final String COLUMN_LESSON_ID = "lesson_id";
    private static final String USER_ID = "userId";
    public static final String COLUMN_FAVORITE_LESSONS = "favoriteLessons";


    private List<Course> courses = new ArrayList<>();
    private List<Genre> genres = new ArrayList<>();
    private List<Genre> chordsSongs = new ArrayList<>();
    private List<Genre> tabsSongs = new ArrayList<>();
    private List<Object> favorite = new ArrayList<>();

    private List<Song> favoriteTabsSongs = new ArrayList<>();
    private List<Song> favoriteChordsSongs = new ArrayList<>();

    public List<Song> getFavoriteTabsSongs() {
        return favoriteTabsSongs;
    }

    public void clearData() {
        this.favorite.clear();
        this.favoriteChordsSongs.clear();
        this.favoriteTabsSongs.clear();
        this.tabsSongs.clear();
        this.chordsSongs.clear();
        this.courses.clear();
        this.genres.clear();

    }
    public void setFavoriteTabsSongs(List<Song> favoriteTabsSongs) {
        if (favoriteTabsSongs != null) {
            this.favoriteTabsSongs.clear();
            this.favoriteTabsSongs.addAll(favoriteTabsSongs);
        }
    }

    public List<Song> getFavoriteChordsSongs() {
        return favoriteChordsSongs;
    }

    public void setFavoriteChordsSongs(List<Song> favoriteChordsSongs) {
        if (favoriteChordsSongs != null) {
            this.favoriteChordsSongs.clear();
            this.favoriteChordsSongs.addAll(favoriteChordsSongs);
        }
    }

    public List<Object> getFavorite() {
        return favorite;
    }

    public void setFavorite(List<Object> favorite) {

        if (favorite != null) {
            this.favorite.clear();
            this.favorite.addAll(favorite);
        }
    }

    public void setChordsSongs(List<Genre> chordsSongs) {
        if (chordsSongs != null) {
            this.chordsSongs.clear();
            this.chordsSongs.addAll(chordsSongs);
        }
    }

    public void setTabsSongs(List<Genre> tabsSongs) {
        if (tabsSongs != null) {
            this.tabsSongs.clear();
            this.tabsSongs.addAll(tabsSongs);
        }
    }

    public List<Genre> getChordsSongs() {
        return chordsSongs;
    }

    public <T> void updateFavoriteSongs(List<T> favoriteSongs) {
        List<Song> newFavoriteTabsSongs = new ArrayList<>();
        List<Song> newFavoriteChordsSongs = new ArrayList<>();
        for (Object object : favoriteSongs) {
            if (object instanceof Song) {
                if (((Song) object).getTabs()) {
                    newFavoriteTabsSongs.add((Song) object);
                }
                if (((Song) object).getChords()) {
                    newFavoriteChordsSongs.add((Song) object);
                }
            }

        }
        setFavoriteChordsSongs(newFavoriteChordsSongs);
        setFavoriteTabsSongs(newFavoriteTabsSongs);
    }

    public void updateSongs(List<Genre> genres) {
        if (genres != null) {
            List<Genre> newChordsSongs = new ArrayList<>();
            List<Genre> newTabsSongs = new ArrayList<>();

            for (Genre genre : genres) {
                ArrayList<Song> chordsSong = new ArrayList<>();
                List<Song> tabsSong = new ArrayList<>();

                for (Song song : genre.getSongs()) {
                    if (song.getChords()) {
                        chordsSong.add(song);
                    }
                    if (song.getTabs()) {
                        tabsSong.add(song);
                    }
                }
                if (!chordsSong.isEmpty()) {
                    Genre newGenre=new Genre();
                    newGenre.setObjectId(genre.getObjectId());
                    newGenre.setTitle(genre.getTitle());
                    newGenre.setSongs(chordsSong);
                    newChordsSongs.add(newGenre);
                }
                if (!tabsSong.isEmpty()) {
                    Genre newGenre=new Genre();
                    newGenre.setObjectId(genre.getObjectId());
                    newGenre.setTitle(genre.getTitle());
                    newGenre.setSongs(tabsSong);
                    newTabsSongs.add(newGenre);
                }
            }
            setChordsSongs(newChordsSongs);
            setTabsSongs(newTabsSongs);
        }


    }

    public List<Genre> getTabsSongs() {
        return tabsSongs;
    }

    public void setCourses(List<Course> courses) {
        if (courses != null) {
            this.courses.clear();
            this.courses.addAll(courses);
        }
    }

    public void setGenres(List<Genre> genres) {
        if (genres != null) {
            this.genres.clear();
            this.genres.addAll(genres);
        }
    }

    public List<Genre> getGenresList() {
        return this.genres;
    }

    public List<Course> getCoursesList() {
        return this.courses;
    }

    public <T> void deleteFromUsersLessons(final T lesson, final String columnName,
                                           final DbListener<Integer> listener) {
        final List<T> lessons = new ArrayList<>();
        lessons.add(lesson);
        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setProperties(COLUMN_FAVORITE_LESSONS, COLUMN_FAVORITE_SONGS);
        queryBuilder.setWhereClause(USER_ID + "='" + UserAuthManager.getInstance().getCurrentUser().getObjectId() + "'");
        Backendless.Persistence.of(UsersLessons.class).find(queryBuilder, new AsyncCallback<List<UsersLessons>>() {
            @Override
            public void handleResponse(List<UsersLessons> response) {
                Backendless.Persistence.of(UsersLessons.class).deleteRelation(response.get(0), columnName, lessons,
                        new AsyncCallback<Integer>() {
                            @Override
                            public void handleResponse(Integer response) {
                                if (lesson instanceof Lesson) {
                                    favorite.remove(isFavorite(((Lesson) lesson).getObjectId()));
                                } else if (lesson instanceof Song) {
                                    favorite.remove(isFavorite(((Song) lesson).getObjectId()));
                                    if (((Song) lesson).getChords()) {
                                        removeFromFavoriteChordsSongs(((Song) lesson).getObjectId());
                                    }
                                    if (((Song) lesson).getTabs()) {
                                        removeFromFavoriteTabsSongs(((Song) lesson).getObjectId());
                                    }
                                }
                                if (listener != null) {
                                    listener.onSuccess(response);
                                }

                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {
                                if (listener != null) {
                                    listener.onError(fault);
                                }
                            }
                        });
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                if (listener != null) {
                    listener.onError(fault);
                }
            }
        });
    }

    private void removeFromFavoriteChordsSongs(String id) {
        for (int i = 0; i < favoriteChordsSongs.size(); i++) {
            if (TextUtils.equals(favoriteChordsSongs.get(i).getObjectId(), id)) {
                favoriteChordsSongs.remove(i);
                return;
            }
        }
    }

    private void removeFromFavoriteTabsSongs(String id) {
        for (int i = 0; i < favoriteTabsSongs.size(); i++) {
            if (TextUtils.equals(favoriteTabsSongs.get(i).getObjectId(), id)) {
                favoriteTabsSongs.remove(i);
                return;
            }
        }
    }

    public <T> void addToUsersLessons(final T lesson, final String columnName,
                                      final DbListener<Integer> listener) {
        final List<T> lessons = new ArrayList<>();
        lessons.add(lesson);
        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setProperties(COLUMN_FAVORITE_LESSONS, COLUMN_FAVORITE_SONGS);
        queryBuilder.setWhereClause(USER_ID + "='" + UserAuthManager.getInstance().getCurrentUser().getObjectId() + "'");
        Backendless.Persistence.of(UsersLessons.class).find(queryBuilder, new AsyncCallback<List<UsersLessons>>() {
            @Override
            public void handleResponse(List<UsersLessons> response) {
                if (response.size()==0){
                    addUserToUsersLessons(UserAuthManager.getInstance().getCurrentUser().getObjectId(),columnName,lessons,lesson,listener);
                }else{
                    addToLessons(response.get(0),columnName,lessons,lesson,listener);
                }

            }

            @Override
            public void handleFault(BackendlessFault fault) {
                if (listener != null) {
                    listener.onError(fault);
                }
            }
        });

    }

    private <T> void addUserToUsersLessons(String idUser, final String columnName, final List<T> lessons, final T lesson, final DbListener listener) {
        UsersLessons usersLessons=new UsersLessons();
        usersLessons.setUserId(idUser);
        Backendless.Persistence.save(usersLessons,
                new AsyncCallback<UsersLessons>() {
                    @Override
                    public void handleResponse(UsersLessons response) {
                        addToLessons(response,columnName,lessons,lesson,listener);
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        if (listener != null) {
                            listener.onError(fault);
                        }
                    }
                });
    }

    private <T>void addToLessons(UsersLessons usersLessons, String columnName, final List<T>lessons , final T lesson, final DbListener listener){
        Backendless.Persistence.of(UsersLessons.class).addRelation(usersLessons, columnName, lessons,
                new AsyncCallback<Integer>() {
                    @Override
                    public void handleResponse(Integer response) {
                        favorite.add(lesson);
                        if (lesson instanceof Song) {
                            if (((Song) lesson).getTabs()) {
                                favoriteTabsSongs.add((Song) lesson);
                            }
                            if (((Song) lesson).getChords()) {
                                favoriteChordsSongs.add((Song) lesson);
                            }
                        }

                        if (listener != null) {
                            listener.onSuccess(response);
                        }
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        if (listener != null) {
                            listener.onError(fault);
                        }
                    }
                });
    }

    public List<Song> getFavoriteSongs() {
        final DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setProperties("objectId", "title", "author", "chords", "tabs");
        queryBuilder.setWhereClause(USERS_LESSON_TABLE + "[" + COLUMN_FAVORITE_SONGS + "]." + USER_ID + "='" + UserAuthManager.getInstance().getCurrentUser().getObjectId() + "'");
        return Backendless.Persistence.of(Song.class).find(queryBuilder);
    }

    public List<Lesson> getFavoriteLessons() {
        final DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setProperties("objectId", "title", "videoUrl");
        queryBuilder.setWhereClause(USERS_LESSON_TABLE + "[" + COLUMN_FAVORITE_LESSONS + "]." + USER_ID + "='" + UserAuthManager.getInstance().getCurrentUser().getObjectId() + "'");
        return Backendless.Persistence.of(Lesson.class).find(queryBuilder);
    }

    public List<Course> getCourses() {
        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.addSortBy("created DESC");
        return Backendless.Persistence.of(Course.class).find(queryBuilder);

    }

    public List<Genre> getGenres() {
        return Backendless.Persistence.of(Genre.class).find(DataQueryBuilder.create());
    }

    public List<Lesson> getLessonsInCourse(String courseId){
        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setWhereClause(COLUMN_COURSE_ID + "='" + courseId + "'");
        queryBuilder.setProperties("objectId", "title", "videoUrl");
        return Backendless.Data.of(Lesson.class).find(queryBuilder);
    }

    public List<Song> getSongsInGenre(String genreId) {
        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setProperties("objectId", "title", "author", "videoUrl", "chords", "tabs");
        queryBuilder.setWhereClause(COLUMN_GENRE_ID + "='" + genreId + "'");
        return Backendless.Data.of(Song.class).find(queryBuilder);
    }

    public void getLessonById(String idLesson, final DbListener<Lesson> listener) {
        Backendless.Persistence.of(Lesson.class).findById(idLesson, new AsyncCallback<Lesson>() {
            @Override
            public void handleResponse(Lesson response) {
                if (listener != null) {
                    listener.onSuccess(response);
                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                listener.onError(fault);
            }
        });
    }

    public void getSongById(String songId, final DbListener<Song> listener) {
        Backendless.Persistence.of(Song.class).findById(songId, new AsyncCallback<Song>() {
            @Override
            public void handleResponse(Song response) {
                if (listener != null) {
                    listener.onSuccess(response);
                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                if (listener != null) {
                    listener.onError(fault);
                }
            }
        });
    }

    public int isFavorite(String id) {
        if (favorite.size() > 0) {
            for (int i = 0; i < favorite.size(); i++) {
                Object object = favorite.get(i);
                if (object instanceof Lesson) {
                    if (TextUtils.equals(((Lesson) object).getObjectId(), id)) {
                        return i;
                    }
                } else if (object instanceof Song) {
                    if (TextUtils.equals(((Song) object).getObjectId(), id)) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }




    public interface DbListener<T> {
        void onSuccess(T response);

        void onError(BackendlessFault fault);
    }
}

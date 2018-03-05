package com.example.user.guitarlessons.managers;

import android.text.TextUtils;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
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
    public static final String USERS_LESSON_TABLE="Users_lessons";
    public static final String COLUMN_COURSE_ID = "courseId";
    public static final String COLUMN_GENRE_ID = "genreId";
    public static final String COLUMN_FAVORITE_SONGS = "favoriteSongs";
    public static final String COLUMN_FAVORITE = "favorite";
    public static final String COLUMN_LESSON_ID = "lesson_id";
    private static final String USER_ID ="userId" ;
    public static final String  COLUMN_FAVORITE_LESSONS="favoriteLessons";

    private BackendlessUser mCurrentUser = UserAuthManager.getInstance().getCurrentUser();
    private List<Course> courses = new ArrayList<>();
    private List<Genre> genres = new ArrayList<>();

    List<Object> favorite=new ArrayList<>();

    public List<Object> getFavorite() {
        return favorite;
    }

    public void setFavorite(List<Object> favorite) {
        this.favorite = favorite;
    }


    private List<String> favoriteIdList=new ArrayList<>();


    public void setCourses(List<Course> courses) {
        if (courses != null) {
            this.courses.clear();
            this.courses.addAll(courses);
        }
    }
    public void setFavoriteList(List<String> favoriteList) {
        if ( favoriteList!= null) {
            this.favoriteIdList.clear();
            this.favoriteIdList.addAll(favoriteList);
        }
    }
    public void addToFavoriteList(String id){
        if (id!=null){
            favoriteIdList.add(id);
        }
    }
    public void deleteFromFavoriteList(String id){
        if (id!=null){
            favoriteIdList.remove(id);
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

    public BackendlessUser getmCurrentUser() {
        return mCurrentUser;
    }

    public <T> void updateCurrentUser(final DbListener listener, final T queryResponce) {
        Backendless.Persistence.of(BackendlessUser.class).findById(mCurrentUser.getObjectId(),
                new AsyncCallback<BackendlessUser>() {
                    @Override
                    public void handleResponse(BackendlessUser response) {
                        mCurrentUser = response;
                        if (listener != null) {
                            listener.onSuccess(queryResponce);
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

    public <T> void deleteFromUsersLessons(final T lesson, final String columnName,
                                           final DbListener<Integer> listener) {
        final List<T> lessons = new ArrayList<>();
        lessons.add(lesson);
        DataQueryBuilder queryBuilder=DataQueryBuilder.create();
        queryBuilder.setProperties(COLUMN_FAVORITE_LESSONS,COLUMN_FAVORITE_SONGS);
        queryBuilder.setWhereClause(USER_ID+ "='" +mCurrentUser.getObjectId()+ "'");
        Backendless.Persistence.of(UsersLessons.class).find(queryBuilder, new AsyncCallback<List<UsersLessons>>() {
            @Override
            public void handleResponse(List<UsersLessons> response) {
                Backendless.Persistence.of(UsersLessons.class).deleteRelation(response.get(0), columnName, lessons,
                        new AsyncCallback<Integer>() {
                    @Override
                    public void handleResponse(Integer response) {
                        if (lesson instanceof Lesson) {
                            favorite.remove(isFavorite(((Lesson) lesson).getObjectId()));
                        }else if (lesson instanceof Song){
                            favorite.remove(isFavorite(((Song) lesson).getObjectId()));
                        }
                        if (listener!=null){
                            listener.onSuccess(response);
                        }

                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        if (listener!=null){
                            listener.onError(fault);
                        }
                    }
                });
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                if (listener!=null){
                    listener.onError(fault);
                }
            }
        });
    }

    public <T> void addToUsersLessons(final T lesson, final String columnName,
                                      final DbListener<Integer> listener) {
        final List<T> lessons = new ArrayList<>();
        lessons.add(lesson);
        DataQueryBuilder queryBuilder=DataQueryBuilder.create();
        queryBuilder.setProperties(COLUMN_FAVORITE_LESSONS,COLUMN_FAVORITE_SONGS);
        queryBuilder.setWhereClause(USER_ID+ "='" +mCurrentUser.getObjectId()+ "'");
        Backendless.Persistence.of(UsersLessons.class).find(queryBuilder, new AsyncCallback<List<UsersLessons>>() {
            @Override
            public void handleResponse(List<UsersLessons> response) {
                Backendless.Persistence.of(UsersLessons.class).addRelation(response.get(0), columnName, lessons,
                        new AsyncCallback<Integer>() {
                            @Override
                            public void handleResponse(Integer response) {
                                favorite.add(lesson);
                                if (listener!=null){
                                    listener.onSuccess(response);
                                }
                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {
                                if (listener!=null){
                                    listener.onError(fault);
                                }
                            }
                        });
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                if (listener!=null){
                    listener.onError(fault);
                }
            }
        });

    }
  public List<Song> getFavoriteSongs(){
       final DataQueryBuilder queryBuilder=DataQueryBuilder.create();
        queryBuilder.setProperties("objectId","title","author");
        queryBuilder.setWhereClause(USERS_LESSON_TABLE+"["+COLUMN_FAVORITE_SONGS+ "]."+USER_ID+"='" +mCurrentUser.getObjectId()+ "'");
        return Backendless.Persistence.of(Song.class).find(queryBuilder);
    }

    public List<Lesson> getFavoriteLessons(){
        final DataQueryBuilder queryBuilder=DataQueryBuilder.create();
        queryBuilder.setProperties("objectId","title");
        queryBuilder.setWhereClause(USERS_LESSON_TABLE+"["+COLUMN_FAVORITE_LESSONS+ "]."+USER_ID+"='" +mCurrentUser.getObjectId()+ "'");
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

    public List<Lesson> getLessonsInCourse(String courseId) {
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
        for (int i = 0; i <favorite.size() ; i++) {
            Object object=favorite.get(i);
            if (object instanceof Lesson){
                if (TextUtils.equals(((Lesson) object).getObjectId(),id))
                {
                    return i;
                }
            }else if (object instanceof Song){
                if (TextUtils.equals(((Song) object).getObjectId(),id)){
                    return i;
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

package com.example.user.guitarlessons.managers;

import com.example.user.guitarlessons.model.Course;
import com.example.user.guitarlessons.model.Genre;
import com.example.user.guitarlessons.model.Lesson;
import com.example.user.guitarlessons.model.LessonDetails;
import com.example.user.guitarlessons.model.Song;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by user on 27.02.2018.
 */

public class ContentManager {

    private static ContentManager sInstance;

    public static ContentManager getInstance() {
        if (sInstance == null) {
            sInstance = new ContentManager();
        }

        return sInstance;
    }

    private Disposable mDisposable;

    public void loadCourses(final ContentListener<List<Course>> listener) {

        Single<List<Course>> courseSingle = Single.create(new SingleOnSubscribe<List<Course>>() {
            @Override
            public void subscribe(final SingleEmitter<List<Course>> emitter) throws Exception {
                List<Course> courses = Collections.emptyList();
                try {
                    courses = ApiManager.getInstance().getCourses();
                } catch (Exception e) {
                    listener.onError(e);
                }
                Single<List<Course>> courseListSingle = Single.create(new SingleOnSubscribe<List<Course>>() {
                    @Override
                    public void subscribe(SingleEmitter<List<Course>> e) throws Exception {
                        e.onSuccess(new ArrayList<Course>());
                    }
                });

                for (final Course course : courses) {
                    Single<Course> lessonSingle = Single.create(new SingleOnSubscribe<Course>() {
                        @Override
                        public void subscribe(SingleEmitter<Course> e) throws Exception {
                            List<Lesson> lessons = ApiManager.getInstance().getLessonsInCourse(course.getObjectId());
                            course.setLessons(lessons);
                            e.onSuccess(course);
                        }
                    });

                    courseListSingle = courseListSingle
                            .zipWith(lessonSingle, new BiFunction<List<Course>, Course, List<Course>>() {
                                @Override
                                public List<Course> apply(List<Course> courseList, Course course1) throws Exception {
                                    courseList.add(course1);
                                    return courseList;
                                }
                            });
                }

                final List<Course> finalCourses = courses;
                courseListSingle
                        .subscribeWith(new DisposableSingleObserver<List<Course>>() {
                            @Override
                            public void onSuccess(List<Course> courses) {
                                emitter.onSuccess(courses);
                            }

                            @Override
                            public void onError(Throwable e) {
                                emitter.onSuccess(finalCourses);
                            }
                        });
            }
        });

        stopProcess();
        mDisposable = courseSingle
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<Course>>() {
                    @Override
                    public void onSuccess(List<Course> courses) {
                        if (listener != null) {
                            ApiManager.getInstance().setCourses(courses);
                            listener.onSuccess(courses);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (listener != null) {
                            listener.onError(e);
                        }
                    }
                });
    }

    public void loadGenres(final ContentListener<List<Genre>> listener) {

        Single<List<Genre>> genreSingle = Single.create(new SingleOnSubscribe<List<Genre>>() {
            @Override
            public void subscribe(final SingleEmitter<List<Genre>> emitter) throws Exception {

                List<Genre> genres = Collections.emptyList();
                try {
                    genres = ApiManager.getInstance().getGenres();
                } catch (Exception e) {
                    listener.onError(e);
                }

                Single<List<Genre>> genreListSingle = Single.create(new SingleOnSubscribe<List<Genre>>() {
                    @Override
                    public void subscribe(SingleEmitter<List<Genre>> e) throws Exception {
                        e.onSuccess(new ArrayList<Genre>());
                    }
                });

                for (final Genre genre : genres) {
                    Single<Genre> songSingle = Single.create(new SingleOnSubscribe<Genre>() {
                        @Override
                        public void subscribe(SingleEmitter<Genre> e) throws Exception {
                            List<Song> songs = ApiManager.getInstance().getSongsInGenre(genre.getObjectId());
                            genre.setSongs(songs);
                            e.onSuccess(genre);
                        }
                    });

                    genreListSingle = genreListSingle
                            .zipWith(songSingle, new BiFunction<List<Genre>, Genre, List<Genre>>() {
                                @Override
                                public List<Genre> apply(List<Genre> genreList, Genre genre1) throws Exception {
                                    genreList.add(genre1);
                                    return genreList;
                                }
                            });
                }

                final List<Genre> finalGenres = genres;
                genreListSingle
                        .subscribeWith(new DisposableSingleObserver<List<Genre>>() {
                            @Override
                            public void onSuccess(List<Genre> genres) {
                                emitter.onSuccess(genres);
                            }

                            @Override
                            public void onError(Throwable e) {
                                emitter.onSuccess(finalGenres);
                            }
                        });
            }
        });

        stopProcess();
        mDisposable = genreSingle
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<Genre>>() {
                    @Override
                    public void onSuccess(List<Genre> genres) {
                        if (listener != null) {
                            listener.onSuccess(genres);
                            ApiManager.getInstance().setGenres(genres);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (listener != null) {
                            listener.onError(e);
                        }
                    }
                });
    }

    public void getLesson(final String idLesson, final ContentListener<Lesson> listener) {
        Single<Lesson> lessonSingle = Single.create(new SingleOnSubscribe<Lesson>() {
            @Override
            public void subscribe(SingleEmitter<Lesson> e) throws Exception {
                Lesson lesson=ApiManager.getInstance().getLessonById(idLesson);
                e.onSuccess(lesson);
            }
        });
      lessonSingle=lessonSingle.flatMap(new Function<Lesson, Single<Lesson>>() {
            @Override
            public Single<Lesson> apply(final Lesson lesson) throws Exception {
                return Single.create(new SingleOnSubscribe<Lesson>() {
                    @Override
                    public void subscribe(SingleEmitter<Lesson> e) throws Exception {
                        LessonDetails details=ApiManager.getInstance().getLessonsDetails(lesson.getObjectId()).get(0);
                        lesson.setDetails(details);
                        e.onSuccess(lesson);

                    }
                });
            }
        });
      lessonSingle.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribeWith(new DisposableSingleObserver<Lesson>() {
          @Override
          public void onSuccess(Lesson lesson) {
              if (listener!=null){
                  listener.onSuccess(lesson);
              }
          }

          @Override
          public void onError(Throwable e) {
                if (listener!=null){
                    listener.onError(e);
                }
          }
      });

    }

    public void stopProcess() {
        if (mDisposable != null) {
            mDisposable.dispose();
        }
    }

    public interface ContentListener<T> {
        void onSuccess(T response);

        void onError(Throwable e);
    }
}

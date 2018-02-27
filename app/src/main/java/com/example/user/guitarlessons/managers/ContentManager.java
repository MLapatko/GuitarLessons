package com.example.user.guitarlessons.managers;

import com.example.user.guitarlessons.ApiManager;
import com.example.user.guitarlessons.model.Course;
import com.example.user.guitarlessons.model.Lesson;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
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

                final List<Course> courses = ApiManager.getInstance().getCourses();

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

                courseListSingle
                        .subscribeWith(new DisposableSingleObserver<List<Course>>() {
                            @Override
                            public void onSuccess(List<Course> courses) {
                                emitter.onSuccess(courses);
                            }

                            @Override
                            public void onError(Throwable e) {
                                emitter.onSuccess(courses);
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

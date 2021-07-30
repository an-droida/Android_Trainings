package com.android.mvvm_injection_sample.rx_kotlin_sample.extensions

import com.android.mvvm_injection_sample.rx_kotlin_sample.models.Blog
import com.android.mvvm_injection_sample.rx_kotlin_sample.models.BlogDetails
import com.android.mvvm_injection_sample.rx_kotlin_sample.models.User
import com.android.mvvm_injection_sample.rx_kotlin_sample.models.UserProfile
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.BiFunction

val mUserList = mutableListOf<User>(
    User(1, "demo1", 15),
    User(2, "demo2", 18),
    User(3, "demo3", 16),
    User(4, "demo4", 15),
    User(5, "demo5", 20),
    User(6, "demo6", 20),
    User(7, "demo7", 22),
    User(8, "demo8", 22),
    User(9, "demo9", 22)
)

val mUserProfileList = mutableListOf<UserProfile>(
    UserProfile(1, "demo1", 15, ""),
    UserProfile(2, "demo2", 18, ""),
    UserProfile(3, "demo3", 20, ""),
    UserProfile(4, "demo4", 15, ""),
    UserProfile(5, "demo5", 24, ""),
    UserProfile(6, "demo6", 21, ""),
    UserProfile(7, "demo7", 22, ""),
    UserProfile(8, "demo8", 22, "")
)

val mBlogList = mutableListOf<Blog>(
    Blog(1, 1, "title1", "content1"),
    Blog(2, 1, "title2", "content2"),
    Blog(3, 2, "title3", "content4"),
    Blog(4, 2, "title4", "content2"),
    Blog(5, 2, "title5", "content3"),
    Blog(6, 3, "title6", "content3"),
    Blog(7, 3, "title7", "content2"),
    Blog(8, 4, "title8", "content5")
)

fun getUserProfile(id: Long): Observable<UserProfile> {
    return Observable.fromIterable(mUserProfileList)
        .filter {
            it.id == id
        }
}

//flatMap
fun flatMapOperator(): Observable<User> {
    return Observable.fromIterable(mUserList)
}

fun flatMapOperatorTwo(): Observable<List<User>> {
    return Observable.just(mUserList)
}


//groupBy
fun groupByOperator(): Observable<User> {
    return Observable.fromIterable(mUserList)
}


// merge
fun getUser(): Observable<User> {
    return Observable.fromIterable(mUserList)
}

fun getProfile(): Observable<UserProfile> {
    return Observable.fromIterable(mUserProfileList)
}

fun mergeOperator(): Observable<Any> {
    return Observable.merge(getUser(), getProfile())
}


// concat
fun getNumbs1To100(): Observable<Int> {
    return Observable.range(1, 100)
}

fun getNumbs101To150(): Observable<Int> {
    return Observable.range(101, 50)
}

fun concatOperator(): Observable<Int> {
    return getNumbs1To100().concatWith(getNumbs101To150())
}


// startWith
fun startWithOperator(): Observable<Int> {
    return getNumbs101To150().startWith(getNumbs1To100())
}


//zip
fun zipOperators(): Observable<Any> {
    val num = Observable.just(1, 2, 3, 4, 5)
    val char = Observable.just("A", "B", "C", "D")
    return Observable.zip(num, char, { t1, t2 ->
        "$t1 $t2"
    })
}

fun getBlog(): Observable<Blog> {
    return Observable.fromIterable(mBlogList)
}

fun getBlogs(): Observable<List<Blog>> {
    return Observable.just(mBlogList)
}

fun getUsers(): Observable<List<User>> {
    return Observable.just(mUserList)
}


fun zipOperatorTwo(): Observable<List<BlogDetails>> {
    return Observable.zip(getUsers(), getBlogs(), BiFunction { t1, t2 ->
        blogDetail(t1, t2)
    })
}

fun blogDetail(t1: List<User>, t2: List<Blog>): List<BlogDetails> {
    val listBlogDetail = mutableListOf<BlogDetails>()
    t1.forEach { user ->
        t2.forEach { blog ->
            if (blog.userId == user.id) {
                listBlogDetail.add(
                    BlogDetails(
                        blog.id, blog.userId, blog.title, blog.content, user
                    )
                )
            }
        }
    }
    return  listBlogDetail
}





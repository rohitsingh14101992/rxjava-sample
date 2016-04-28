package rxjava.droid.retrofit;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import rxjava.droid.R;
import rxjava.droid.retrofit.beans.Contributor;
import rxjava.droid.retrofit.beans.GithubApi;
import rxjava.droid.retrofit.beans.User;
import rxjava.droid.retrofit.beans.UserWithContributor;
import rxjava.droid.retrofit.service.GithubService;

public class RetrofitExampleActivity extends AppCompatActivity {

    private EditText mOwnerNameEt, mReponameEt;
    private RecyclerView mRecyclerView;
    private RetrofitExampleAdapter mAdapter;
    private CompositeSubscription mCompositeSubscription;
    private GithubApi mGithubService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit_example_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.git_contributors_rv);
        mReponameEt = (EditText) findViewById(R.id.reponame_et);
        mOwnerNameEt = (EditText) findViewById(R.id.owner_et);
        setSupportActionBar(toolbar);
        String githubToken = getResources().getString(R.string.github_oauth_token);
        mGithubService = GithubService.createGithubService(githubToken);
        mCompositeSubscription = new CompositeSubscription();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void getDetails(View view) {
        mAdapter.clear();
        mAdapter.setReponame(mReponameEt.getText().toString());
        mCompositeSubscription.add(
                mGithubService.contributors(mOwnerNameEt.getText().toString(), mReponameEt.getText().toString())
                        .flatMap(new Func1<List<Contributor>, Observable<Contributor>>() {
                            @Override
                            public Observable<Contributor> call(List<Contributor> contributors) {
                                return Observable.from(contributors);
                            }
                        })
                        .flatMap(new Func1<Contributor, Observable<UserWithContributor>>() {

                            @Override
                            public Observable<UserWithContributor> call(Contributor contributor) {
                                Observable<User> observableUser = mGithubService.user(contributor.login);
                                return Observable.zip(observableUser,
                                        Observable.just(contributor),
                                        new Func2<User, Contributor, UserWithContributor>() {
                                            @Override
                                            public UserWithContributor call(User user, Contributor contributor) {
                                                return new UserWithContributor(user, contributor);
                                            }
                                        });
                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<UserWithContributor>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(UserWithContributor userWithContributor) {
                                mAdapter.add(userWithContributor);
                                mAdapter.notifyDataSetChanged();
                            }
                        }));

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mRecyclerView.getAdapter() == null) {
            mAdapter = new RetrofitExampleAdapter();
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCompositeSubscription.unsubscribe();
    }
}

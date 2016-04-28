package rxjava.droid.retrofit;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import rxjava.droid.R;
import rxjava.droid.retrofit.beans.UserWithContributor;

/**
 * Created by rohitsingh on 28/04/16.
 */
public class RetrofitExampleAdapter extends RecyclerView.Adapter<RetrofitExampleAdapter.ContributorViewHolder> {

    List<UserWithContributor> list = new ArrayList<>();
    String reponame;

    public void setReponame(String reponame) {
        this.reponame = reponame;
    }

    public void add(UserWithContributor userWithContributor) {
        list.add(userWithContributor);
    }

    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }

    @Override
    public ContributorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_git_contributors, parent, false);
        return new ContributorViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onBindViewHolder(ContributorViewHolder holder, int position) {
        holder.updateView(list.get(position));
    }



    class ContributorViewHolder extends RecyclerView.ViewHolder {
        TextView nameTv, emailTv, detailsTv;

        public ContributorViewHolder(View itemView) {
            super(itemView);
            nameTv = (TextView) itemView.findViewById(R.id.name_tv);
            emailTv = (TextView) itemView.findViewById(R.id.email_tv);
            detailsTv = (TextView) itemView.findViewById(R.id.details_tv);
        }

        public void updateView(UserWithContributor userWithContributor) {
            nameTv.setText(userWithContributor.user.name);
            emailTv.setText(userWithContributor.user.email);
            detailsTv.setText("Made "+ userWithContributor.contributor.contributions + "contributions to "+reponame);
        }
    }
}

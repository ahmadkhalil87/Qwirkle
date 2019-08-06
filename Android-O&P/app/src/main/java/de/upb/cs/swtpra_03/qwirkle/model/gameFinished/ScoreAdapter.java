package de.upb.cs.swtpra_03.qwirkle.model.gameFinished;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.upb.cs.swtpra_03.qwirkle.R;

public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ScoreViewHolder> {


    public class ScoreViewHolder extends RecyclerView.ViewHolder {

        TextView username;
        TextView scoreTime;

        public ScoreViewHolder(View itemView) {
            super(itemView);

            username = (TextView) itemView.findViewById(R.id.username);
            scoreTime = (TextView) itemView.findViewById(R.id.points_and_time);
        }
    }


    private final List<Player> models;

    public ScoreAdapter(List<Player> models) {
        this.models = models;
    }

    @NonNull
    @Override
    public ScoreViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.recycleritem_score, viewGroup, false);
        return new ScoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreViewHolder scoreViewHolder, int i) {
        Player playerViewModel = models.get(i);
        scoreViewHolder.username.setText((CharSequence) models.get(i).getName());
        String scores = scoreViewHolder.username.getContext().getString(
                R.string.scores,
                models.get(i).getScore(),
                models.get(i).getMin(),
                models.get(i).getSec()
        );
        scoreViewHolder.scoreTime.setText(scores);
    }

    @Override
    public int getItemCount() {
        return models.size();
    }
}

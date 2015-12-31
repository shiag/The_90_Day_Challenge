package com.sgmasterappsgmail.The90DayChallenge.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.sgmasterappsgmail.The90DayChallenge.R;
import com.sgmasterappsgmail.The90DayChallenge.models.TodoDaily;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by shia on 11/2/2015.
 */
public class TodoDailyAdepter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<TodoDaily> dailyGoalsToday = new ArrayList<>();
    public static final int TODAY = 0;
    public static final int ARCHIVE = 1;
    public static final int HELP = 2;
    public static final int NOT_DONE = 3;
    public static final int EDIT = 1;
    public static final int DONE = 2;
    public static final int HELP_B = 3;
    public static final int NOT_DONE_B = 4;
    public static final int SHIFT = 5;
    public static final int UP = 6;
    public static final int DOWN = 7;
    private ItemClickListener itemClickListener;
    private static int yellow = Color.parseColor("#f9ce1e");
    private static int notDone = Color.parseColor("#c6949c");
    private static int help = Color.parseColor("#c6b194");
    private static int done = Color.parseColor("#b5c694");
    private static int primaryColor = Color.parseColor("#8f3e97");
    private Context context;
    private final int layoutToInflate;
    private Date date;

    public TodoDailyAdepter(Context context, int layoutToInflate, ItemClickListener listener) {
        this.context = context;
        itemClickListener = listener;
        this.layoutToInflate = layoutToInflate;
    }

    private final ClickListener clickListener = new ClickListener() {
        @Override
        public void onItemClicked(int position, int icon) {
            TodoDaily daily = dailyGoalsToday.get(position);
            itemClickListener.onItemClicked(daily, icon);
        }
    };

    public void swapToday(List<TodoDaily> newList) {
        this.dailyGoalsToday = newList;
        Collections.sort(dailyGoalsToday);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        switch (layoutToInflate) {
            case TODAY:
                return TODAY;
            case ARCHIVE:
                return ARCHIVE;
            case HELP:
                return HELP;
            case NOT_DONE:
                return NOT_DONE;
            default:
                return dailyGoalsToday.size();
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TODAY:
                return new TodayViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_daily, parent, false), clickListener);
            case ARCHIVE:
                return new ArchiveHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.archive_layout, parent, false), clickListener);
            case HELP:
                return new HelpHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.help_layout, parent, false), clickListener);
            case NOT_DONE:
                return new NotDoneHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.not_done_layout, parent, false), clickListener);
            default:
                return null;
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TodayViewHolder) {
            TodayViewHolder todayHolder = (TodayViewHolder) holder;
            TodoDaily daily = dailyGoalsToday.get(position);
            date = new Date(daily.getDate());
            String todayDate = new SimpleDateFormat("EEE, MMM d yyyy", Locale.ENGLISH).format(date);

            if (position % 3 == 0)
                todayHolder.header.setVisibility(View.VISIBLE);
            else
                todayHolder.header.setVisibility(View.GONE);
            todayHolder.goel.setText(daily.getGoal());
            todayHolder.tittle.setText(daily.getTittle());
            todayHolder.description.setText(daily.getDescription());
            todayHolder.date.setText(todayDate);
            todayHolder.edit.setImageDrawable(setIcons(0));
            todayHolder.done.setImageDrawable(setIcons(1));
            todayHolder.help.setImageDrawable(setIcons(2));
            todayHolder.notDone.setImageDrawable(setIcons(3));
            todayHolder.shiftForTom.setImageDrawable(setIcons(4));
            todayHolder.cal.setImageDrawable(setIcons(8));
        } else if (holder instanceof ArchiveHolder) {
            ArchiveHolder archiveHolder = (ArchiveHolder) holder;
            TodoDaily daily = dailyGoalsToday.get(position);
            date = new Date(daily.getDate());
            String todayDate = new SimpleDateFormat("EEE, MMM d yyyy", Locale.ENGLISH).format(date);
            if (position % 3 == 0) {
                archiveHolder.header.setVisibility(View.VISIBLE);
            } else
                archiveHolder.header.setVisibility(View.GONE);

            archiveHolder.goal.setText(daily.getGoal());
            archiveHolder.tittle.setText(daily.getTittle());
            archiveHolder.description.setText(daily.getDescription());
            archiveHolder.date.setText(todayDate);

            archiveHolder.helpD.setImageDrawable(setIcons(2));
            archiveHolder.notDoneD.setImageDrawable(setIcons(3));
            archiveHolder.shiftForTomD.setImageDrawable(setIcons(4));

            archiveHolder.doneH.setImageDrawable(setIcons(1));
            archiveHolder.notDoneH.setImageDrawable(setIcons(3));
            archiveHolder.shiftForTomH.setImageDrawable(setIcons(4));

            archiveHolder.doneN.setImageDrawable(setIcons(1));
            archiveHolder.helpN.setImageDrawable(setIcons(2));
            archiveHolder.shiftForTomN.setImageDrawable(setIcons(4));

            if (daily.isHide()) {
                archiveHolder.description.setVisibility(View.GONE);
                archiveHolder.down.setVisibility(View.GONE);
                archiveHolder.up.setVisibility(View.VISIBLE);
            } else {
                archiveHolder.description.setVisibility(View.VISIBLE);
                archiveHolder.down.setVisibility(View.VISIBLE);
                archiveHolder.up.setVisibility(View.GONE);
            }
            //for display of the right image
            if (daily.isDone()) {
                archiveHolder.linNotDone.setVisibility(View.GONE);
                archiveHolder.linHelp.setVisibility(View.GONE);
                archiveHolder.linDone.setVisibility(View.VISIBLE);
                archiveHolder.checkIfDone.setImageDrawable(setIcons(5));
            } else if (daily.isHelp()) {
                archiveHolder.linNotDone.setVisibility(View.GONE);
                archiveHolder.linHelp.setVisibility(View.VISIBLE);
                archiveHolder.linDone.setVisibility(View.GONE);
                archiveHolder.checkIfDone.setImageDrawable(setIcons(6));
            } else if (daily.isNotDone()) {
                archiveHolder.linNotDone.setVisibility(View.VISIBLE);
                archiveHolder.linHelp.setVisibility(View.GONE);
                archiveHolder.linDone.setVisibility(View.GONE);
                archiveHolder.checkIfDone.setImageDrawable(setIcons(7));
            } else {
                archiveHolder.linNotDone.setVisibility(View.GONE);
                archiveHolder.linHelp.setVisibility(View.GONE);
                archiveHolder.linDone.setVisibility(View.VISIBLE);
                archiveHolder.checkIfDone.setImageDrawable(null);
            }
            archiveHolder.cal.setImageDrawable(setIcons(8));
        } else if (holder instanceof HelpHolder) {
            HelpHolder helpHolder = (HelpHolder) holder;
            final TodoDaily daily = dailyGoalsToday.get(position);
            String time = DateUtils.getRelativeTimeSpanString(daily.getDate()).toString();
            helpHolder.timeAgo.setText(time);
            helpHolder.tittle.setText(daily.getTittle());
            helpHolder.description.setText(daily.getDescription());
            helpHolder.done.setImageDrawable(setIcons(1));
            helpHolder.notDone.setImageDrawable(setIcons(3));
            helpHolder.shiftForTom.setImageDrawable(setIcons(4));
            helpHolder.helpNeed.setImageDrawable(setIcons(6));
            if (daily.isHide()) {
                helpHolder.description.setVisibility(View.GONE);
                helpHolder.down.setVisibility(View.GONE);
                helpHolder.up.setVisibility(View.VISIBLE);
            } else {
                helpHolder.description.setVisibility(View.VISIBLE);
                helpHolder.down.setVisibility(View.VISIBLE);
                helpHolder.up.setVisibility(View.GONE);
            }

        } else if (holder instanceof NotDoneHolder) {
            NotDoneHolder notDoneHolder = (NotDoneHolder) holder;
            TodoDaily daily = dailyGoalsToday.get(position);
            String time = DateUtils.getRelativeTimeSpanString(daily.getDate()).toString();
            notDoneHolder.timeAgo.setText(time);
            notDoneHolder.tittle.setText(daily.getTittle());
            notDoneHolder.description.setText(daily.getDescription());
            notDoneHolder.done.setImageDrawable(setIcons(1));
            notDoneHolder.help.setImageDrawable(setIcons(2));
            notDoneHolder.shiftForTom.setImageDrawable(setIcons(4));
            notDoneHolder.notDoneImage.setImageDrawable(setIcons(7));
            if (daily.isHide()) {
                notDoneHolder.description.setVisibility(View.GONE);
                notDoneHolder.down.setVisibility(View.GONE);
                notDoneHolder.up.setVisibility(View.VISIBLE);
            } else {
                notDoneHolder.description.setVisibility(View.VISIBLE);
                notDoneHolder.down.setVisibility(View.VISIBLE);
                notDoneHolder.up.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return dailyGoalsToday.size();
    }

    public Drawable setIcons(int whatIcon) {
        int[] imageResId = {
                R.drawable.ic_action_content_create,
                R.drawable.ic_action_toggle_check_box,
                R.drawable.ic_action_maps_location_history,
                R.drawable.ic_action_content_report,
                R.drawable.ic_action_content_undo,
                R.drawable.ic_action_toggle_check_box,
                R.drawable.ic_action_maps_location_history,
                R.drawable.ic_action_content_report,
                R.drawable.ic_action_action_event
        };
        Drawable drawable = ContextCompat.getDrawable(context, imageResId[whatIcon]);
        if (drawable != null) {
            drawable.mutate();
            switch (whatIcon) {
                case 0:
                    drawable.setColorFilter(primaryColor, PorterDuff.Mode.SRC_ATOP);
                    break;
                case 1:
                    drawable.setColorFilter(primaryColor, PorterDuff.Mode.SRC_ATOP);
                    break;
                case 2:
                    drawable.setColorFilter(primaryColor, PorterDuff.Mode.SRC_ATOP);
                    break;
                case 3:
                    drawable.setColorFilter(primaryColor, PorterDuff.Mode.SRC_ATOP);
                    break;
                case 4:
                    drawable.setColorFilter(primaryColor, PorterDuff.Mode.SRC_ATOP);
                    break;
                case 5:
                    drawable.setColorFilter(done, PorterDuff.Mode.SRC_ATOP);
                    break;
                case 6:
                    drawable.setColorFilter(help, PorterDuff.Mode.SRC_ATOP);
                    break;
                case 7:
                    drawable.setColorFilter(notDone, PorterDuff.Mode.SRC_ATOP);
                    break;
                case 8:
                    drawable.setColorFilter(yellow, PorterDuff.Mode.SRC_ATOP);
                    break;

            }
        }
        return drawable;

    }

    public static class TodayViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView edit, done, help, notDone, shiftForTom, cal;
        private TextView tittle, description, date, goel;
        private CardView header;
        ClickListener clickListener;

        public TodayViewHolder(View itemView, ClickListener clickListener) {
            super(itemView);
            cal = (ImageView) itemView.findViewById(R.id.calender);
            header = (CardView) itemView.findViewById(R.id.include_card);
            tittle = (TextView) itemView.findViewById(R.id.tittle);
            goel = (TextView) itemView.findViewById(R.id.goal);
            date = (TextView) itemView.findViewById(R.id.date);
            description = (TextView) itemView.findViewById(R.id.description);
            edit = (ImageView) itemView.findViewById(R.id.edit);
            done = (ImageView) itemView.findViewById(R.id.done);
            help = (ImageView) itemView.findViewById(R.id.help);
            notDone = (ImageView) itemView.findViewById(R.id.not_done);
            shiftForTom = (ImageView) itemView.findViewById(R.id.send_to_tom);
            edit.setOnClickListener(this);
            done.setOnClickListener(this);
            help.setOnClickListener(this);
            notDone.setOnClickListener(this);
            shiftForTom.setOnClickListener(this);
            this.clickListener = clickListener;
        }

        @Override
        public void onClick(View v) {
            int icon = 0;
            if (v == edit) {
                icon = EDIT;
            } else if (v == done) {
                icon = DONE;
            } else if (v == help) {
                icon = HELP_B;
            } else if (v == notDone) {
                icon = NOT_DONE_B;
            } else if (v == shiftForTom) {
                icon = SHIFT;
            }
            clickListener.onItemClicked(getAdapterPosition(), icon);
        }

    }

    public static class ArchiveHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView helpD, notDoneD, shiftForTomD, cal, checkIfDone, doneH, notDoneH, shiftForTomH, doneN, helpN, shiftForTomN, up, down;
        private TextView tittle, description, date, goal;
        private View header;
        private LinearLayout linDone, linHelp, linNotDone;
        ClickListener clickListener;

        public ArchiveHolder(View itemView, ClickListener clickListener) {
            super(itemView);
            linDone = (LinearLayout) itemView.findViewById(R.id.lin_done);
            linHelp = (LinearLayout) itemView.findViewById(R.id.lin_help);
            linNotDone = (LinearLayout) itemView.findViewById(R.id.lin_not_done);
            tittle = (TextView) itemView.findViewById(R.id.tittle);
            cal = (ImageView) itemView.findViewById(R.id.calender);
            checkIfDone = (ImageView) itemView.findViewById(R.id.check_if_done);
            goal = (TextView) itemView.findViewById(R.id.goal);
            date = (TextView) itemView.findViewById(R.id.date);
            description = (TextView) itemView.findViewById(R.id.description);
            doneN = (ImageView) itemView.findViewById(R.id.done_n);
            doneH = (ImageView) itemView.findViewById(R.id.done_h);
            helpD = (ImageView) itemView.findViewById(R.id.help_d);
            helpN = (ImageView) itemView.findViewById(R.id.help_n);
            notDoneD = (ImageView) itemView.findViewById(R.id.not_done_d);
            notDoneH = (ImageView) itemView.findViewById(R.id.not_done_h);
            shiftForTomD = (ImageView) itemView.findViewById(R.id.send_to_tom_d);
            shiftForTomH = (ImageView) itemView.findViewById(R.id.send_to_tom_h);
            shiftForTomN = (ImageView) itemView.findViewById(R.id.send_to_tom_n);
            up = (ImageView) itemView.findViewById(R.id.up_image);
            down = (ImageView) itemView.findViewById(R.id.down_image);
            header = itemView.findViewById(R.id.include);
            helpD.setOnClickListener(this);
            notDoneD.setOnClickListener(this);
            shiftForTomD.setOnClickListener(this);
            doneH.setOnClickListener(this);
            notDoneH.setOnClickListener(this);
            shiftForTomH.setOnClickListener(this);
            doneN.setOnClickListener(this);
            helpN.setOnClickListener(this);
            shiftForTomN.setOnClickListener(this);
            up.setOnClickListener(this);
            down.setOnClickListener(this);
            this.clickListener = clickListener;
        }

        @Override
        public void onClick(View v) {
            int icon = 0;
            if (v == doneN || v == doneH) {
                icon = DONE;
            } else if (v == helpD || v == helpN) {
                icon = HELP_B;
            } else if (v == notDoneD || v == notDoneH) {
                icon = NOT_DONE_B;
            } else if (v == shiftForTomD || v == shiftForTomH || v == shiftForTomN) {
                icon = SHIFT;
            } else if (v == up) {
                icon = UP;
            } else if (v == down) {
                icon = DOWN;
            }
            clickListener.onItemClicked(getAdapterPosition(), icon);
        }

    }

    public static class HelpHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView done, notDone, shiftForTom, helpNeed, up, down;
        private TextView tittle, description, timeAgo;
        ClickListener clickListener;

        public HelpHolder(View itemView, ClickListener clickListener) {
            super(itemView);
            tittle = (TextView) itemView.findViewById(R.id.tittle);
            description = (TextView) itemView.findViewById(R.id.description);
            timeAgo = (TextView) itemView.findViewById(R.id.time_ago);
            done = (ImageView) itemView.findViewById(R.id.done);
            up = (ImageView) itemView.findViewById(R.id.up_image);
            down = (ImageView) itemView.findViewById(R.id.down_image);
            notDone = (ImageView) itemView.findViewById(R.id.not_done);
            helpNeed = (ImageView) itemView.findViewById(R.id.help_need);
            shiftForTom = (ImageView) itemView.findViewById(R.id.send_to_tom);
            done.setOnClickListener(this);
            notDone.setOnClickListener(this);
            shiftForTom.setOnClickListener(this);
            up.setOnClickListener(this);
            down.setOnClickListener(this);
            this.clickListener = clickListener;
        }

        @Override
        public void onClick(View v) {
            int icon = 0;
            if (v == done) {
                icon = DONE;
            } else if (v == notDone) {
                icon = NOT_DONE_B;
            } else if (v == shiftForTom) {
                icon = SHIFT;
            } else if (v == up) {
                icon = UP;
            } else if (v == down) {
                icon = DOWN;
            }

            clickListener.onItemClicked(getAdapterPosition(), icon);
        }

    }

    public static class NotDoneHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView done, help, shiftForTom, notDoneImage, up, down;
        private TextView tittle, description, timeAgo;
        ClickListener clickListener;

        public NotDoneHolder(View itemView, ClickListener clickListener) {
            super(itemView);
            timeAgo = (TextView) itemView.findViewById(R.id.time_ago);
            notDoneImage = (ImageView) itemView.findViewById(R.id.notdone_image);
            tittle = (TextView) itemView.findViewById(R.id.tittle);
            description = (TextView) itemView.findViewById(R.id.description);
            done = (ImageView) itemView.findViewById(R.id.help);
            help = (ImageView) itemView.findViewById(R.id.not_done);
            shiftForTom = (ImageView) itemView.findViewById(R.id.send_to_tom);
            up = (ImageView) itemView.findViewById(R.id.up_image);
            down = (ImageView) itemView.findViewById(R.id.down_image);
            done.setOnClickListener(this);
            help.setOnClickListener(this);
            shiftForTom.setOnClickListener(this);
            up.setOnClickListener(this);
            down.setOnClickListener(this);
            this.clickListener = clickListener;
        }

        @Override
        public void onClick(View v) {
            int icon = 0;
            if (v == done) {
                icon = DONE;
            } else if (v == help) {
                icon = HELP_B;
            } else if (v == shiftForTom) {
                icon = SHIFT;
            } else if (v == up) {
                icon = UP;
            } else if (v == down) {
                icon = DOWN;
            }
            clickListener.onItemClicked(getAdapterPosition(), icon);
        }
    }

    private interface ClickListener {
        void onItemClicked(int position, int icon);
    }


    public interface ItemClickListener {
        void onItemClicked(TodoDaily daily, int icon);
    }

}


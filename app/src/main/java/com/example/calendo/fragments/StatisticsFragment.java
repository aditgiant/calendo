package com.example.calendo.fragments;

import android.app.usage.UsageEvents;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.chart.common.listener.Event;
import com.anychart.chart.common.listener.ListenersInterface;
import com.anychart.charts.Pie;
import com.anychart.enums.Align;
import com.anychart.enums.LegendLayout;
import com.example.calendo.R;
import com.example.calendo.fragments.todolist.Task;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.example.calendo.utils.User.MY_PREFS_NAME;

public class StatisticsFragment extends Fragment {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String userID;
    AnyChartView anyChartView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_statistics, container, false);
        //Retrieve userID
        SharedPreferences sharedPref = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        this.userID = sharedPref.getString("userID", "NOUSERFOUND");
        anyChartView = view.findViewById(R.id.any_chart_view);
        anyChartView.setProgressBar(view.findViewById(R.id.progress_bar));

        renderChart();

        return view;
    }


    private void renderChart(){
        CollectionReference usersRef = db.collection("Users").document(this.userID).collection("list");

        usersRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshots) {
                        drawChart(querySnapshots);

                    }
                });
    }

    private void drawChart(QuerySnapshot querySnapshot){
        List<DataEntry> data = new ArrayList<>();
        int countCompleted=0;
        int countUncompleted=0;

        for(QueryDocumentSnapshot documentSnapshot: querySnapshot) {
            Task todolist = documentSnapshot.toObject(Task.class);

            String status = todolist.getStatus();
            if(status.equals("completed")){
                countCompleted++;
            }else if(status.equals("uncompleted")){
                countUncompleted++;
            }

        }

        data.add(new ValueDataEntry("Completed", countCompleted));
        data.add(new ValueDataEntry("UnCompleted", countUncompleted));

        Pie pie = AnyChart.pie();

        pie.data(data);
        pie.title("Completion Todo List");
        pie.labels().position("outside");

        pie.legend().title().enabled(true);
        pie.legend().title()
                .text("Status")
                .padding(0d, 0d, 10d, 0d);

        pie.legend()
                .position("center-bottom")
                .itemsLayout(LegendLayout.HORIZONTAL)
                .align(Align.CENTER);

        anyChartView.setChart(pie);

    }

}

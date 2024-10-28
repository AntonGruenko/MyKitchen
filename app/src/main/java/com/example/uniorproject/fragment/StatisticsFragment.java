package com.example.uniorproject.fragment;

import static android.text.format.DateUtils.DAY_IN_MILLIS;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.VolleyError;
import com.example.uniorproject.adapter.MealsAdapter;
import com.example.uniorproject.databinding.FragmentStatisticsBinding;
import com.example.uniorproject.domain.Day;
import com.example.uniorproject.domain.Meal;
import com.example.uniorproject.domain.User;
import com.example.uniorproject.domain.mapper.DayMapper;
import com.example.uniorproject.domain.mapper.UserMapper;
import com.example.uniorproject.noDb.NoDb;
import com.example.uniorproject.rest.VolleyAPI;
import com.example.uniorproject.rest.VolleyCallback;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class StatisticsFragment extends Fragment {

    private FragmentStatisticsBinding binding;
    private SharedPreferences sharedPreferences;
    private User user;
    private Calendar currentCalendar;
    private  long registrationPeriod;
    private int daysFromRegistration;
    private Day day;
    private Calendar calendar;
    private Calendar userCalendar;
    private SharedPreferences.Editor editor;
    private MealsAdapter mealsAdapter;
    private Context context;
    private List<Day> chartDays;
    private ArrayList<BarEntry> barEntries;
    private ArrayList<String> labels;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStatisticsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        context = getContext();
        chartDays = new ArrayList<>();
        barEntries = new ArrayList<>();
        labels = new ArrayList<>();

        sharedPreferences = context.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);

        binding.mealsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        if(getActivity() != null) {
            new VolleyAPI(context).findUserByEmail(sharedPreferences.getString("userEmail", ""), new VolleyCallback() {
                @Override
                public void onSuccess(JSONObject response) {
                    user = UserMapper.userFromJson(response);
                    currentCalendar = Calendar.getInstance();
                    userCalendar = Calendar.getInstance();
                    userCalendar.setTimeInMillis(user.getRegistrationDate());
                    Calendar comparisonCalendar = Calendar.getInstance();
                    comparisonCalendar.setTimeInMillis(user.getRegistrationDate());

                    daysFromRegistration = currentCalendar.get(Calendar.DAY_OF_YEAR) - userCalendar.get(Calendar.DAY_OF_YEAR);

                    calendar = Calendar.getInstance();
                    binding.date.setText(String.format(Locale.ENGLISH, "%1$td.%1$tm.%1$tY", calendar));

                    new VolleyAPI(context).findDaysByUserAndDay(user.getId(), daysFromRegistration, new VolleyCallback() {
                        @Override
                        public void onSuccess(JSONObject response) {
                            changeDate(response, calendar);

                            new VolleyAPI(context).findMealsByUserAndDay(user.getId(), day.getId(), new VolleyCallback() {
                                @Override
                                public void onSuccess(JSONObject response) {
                                    mealsAdapter = new MealsAdapter(context, NoDb.MEALS_LIST, new MealsAdapter.MealsCallback() {
                                        @Override
                                        public void onCall(JSONObject response) {
                                            Day responseDay = DayMapper.dayFromJson(response);
                                            clearDays();
                                            fillDays(responseDay.getDay(), user);
                                            changeDate(response, calendar);
                                        }
                                    }, day);
                                    fillDays(day.getDay(), user);
                                    binding.mealsRecyclerView.setAdapter(mealsAdapter);
                                }

                                @Override
                                public void onError(VolleyError error) {

                                }
                            });
                        }

                        @Override
                        public void onError(VolleyError error) {
                            binding.kcalRemain.setText(String.valueOf(user.getKcal()));
                            binding.proteinsRemain.setText(String.valueOf(user.getProteins()));
                            binding.fatsRemain.setText(String.valueOf(user.getFats()));
                            binding.carbohydratesRemain.setText(String.valueOf(user.getCarbohydrates()));
                        }
                    });

                }

                @Override
                public void onError(VolleyError error) {

                }
            });
        }

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.add(Calendar.DAY_OF_YEAR, -1);
                --daysFromRegistration;
                clearDays();
                if(daysFromRegistration >= 0 && getActivity() != null) {
                    new VolleyAPI(context).findDaysByUserAndDay(user.getId(), daysFromRegistration, new VolleyCallback() {
                        @Override
                        public void onSuccess(JSONObject response) {
                            changeDate(response, calendar);

                            new VolleyAPI(context).findMealsByUserAndDay(user.getId(), day.getId(), new VolleyCallback() {
                                @Override
                                public void onSuccess(JSONObject response) {
                                    binding.mealsRecyclerView.setVisibility(View.VISIBLE);
                                    binding.mealsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
                                    fillDays(day.getDay(), user);
                                    mealsAdapter = new MealsAdapter(context, NoDb.MEALS_LIST, new MealsAdapter.MealsCallback() {
                                        @Override 
                                        public void onCall(JSONObject response) {
                                            Day responseDay = DayMapper.dayFromJson(response);
                                            clearDays();
                                            fillDays(responseDay.getDay(), user);
                                            changeDate(response, calendar);
                                        }
                                    }, day);
                                    binding.mealsRecyclerView.setAdapter(mealsAdapter);
                                }

                                @Override
                                public void onError(VolleyError error) {
                                    binding.mealsRecyclerView.setVisibility(View.GONE);
                                    binding.chart.setVisibility(View.GONE);
                                }
                            });
                        }

                        @Override
                        public void onError(VolleyError error) {
                            if(error.networkResponse.statusCode == 500){
                                binding.kcalProgressBar.setProgress(0);
                                binding.date.setText(String.format(Locale.ENGLISH, "%1$td.%1$tm.%1$tY", calendar));
                                binding.kcalRemain.setText(String.valueOf(user.getKcal()));
                                binding.proteinsRemain.setText(String.valueOf(user.getProteins()));
                                binding.fatsRemain.setText(String.valueOf(user.getFats()));
                                binding.carbohydratesRemain.setText(String.valueOf(user.getCarbohydrates()));
                                binding.chart.setVisibility(View.GONE);
                                clearDays();
                            }
                        }
                    });
                }

                else{
                    calendar.add(Calendar.DAY_OF_YEAR, 1);
                    ++daysFromRegistration;
                }
            }
        });

        binding.forwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                Calendar compareCalendar = Calendar.getInstance();
                compareCalendar.set(Calendar.HOUR, 0);
                compareCalendar.set(Calendar.MINUTE, 0);
                compareCalendar.set(Calendar.SECOND, 0);
                if(calendar.get(Calendar.DAY_OF_YEAR) <= compareCalendar.get(Calendar.DAY_OF_YEAR)) {
                    ++daysFromRegistration;
                    clearDays();
                    new VolleyAPI(getContext()).findDaysByUserAndDay(user.getId(), daysFromRegistration, new VolleyCallback() {
                        @Override
                        public void onSuccess(JSONObject response) {
                            changeDate(response, calendar);

                            new VolleyAPI(getContext()).findMealsByUserAndDay(user.getId(), day.getId(), new VolleyCallback() {
                                @Override
                                public void onSuccess(JSONObject response) {
                                    binding.mealsRecyclerView.setVisibility(View.VISIBLE);
                                    binding.mealsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                    fillDays(day.getDay(), user);
                                    mealsAdapter = new MealsAdapter(getContext(), NoDb.MEALS_LIST, new MealsAdapter.MealsCallback() {
                                        @Override
                                        public void onCall(JSONObject response) {
                                            Day responseDay = DayMapper.dayFromJson(response);
                                            clearDays();
                                            fillDays(responseDay.getDay(), user);
                                            changeDate(response, calendar);
                                        }
                                    }, day);
                                    binding.mealsRecyclerView.setAdapter(mealsAdapter);

                                }

                                @Override
                                public void onError(VolleyError error) {
                                    binding.mealsRecyclerView.setVisibility(View.GONE);
                                }
                            });
                        }

                        @Override
                        public void onError(VolleyError error) {
                            if (error.networkResponse.statusCode == 500) {
                                binding.kcalRemain.setText(String.valueOf(user.getKcal()));
                                binding.proteinsRemain.setText(String.valueOf(user.getProteins()));
                                binding.fatsRemain.setText(String.valueOf(user.getFats()));
                                binding.carbohydratesRemain.setText(String.valueOf(user.getCarbohydrates()));
                                binding.date.setText(String.format(Locale.ENGLISH, "%1$td.%1$tm.%1$tY", calendar));
                                binding.chart.setVisibility(View.GONE);
                                binding.mealsRecyclerView.setVisibility(View.GONE);
                                clearDays();
                            }
                        }
                    });
                }
                else{
                    calendar.add(Calendar.DAY_OF_YEAR, -1);
                }
            }
        });


        return view;
    }

    private void changeDate(JSONObject response, Calendar calendar){
        binding.chart.setVisibility(View.VISIBLE);
        binding.mealsRecyclerView.setVisibility(View.VISIBLE);
        day = DayMapper.dayFromJson(response);
        binding.date.setText(String.format(Locale.ENGLISH, "%1$td.%1$tm.%1$tY", calendar));
        double progress = day.getKcal() * 1.0 / user.getKcal();
        int kcalRemain = user.getKcal() - day.getKcal();
        int proteinsRemain = user.getProteins() - day.getProteins();
        int fatsRemain = user.getFats() - day.getFats();
        int carbohydratesRemain = user.getCarbohydrates() - day.getCarbohydrates();
        binding.kcalProgressBar.setProgress((int) (progress * 100));
        if(kcalRemain < 0){
            kcalRemain *= -1;
            binding.caloriesRemain.setText("Ккал превышено");
        }
        else{
            binding.caloriesRemain.setText("Ккал осталось");
        }
        if(proteinsRemain < 0){
            proteinsRemain *= -1;
            binding.proteins.setText("Белков превышено");
        }
        else{
            binding.proteins.setText("Белки");
        }
        if(fatsRemain < 0){
            fatsRemain *= -1;
            binding.fats.setText("Жиров превышено");
        }
        else{
            binding.fats.setText("Жиры");
        }
        if(carbohydratesRemain < 0){
            carbohydratesRemain *= -1;
            binding.carbohydrates.setText("Углеводов превышено");
        }
        else{
            binding.carbohydrates.setText("Углеводы");
        }
        binding.kcalRemain.setText(String.valueOf(kcalRemain));
        binding.proteinsRemain.setText(String.valueOf(proteinsRemain));
        binding.fatsRemain.setText(String.valueOf(fatsRemain));
        binding.carbohydratesRemain.setText(String.valueOf(carbohydratesRemain));
    }

    private void fillDays(int day, User currentUser){
        new VolleyAPI(context).findDaysByUser(currentUser.getId(), new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                chartDays.clear();
                for (int i = NoDb.DAYS_BY_USER_LIST.size() - 1; i >= 0; --i){
                    if(NoDb.DAYS_BY_USER_LIST.get(i).getDay() == day){
                        for(int j = i - 6; j <= i; ++j){
                            if(j >= 0) {
                                chartDays.add(NoDb.DAYS_BY_USER_LIST.get(j));
                            }
                            else {
                                chartDays.add(new Day(currentUser, 0,0,0,0,0, false));
                            }
                        }
                        break;
                    }
                }

                for (int i = 0; i < chartDays.size(); i++){
                    int kcal = chartDays.get(i).getKcal();
                    barEntries.add(new BarEntry(i, kcal));
                }

                BarDataSet barDataSet = new BarDataSet(barEntries, "");
                /*barDataSet.setColors(colorArray);*/
                Description description = new Description();
                description.setText("Дни");
                binding.chart.setDescription(description);
                BarData barData = new BarData(barDataSet);
                binding.chart.setData(barData);

                LimitLine ll = new LimitLine(user.getKcal());
                XAxis xAxis = binding.chart.getXAxis();
                xAxis.setPosition(XAxis.XAxisPosition.TOP);
                xAxis.setDrawGridLines(false);
                xAxis.setDrawAxisLine(false);
                xAxis.setGranularity(1f);
                xAxis.setLabelCount(chartDays.size());
                YAxis yAxis = binding.chart.getAxisLeft();
                yAxis.addLimitLine(ll);
                binding.chart.animateY(500);
            }

            @Override
            public void onError(@Nullable VolleyError error) {

            }
        });

    }

    private void clearDays(){
        barEntries.clear();
        BarDataSet barDataSet = new BarDataSet(barEntries, "");
        BarData barData = new BarData(barDataSet);
        binding.chart.setData(barData);
    }

}

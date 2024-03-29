package com.example.uniorproject.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.ContentInfo;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.example.uniorproject.R;
import com.example.uniorproject.adapter.RecipeTagsAdapter;
import com.example.uniorproject.adapter.SelectedTagsAdapter;
import com.example.uniorproject.databinding.FragmentSetRecipeTagsBinding;
import com.example.uniorproject.noDb.NoDb;

public class SetRecipeTagsFragment extends Fragment {
    SelectedTagsAdapter selectedTagsAdapter;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentSetRecipeTagsBinding binding = FragmentSetRecipeTagsBinding.inflate(inflater, null, false);
        View view = binding.getRoot();
        context = getContext();

        NoDb.TAG_LIST.clear();
        NoDb.TAG_LIST.add("Веганское");
        NoDb.TAG_LIST.add("Вегетарианское");
        NoDb.TAG_LIST.add("Дешевое");
        NoDb.TAG_LIST.add("Дорогое");
        NoDb.TAG_LIST.add("Острое");
        NoDb.TAG_LIST.add("Халяль");
        NoDb.TAG_LIST.add("В пост");
        NoDb.TAG_LIST.add("Завтрак");
        NoDb.TAG_LIST.add("Обед");
        NoDb.TAG_LIST.add("Ужин");
        NoDb.TAG_LIST.add("Первое");
        NoDb.TAG_LIST.add("Второе");
        NoDb.TAG_LIST.add("Напиток");
        NoDb.TAG_LIST.add("ЗОЖ");
        NoDb.TAG_LIST.add("Русская кухня");
        NoDb.TAG_LIST.add("Французская кухня");
        NoDb.TAG_LIST.add("Итальянская кухня");
        NoDb.TAG_LIST.add("Азиатская кухня");
        NoDb.TAG_LIST.add("Сладкое");
        NoDb.TAG_LIST.add("Морепродкуты");

        RecipeTagsAdapter adapter = new RecipeTagsAdapter(context, NoDb.TAG_LIST);
        binding.recommendedTags.setLayoutManager(new GridLayoutManager(context, 3));
        binding.recommendedTags.setAdapter(adapter);

        selectedTagsAdapter = new SelectedTagsAdapter(context, NoDb.SELECTED_TAG_LIST);
        binding.selectedTags.setLayoutManager(new LinearLayoutManager(context));
        binding.selectedTags.setAdapter(selectedTagsAdapter);

        binding.editTag.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_NEXT && !NoDb.SELECTED_TAG_LIST.contains(binding.editTag.getText().toString())){
                    NoDb.SELECTED_TAG_LIST.add(
                            binding.editTag.getText().toString()
                    );

                    selectedTagsAdapter.notifyItemInserted(NoDb.SELECTED_TAG_LIST.size());
                }
                return false;
            }
        });

        binding.buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.create_container, new CreateRecipeGuideFragment(), "setGuide")
                        .commit();
            }
        });
        
        binding.buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.create_container, new SetRecipeNutritionalValueFragment(), "setNutrition")
                        .commit();
            }
        });

        return view;
    }

    public void updateAdapter(){
        selectedTagsAdapter.notifyDataSetChanged();
    }
}
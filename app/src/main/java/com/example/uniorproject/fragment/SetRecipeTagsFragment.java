package com.example.uniorproject.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.example.uniorproject.R;
import com.example.uniorproject.adapter.RecipeCreatorAdapter;
import com.example.uniorproject.adapter.RecipeTagsAdapter;
import com.example.uniorproject.adapter.SelectedTagsAdapter;
import com.example.uniorproject.databinding.FragmentSetRecipeTagsBinding;
import com.example.uniorproject.noDb.NoDb;

public class SetRecipeTagsFragment extends Fragment {
    SelectedTagsAdapter selectedTagsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentSetRecipeTagsBinding binding = FragmentSetRecipeTagsBinding.inflate(inflater, null, false);
        View view = binding.getRoot();
        NoDb.TAG_LIST.clear();
        NoDb.TAG_LIST.add("Веганское");
        NoDb.TAG_LIST.add("Вегетарианское");
        NoDb.TAG_LIST.add("Дешевое");
        NoDb.TAG_LIST.add("Дорогое");
        NoDb.TAG_LIST.add("Острое");
        NoDb.TAG_LIST.add("Халяль");
        NoDb.TAG_LIST.add("В пост");

        RecipeTagsAdapter adapter = new RecipeTagsAdapter(getActivity(), NoDb.TAG_LIST);
        binding.recommendedTags.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        binding.recommendedTags.setAdapter(adapter);

        selectedTagsAdapter = new SelectedTagsAdapter(getActivity(), NoDb.SELECTED_TAG_LIST);
        binding.selectedTags.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.selectedTags.setAdapter(selectedTagsAdapter);

        binding.editTag.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_NEXT){
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
                        .replace(R.id.create_container, new SetRecipeNutritionalValue(), "setNutrition")
                        .commit();
            }
        });

        return view;
    }

    public void updateAdapter(){
        selectedTagsAdapter.notifyDataSetChanged();
    }
}
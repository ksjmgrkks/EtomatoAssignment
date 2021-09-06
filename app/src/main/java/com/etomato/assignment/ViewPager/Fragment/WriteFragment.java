package com.etomato.assignment.ViewPager.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.etomato.assignment.R;

public class WriteFragment extends Fragment {

    // TODO: 매개변수와 인수 이름 변경하기, 일치하는 이름 선택하기
    // 프래그먼트 초기화 매개변수, ex) ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: 매개변수 이름 변경 및 데이터 타입 변경하기
    private String mParam1;
    private String mParam2;

    public WriteFragment() {
        // 비어있는 public 생성자가 필요함
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * 제공된 매개변수를 이용한 이 프래그먼트의 새로운 인스턴스를 생성하기 위해
     * 이 팩토리 메서드를 사용하세요.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WriteFragment.
     */
    // TODO: 매개변수 변경해주기
    public static WriteFragment newInstance(String param1, String param2) {
        WriteFragment fragment = new WriteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_write, container, false);
    }
}
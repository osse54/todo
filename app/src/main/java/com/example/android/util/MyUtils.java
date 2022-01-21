package com.example.android.util;
import java.util.*;

import android.content.Intent;
import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.android.activities.MainActivity;
import com.example.android.dto.Todo;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;

public class MyUtils {

    private final static String PREF_NAME = "todoManager";

    private final static String TODO_LIST = "todoList";
    private final static String TODO_AMOUNT = "todoAmount";
    private final static String TODO_NO = "todoNo";

    private final static String ADDING_TODO_NAME = "addingTodoName";
    private final static String ADDING_TODO_CATEGORY = "addingTodoCategory";
    private final static String ADDING_TODO_EXPLAIN = "addingTodoExplain";
    private final static String ADDING_TODO_COMPLETE = "addingTodoComplete";
    private final static String ADDING_TODO_END = "addingTodoEnd";

    private final static String CATEGORY = "category";
    private final static String CATEGORY_COUNT = "categoryCount";

    private final static Gson gson = new Gson();

    // fragment 이동 시 마다 각 fragment가 가지는 adapter들을 할당해줘야 한다.
    public static RecyclerView.Adapter[] adapters;
    private static MainActivity MAIN_ACTIVITY;

    public final static String DATE_PATTERN = "yyyyMMdd";
    public final static String SHOWN_DATE_PATTERN = "MM월 dd일";
    private static SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);

    private static HashSet<String> set;

    /**
     * CalendarFragment에서 달력을 누를 때 마다 날짜가 바뀜
     * 기본 선택은 바뀌지 않음
      */
    public static int year;
    public static int month;
    public static int day;

    public static SwipeRefreshLayout swipeRefreshLayout;

    // 해당 일정의 Intent를 자동 생성하여 return한다.
    public static Intent getIntent(Todo todo) {
        Intent intent = new Intent();
        intent.putExtra("no", todo.getNo());
        intent.putExtra("name", todo.getName());
        intent.putExtra("category", todo.getCategory());
        intent.putExtra("explain", todo.getExplain());
        intent.putExtra("complete", todo.isComplete());
        intent.putExtra("end", todo.getEnd());
        return intent;
    }

    // DATE_PATTERN과 같은 패턴으로 날짜를 String으로 반환
    public static String getNow() {
        return sdf.format(new Date());
    }

    public static String getStringFromDate(Date date) {
        return sdf.format(date);
    }

    public static String getStringFromDate(Date date, String pattern) {
        sdf.applyPattern(pattern);
        String strDate = sdf.format(date);
        sdf.applyPattern(DATE_PATTERN);
        return strDate;
    }

    public static int[] getNowToInt() {
        String now = getNow();
        return new int[]{Integer.parseInt(now.substring(0, 4)), Integer.parseInt(now.substring(4, 6)), Integer.parseInt(now.substring(6, 8))};
    }

    public static String getDateText(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day);
        return getDateText(calendar);
    }

    public static String getDateText(Calendar calendar) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int[] now = MyUtils.getNowToInt();
        String str = MyUtils.getStringFromDate(calendar.getTime(), SHOWN_DATE_PATTERN);
        if(now[0] == year && now[1] == month) {
            if(day == now[2]) {
                str = "오늘까지";
            } else if(day == now[2] + 1) {
                str = "내일까지";
            } else if(day == now[2] - 1) {
                str = "어제까지";
            }
        }
        return str;
    }

    public static int[] getDateToInt(String str) {
        return new int[]{Integer.parseInt(str.substring(0, 4)), Integer.parseInt(str.substring(4, 6)), Integer.parseInt(str.substring(6, 8))};
    }

    public static int[] getDateToInt(Calendar calendar) {
        return new int[]{calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)};
    }

    public static String getIntDateToStr(int[] date) {
        return date[0] + "" + date[1] + "" + date[2];
    }

    public static RecyclerView.Adapter[] getOtherAdapter(RecyclerView.Adapter adapter) {
        ArrayList<RecyclerView.Adapter> arr = new ArrayList<>();
        for(RecyclerView.Adapter temp : adapters) {
            if(!adapter.equals(temp)) {
                arr.add(temp);
            }
        }
        return arr.toArray(new RecyclerView.Adapter[0]);
    }

    public static MainActivity getMainActivity() {
        return MAIN_ACTIVITY;
    }

    public static void setMainActivity(MainActivity mainActivity) {
        MAIN_ACTIVITY = mainActivity;
    }

    public static void addTodo(Todo todo) {
        MAIN_ACTIVITY.getList().add(todo);
        saveList();
    }

    public static void updateTodo() {
        // instance를 수정하였기 때문에 굳이 list에 있는 instance에 접근하여 수정을 수행할 이유가 없다.
        saveList();
    }

    public static SharedPreferences getSharedPreference() {
        return PreferenceManager.getDefaultSharedPreferences(MAIN_ACTIVITY);
    }

    public static void saveList() {
        HashSet<String> set = new HashSet<>();
        for(Todo temp : MAIN_ACTIVITY.getList()) {
            String str = gson.toJson(temp);
            set.add(str);
        }
        SharedPreferences.Editor editor = getSharedPreference().edit();
        editor.putStringSet(TODO_LIST, set);
        editor.putInt(TODO_AMOUNT, set.size());
        editor.putLong(TODO_NO, Todo.NEXT);
        editor.apply();
    }

    public static void loadList() {
        if(MAIN_ACTIVITY.getList().size() != getSharedPreference().getInt(TODO_AMOUNT, -1)) {
            Collection<String> set = getSharedPreference().getStringSet(TODO_LIST, new HashSet<>());
            for(String temp : set) {
                Todo todo = gson.fromJson(temp, Todo.class);
                MAIN_ACTIVITY.getList().add(todo);
            }
        }
        Todo.NEXT = getSharedPreference().getLong(TODO_NO, 1);
    }

    public static void saveAddingTodo(String[] values) {
        SharedPreferences.Editor editor = getSharedPreference().edit();
        editor.putString(ADDING_TODO_NAME, values[0]);
        editor.putString(ADDING_TODO_CATEGORY, values[1]);
        editor.putString(ADDING_TODO_EXPLAIN, values[2]);
        editor.putString(ADDING_TODO_COMPLETE, values[3]);
        editor.putString(ADDING_TODO_END, values[4]);
        editor.apply();
    }

    public static String[] loadAddingTodo() {
        String[] load = {
                getSharedPreference().getString(ADDING_TODO_NAME, null),
                getSharedPreference().getString(ADDING_TODO_CATEGORY, null),
                getSharedPreference().getString(ADDING_TODO_EXPLAIN, null),
                getSharedPreference().getString(ADDING_TODO_COMPLETE, null),
                getSharedPreference().getString(ADDING_TODO_END, null)
        };
        SharedPreferences.Editor editor = getSharedPreference().edit();
        editor.remove(ADDING_TODO_NAME);
        editor.remove(ADDING_TODO_CATEGORY);
        editor.remove(ADDING_TODO_EXPLAIN);
        editor.remove(ADDING_TODO_COMPLETE);
        editor.remove(ADDING_TODO_END);
        editor.apply();
        return load;
    }

    public static void setDate(int year1, int month1, int day1) {
        year = year1;
        month = month1;
        day = day1;
    }

    public static boolean isSetDate() {
        boolean flag = true;
        if(year == 0 || day == 0) {
            flag = false;
        }
        return flag;
    }

    public static int[] getDate() {
        return new int[]{year, month, day};
    }

    public static String[] getCategories() {
        if(set == null) {
            loadCategories();
        }
        String[] strings = new String[set.size() + 1];
        int i = 0;
        strings[i++] = "카테고리를 선택해주세요";
        for(String temp : set) {
            strings[i++] = temp;
        }
        return strings;
    }

    public static String[] getCategoriesMain() {
        if(set == null) {
            loadCategories();
        }
        return set.toArray(new String[0]);
    }

    public static void saveCategories() {
        SharedPreferences.Editor editor = getSharedPreference().edit();
        HashSet<String> set = new HashSet<>(Arrays.asList(getCategoriesMain()));
        set.remove("카테고리를 선택해주세요");
        editor.putStringSet(CATEGORY, set);
        editor.putInt(CATEGORY_COUNT, set.size());
        editor.apply();
    }

    public static void loadCategories() {
        set = (HashSet<String>) getSharedPreference().getStringSet(CATEGORY, new HashSet<>());
    }

    public static void addCategory(String category) {
        if(set == null) {
            loadCategories();
        }
        set.add(category);
        saveCategories();
        loadCategories();
    }

    public static void removeCategory(String category) {
        List<Todo> list = MAIN_ACTIVITY.getList();
        for(Todo temp : list) {
            if(temp.getCategory().equals(category)) {
                list.remove(temp);
            }
        }
        set.remove(category);
        saveCategories();
        loadCategories();
    }
}

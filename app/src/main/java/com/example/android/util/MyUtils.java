package com.example.android.util;
import android.content.DialogInterface;
import android.os.Handler;
import android.util.Log;
import java.io.IOException;
import java.util.*;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.ContextThemeWrapper;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.android.R;
import com.example.android.activities.MainActivity;
import com.example.android.dto.Holiday;
import com.example.android.dto.Todo;
import com.example.android.httpService.CalendarificService;
import com.example.android.httpService.NaverMapService;
import com.google.gson.Gson;
import com.naver.maps.geometry.LatLng;
import okhttp3.ResponseBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.text.SimpleDateFormat;

public class MyUtils {

    private final static String TODO_LIST = "todoList";
    private final static String TODO_AMOUNT = "todoAmount";
    private final static String TODO_NO = "todoNo";

    private final static String ADDING_TODO_NAME = "addingTodoName";
    private final static String ADDING_TODO_CATEGORY = "addingTodoCategory";
    private final static String ADDING_TODO_EXPLAIN = "addingTodoExplain";
    private final static String ADDING_TODO_COMPLETE = "addingTodoComplete";
    private final static String ADDING_TODO_END = "addingTodoEnd";
    private final static String ADDING_TODO_LATITUDE = "addingTodoLatitude";
    private final static String ADDING_TODO_LONGITUDE = "addingTodoLongitude";
    private final static String ADDING_TODO_LOCATION = "addingTodoLocation";

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

    private static HashMap<Integer, List<Holiday>> map = new HashMap<>();
    public final static Handler HANDLER = new Handler();

    /*
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
        intent.putExtra("location", todo.getLocationName());
        intent.putExtra("latitude", todo.getLatitude());
        intent.putExtra("longitude", todo.getLongitude());
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
        editor.putString(ADDING_TODO_END, values[3]);
        editor.putString(ADDING_TODO_LOCATION, values[4]);
        editor.putString(ADDING_TODO_LATITUDE, values[5]);
        editor.putString(ADDING_TODO_LONGITUDE, values[6]);
        editor.apply();
    }

    public static String[] loadAddingTodo() {
        String[] load = {
                getSharedPreference().getString(ADDING_TODO_NAME, null),
                getSharedPreference().getString(ADDING_TODO_CATEGORY, null),
                getSharedPreference().getString(ADDING_TODO_EXPLAIN, null),
                getSharedPreference().getString(ADDING_TODO_END, null),
                getSharedPreference().getString(ADDING_TODO_LOCATION, null),
                getSharedPreference().getString(ADDING_TODO_LATITUDE, null),
                getSharedPreference().getString(ADDING_TODO_LONGITUDE, null)
        };
        SharedPreferences.Editor editor = getSharedPreference().edit();
        editor.remove(ADDING_TODO_NAME);
        editor.remove(ADDING_TODO_CATEGORY);
        editor.remove(ADDING_TODO_EXPLAIN);
        editor.remove(ADDING_TODO_END);
        editor.remove(ADDING_TODO_LOCATION);
        editor.remove(ADDING_TODO_LATITUDE);
        editor.remove(ADDING_TODO_LONGITUDE);
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
        // putStringSet을 하나만 put하면 어플 재시작 후 일부 또는 전체가 반영이 안되는 문제가 발생
        // 그래서 set.size()를 putInt로 줘서 저장하면 문제 해결, 다른 해결방법(아직 검증 안함) - https://pythonq.com/so/android/421097
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
        // 안드로이드에서 for-each로 list에 remove하면 ConcurrentModificationException발생
        // https://m.blog.naver.com/tmondev/220393974518
        List<Todo> list = MAIN_ACTIVITY.getList();
        for(int i = 0; i < list.size(); i++) {
            if(list.get(i).getCategory().equals(category)) {
                list.remove(list.get(i));
            }
        }
        saveList();
        set.remove(category);
        saveCategories();
        loadCategories();
    }

    // HolidayDecorator의 shouldDecorate()가 해당 메소드를 동시에 여러 번 호출하는 문제를 synchronized를 이용하여 해결
    public synchronized static List<Holiday> getHolidays(int year) {
        if(!map.containsKey(year)) {
            // Retrofit으로 http로 날려서 일정을 받아왔다. 받아온 일정을 저장하는 구조와 코드 작성하기
            Retrofit fit = new Retrofit.Builder()
                    .baseUrl(CalendarificService.baseURL)
                    .build();
            CalendarificService service = fit.create(CalendarificService.class);
            new Thread() {
                @Override
                public void run() {
                    try {
                        List<Holiday> list = new ArrayList<>();
                        String json = service.getHolidays(service.apiKey, "KR", year).execute().body().string();
                        JSONObject jsonObject = new JSONObject(json);
                        int code = jsonObject.getJSONObject("meta").getInt("code");
                        if(code == 200) {
                            JSONArray jsonArray = (JSONArray) ((JSONObject) jsonObject.get("response")).get("holidays");
                            for(int i = 0; i < jsonArray.length() && !jsonArray.isNull(i); i++) {
                                JSONObject holiday = jsonArray.getJSONObject(i);
                                String name = (String) holiday.get("name");
                                String description = (String) holiday.get("description");
                                String isoDate = holiday.getJSONObject("date").getString("iso");
                                isoDate = isoDate.replaceAll("-", "");
                                JSONArray typeJson = (JSONArray) holiday.get("type");
                                String[] type = new String[typeJson.length()];
                                for(int j = 0; j < typeJson.length(); j++) {
                                    type[j] = typeJson.getString(j);
                                }
                                Holiday day = new Holiday(name, description, isoDate, type);
                                list.add(day);
                            }
                        }
                        map.put(year, list);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
            int no = 0;
            boolean flag = true;
            while(flag) {
                Log.i("while", String.valueOf(no++));
                try {
                    Thread.sleep(100);
                    flag = map.get(year) == null;
                    if(no > 100) {
                        flag = false;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return map.get(year);
    }

    public static Intent getGeocode(String address) {
        Retrofit fit = new Retrofit.Builder()
                .baseUrl(NaverMapService.URL)
                .build();
        NaverMapService service = fit.create(NaverMapService.class);

        Intent intent = new Intent();

        new Thread() {
            @Override
            public void run() {
                try {
                    String json = service.getGeocode(address, 1).execute().body().string();
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    if(status.equals("OK")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("addresses");
                        JSONObject jsonAddress = jsonArray.getJSONObject(0);
                        String address = jsonAddress.getString("roadAddress");
                        String latitude = jsonAddress.getString("y");
                        String longitude = jsonAddress.getString("x");
                        intent.putExtra("address", address);
                        intent.putExtra("latitude", latitude);
                        intent.putExtra("longitude", longitude);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        boolean flag = true;
        int no = 0;
        while(flag) {
            if(intent.getStringExtra("address") != null) {
                if(intent.getStringExtra("latitude") != null) {
                    if(intent.getStringExtra("longitude") != null) {
                        flag = false;
                    }
                }
            }
            try {
                Thread.sleep(100);
                Log.i("getGeocode.while", String.valueOf(no++));
                if(no > 100) {
                    flag = false;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return intent;
    }

    public static Intent getReverseGeocode(LatLng latLng) {
        Intent intent = new Intent();

        Retrofit fit = new Retrofit.Builder()
                .baseUrl(NaverMapService.URL)
                .build();
        NaverMapService service = fit.create(NaverMapService.class);

        new Thread() {
            @Override
            public void run() {
                Response<ResponseBody> response = null;
                try {
                    response = service.getReverseGeocode(latLng.longitude + "," + latLng.latitude, "roadaddr,addr", "json").execute();
                    String json = response.body().string();
                    JSONObject jsonObject = new JSONObject(json);
                    // region, land, addition0~2
                    JSONArray results = jsonObject.getJSONArray("results");
                    String address = "";
                    JSONObject object = results.getJSONObject(0);
                    if(object.getString("name").equals("roadaddr")) {
                        JSONObject region = object.getJSONObject("region");
                        JSONObject land = object.getJSONObject("land");
                        address += region.getJSONObject("area1").getString("name") + " ";
                        address += region.getJSONObject("area2").getString("name") + " ";
                        address += land.getString("name") + " ";
                        address += land.getString("number1") + " ";
                        address += land.getJSONObject("addition0").getString("value");
                        intent.putExtra("address", address);
                    }
                    if(object.getString("name").equals("addr")) {
                        JSONObject region = object.getJSONObject("region");
                        JSONObject land = object.getJSONObject("land");
                        address += region.getJSONObject("area1").getString("name") + " ";
                        address += region.getJSONObject("area2").getString("name") + " ";
                        address += region.getJSONObject("area3").getString("name") + " ";
                        address += land.getString("number1");
                        address = address.trim();
                        intent.putExtra("address", address);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        boolean flag = true;
        int no = 0;
        while(flag) {
            if(intent.getStringExtra("address") != null) {
                flag = false;
            }
            try {
                Thread.sleep(100);
                Log.i("reverse geocode.while", String.valueOf(no++));
                if(no > 100) {
                    flag = false;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return intent;
    }
    public static void alertDialog(String str, AppCompatActivity activity) {
        AlertDialog dialog = new AlertDialog.Builder(new ContextThemeWrapper(activity, R.style.AlertDialog_AppCompat))
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                })
                .setTitle(str)
                .setCancelable(false)
                .create();
        dialog.show();
    }
}

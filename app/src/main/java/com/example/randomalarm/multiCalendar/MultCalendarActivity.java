package com.example.randomalarm.multiCalendar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.randomalarm.R;
import com.example.randomalarm.common.StringHelper;
import com.example.randomalarm.common.ViewerHelper;
import com.example.randomalarm.setting.AlarmCalendar;
import com.example.randomalarm.setting.AlarmSettingActivity;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MultCalendarActivity extends AppCompatActivity implements
        CalendarView.OnCalendarMultiSelectListener,
        CalendarView.OnCalendarInterceptListener,
        CalendarView.OnYearChangeListener,
        CalendarView.OnMonthChangeListener {

    public final static String ALARM_CALENDAR_DATA = "ALARM_CALENDAR_DATA";
    public static final int MULT_CALENDAR_CODE = 5;
    private final static int MAX_MULTI_SELECT_SIZE = 200;
    @BindView(R.id.tv_month_day)
    TextView mTextMonthDay;
    @BindView(R.id.tv_year)
    TextView mTextYear;
    @BindView(R.id.tv_lunar)
    TextView mTextLunar;
    @BindView(R.id.tv_current_day)
    TextView mTextCurrentDay;
    @BindView(R.id.calendarView)
    CalendarView mCalendarView;
    @BindView(R.id.calendarLayout)
    CalendarLayout mCalendarLayout;
    @BindView(R.id.tv_clear)
    TextView ivClear;
    @BindView(R.id.fl_current)
    FrameLayout flCurrent;
    Map <String, Calendar> schemeDate = new HashMap <>();
    int selectColor = R.color.colorAccent;
    Calendar nowDate;
    ArrayList <AlarmCalendar> alarmCalendars;
    private int showYear;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mult_calendar);
        ViewerHelper.displayHomeAsUp(getSupportActionBar());
        ButterKnife.bind(this);

        Intent intent = getIntent();
        alarmCalendars = intent.getParcelableArrayListExtra(ALARM_CALENDAR_DATA);
        initView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            this.finish();
            return true;
        } else if (itemId == R.id.action_save) {
            save();
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("SetTextI18n")
    protected void initView() {
        mTextMonthDay.setOnClickListener(v -> {
            if (!mCalendarLayout.isExpand()) {
                mCalendarLayout.expand();
                return;
            }
            mCalendarView.showYearSelectLayout(showYear);
            mTextLunar.setVisibility(View.GONE);
            mTextYear.setVisibility(View.GONE);
            mTextMonthDay.setText(String.valueOf(showYear));
        });
        flCurrent.setOnClickListener(v -> mCalendarView.scrollToCurrent());
        mCalendarView.setOnCalendarMultiSelectListener(this);
        mCalendarView.setOnYearChangeListener(this);
        //设置日期拦截事件，当前有效
        mCalendarView.setOnCalendarInterceptListener(this);
        mCalendarView.setOnMonthChangeListener(this);
        mCalendarView.setMaxMultiSelectSize(MAX_MULTI_SELECT_SIZE);
        showYear = mCalendarView.getCurYear();
        intiText();
        clear();
        addMultiSelect();
        getNow();
    }

    private void intiText() {
        int curDay = mCalendarView.getCurDay();
        mTextYear.setText(String.valueOf(mCalendarView.getCurYear()));
        mTextMonthDay.setText(StringHelper.getLocalFormat("%d月%d日", mCalendarView.getCurMonth(), curDay));
        mTextLunar.setText("今日");
        mTextCurrentDay.setText(String.valueOf(curDay));
    }

    private void clear() {
        ivClear.setOnClickListener(v ->
        {
            mCalendarView.clearMultiSelect();
            mCalendarView.clearSchemeDate();
            schemeDate.clear();
        });
    }

    public void addMultiSelect() {
        for (AlarmCalendar alarmCalendar : alarmCalendars) {
            Calendar schemeCalendar = alarmCalendar.toCalendar();
            schemeCalendar.setSchemeColor(selectColor);//如果单独标记颜色、则会使用这个颜色
            schemeCalendar.setScheme("选");
            schemeCalendar.addScheme(new Calendar.Scheme());
//            Calendar schemeCalendar = getSchemeCalendar(2019, 5, 18, selectColor, "选");
            schemeDate.put(schemeCalendar.toString(), schemeCalendar);
            mCalendarView.putMultiSelect(schemeCalendar);
        }
        mCalendarView.setSchemeDate(schemeDate);
    }

    private Calendar getSchemeCalendar(int year, int month, int day, int color, String text) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(color);//如果单独标记颜色、则会使用这个颜色
        calendar.setScheme(text);
        calendar.addScheme(new Calendar.Scheme());
        return calendar;
    }

    private void save() {
        ArrayList <AlarmCalendar> selectAlarmCalendar = new ArrayList <>();
        for (Calendar calendar : schemeDate.values()) {
            selectAlarmCalendar.add(AlarmCalendar.calendarToThis(calendar));
        }
        Intent intent = getIntent();
        intent.putParcelableArrayListExtra(ALARM_CALENDAR_DATA, selectAlarmCalendar);
        setResult(MULT_CALENDAR_CODE, intent);
    }
    @Override
    public void onCalendarMultiSelectOutOfRange(Calendar calendar) {
        Log.e("OutOfRange", "OutOfRange" + calendar);
    }

    @Override
    public void onMultiSelectOutOfSize(Calendar calendar, int maxSize) {
        Toast.makeText(this, "超过最大选择数量 ：" + maxSize, Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onCalendarMultiSelect(Calendar calendar, int curSize, int maxSize) {
        mTextLunar.setVisibility(View.VISIBLE);
        mTextYear.setVisibility(View.VISIBLE);
        showYear = calendar.getYear();
        int month = calendar.getMonth();
        int day = calendar.getDay();
        mTextMonthDay.setText(String.format("%d月%d日", month, day));
        mTextYear.setText(String.valueOf(calendar.getYear()));
        mTextLunar.setText(calendar.getLunar());
        Calendar schemeCalendar = getSchemeCalendar(showYear, month, day, selectColor, "选");
        String schemeStr = schemeCalendar.toString();
        if (schemeDate.containsKey(schemeStr)) {
            schemeDate.remove(schemeStr);
        } else {
            schemeDate.put(schemeStr, schemeCalendar);
        }
        mCalendarView.setSchemeDate(schemeDate);
        Log.e("onDateMultSelected", "  -- " + calendar.getYear() +
                "  --  " + month +
                "  -- " + day +
                "  --  " + "  --   " + calendar.getScheme());
    }

    @Override
    public void onYearChange(int year) {
        mTextMonthDay.setText(String.valueOf(year));
    }

    @Override
    public void onMonthChange(int year, int month) {
        Log.e("onMonthChange", "  -- " + year + "  --  " + month);
    }

    /**
     * 屏蔽某些不可点击的日期，可根据自己的业务自行修改
     * 如 calendar > 2018年1月1日 && calendar <= 2020年12月31日
     *
     * @param calendar calendar
     * @return 是否屏蔽某些不可点击的日期，MonthView和WeekView有类似的API可调用
     */
    @Override
    public boolean onCalendarIntercept(Calendar calendar) {
        return calendar.compareTo(nowDate) < 0;
    }

    private void getNow() {
        nowDate = new Calendar();
        nowDate.setYear(mCalendarView.getCurYear());
        nowDate.setMonth(mCalendarView.getCurMonth());
        nowDate.setDay(mCalendarView.getCurDay());
    }

    @Override
    public void onCalendarInterceptClick(Calendar calendar, boolean isClick) {
        Toast.makeText(this,
                calendar.toString() + (isClick ? "拦截不可点击" : "拦截设定为无效日期"),
                Toast.LENGTH_SHORT).show();
    }

}

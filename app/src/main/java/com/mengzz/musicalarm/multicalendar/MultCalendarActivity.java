package com.mengzz.musicalarm.multicalendar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;
import com.mengzz.musicalarm.R;
import com.mzz.zandroidcommon.view.BaseActivity;
import com.mzz.zandroidcommon.common.StringHelper;
import com.mengzz.musicalarm.setting.AlarmCalendar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MultCalendarActivity extends BaseActivity implements
        CalendarView.OnCalendarMultiSelectListener,
        CalendarView.OnCalendarInterceptListener,
        CalendarView.OnYearChangeListener,
        CalendarView.OnMonthChangeListener {

    public static final String ALARM_CALENDAR_DATA = "ALARM_CALENDAR_DATA";
    public static final int MULT_CALENDAR_CODE = 5;
    private static final int MAX_MULTI_SELECT_SIZE = 200;
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

    public static void startForResult(FragmentActivity activity, ArrayList <?
            extends Parcelable> value) {
        Intent intent =
                new Intent(activity, MultCalendarActivity.class).putParcelableArrayListExtra(ALARM_CALENDAR_DATA, value);
        activity.startActivityForResult(intent, MULT_CALENDAR_CODE);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mult_calendar);
        ButterKnife.bind(this);

        mCalendarView.setMonthView(CustomMultiMonthView.class);
        mCalendarView.setWeekView(CustomMultiWeekView.class);
        alarmCalendars = getParcelableArrayListExtra(ALARM_CALENDAR_DATA);
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
        if (itemId == R.id.action_save) {
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
        initMultiSelect();
        initNow();
    }

    private void intiText() {
        mTextYear.setText(String.valueOf(showYear));
        int curDay = mCalendarView.getCurDay();
        mTextMonthDay.setText(StringHelper.getLocalFormat("%d月%d日", mCalendarView.getCurMonth(),
                curDay));
        mTextLunar.setText("今日");
        mTextCurrentDay.setText(String.valueOf(curDay));
    }

    private void initNow() {
        nowDate = new Calendar();
        nowDate.setYear(mCalendarView.getCurYear());
        nowDate.setMonth(mCalendarView.getCurMonth());
        nowDate.setDay(mCalendarView.getCurDay());
    }

    @OnClick(R.id.tv_clear)
    void clearOnClick() {
        mCalendarView.clearMultiSelect();
        mCalendarView.clearSchemeDate();
        schemeDate.clear();
    }

    public void initMultiSelect() {
        for (AlarmCalendar alarmCalendar : alarmCalendars) {
            Calendar schemeCalendar = getSchemeCalendar(alarmCalendar.toMultCalendar(),
                    selectColor);
            schemeDate.put(schemeCalendar.toString(), schemeCalendar);
            mCalendarView.putMultiSelect(schemeCalendar);
        }
        mCalendarView.setSchemeDate(schemeDate);
    }

    private Calendar getSchemeCalendar(Calendar calendar, int color) {
        calendar.setSchemeColor(color);
        calendar.setScheme("选");
        calendar.addScheme(new Calendar.Scheme());
        return calendar;
    }

    private void save() {
        ArrayList <AlarmCalendar> selectAlarmCalendar = new ArrayList <>();
        for (Calendar calendar : schemeDate.values()) {
            selectAlarmCalendar.add(AlarmCalendar.calendarToThis(calendar));
        }
        Intent intent = getIntent().putParcelableArrayListExtra(ALARM_CALENDAR_DATA,
                selectAlarmCalendar);
        setResult(MULT_CALENDAR_CODE, intent);
    }

    @Override
    public void onCalendarMultiSelectOutOfRange(Calendar calendar) {
        showToast(StringHelper.getLocalFormat("多选超出范围"));
    }

    @Override
    public void onMultiSelectOutOfSize(Calendar calendar, int maxSize) {
        showToast(StringHelper.getLocalFormat("超过最大可选数目：%d", maxSize));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onCalendarMultiSelect(Calendar calendar, int curSize, int maxSize) {
        mTextLunar.setVisibility(View.VISIBLE);
        mTextYear.setVisibility(View.VISIBLE);
        showYear = calendar.getYear();
        mTextMonthDay.setText(StringHelper.getLocalFormat("%d月%d日", calendar.getMonth(),
                calendar.getDay()));
        mTextYear.setText(String.valueOf(calendar.getYear()));
        mTextLunar.setText(calendar.getLunar());
        Calendar schemeCalendar = getSchemeCalendar(calendar, selectColor);
        String schemeStr = schemeCalendar.toString();
        if (schemeDate.containsKey(schemeStr)) {
            schemeDate.remove(schemeStr);
        } else {
            schemeDate.put(schemeStr, schemeCalendar);
        }
        mCalendarView.setSchemeDate(schemeDate);
    }

    @Override
    public void onYearChange(int year) {
        mTextMonthDay.setText(String.valueOf(year));
    }

    @Override
    public void onMonthChange(int year, int month) {
        mTextMonthDay.setText(StringHelper.getLocalFormat("%d年%d月", year, month));
    }

    /**
     * 屏蔽无效日期
     *
     * @param calendar calendar
     * @return 是否屏蔽某些不可点击的日期，MonthView和WeekView有类似的API可调用
     */
    @Override
    public boolean onCalendarIntercept(Calendar calendar) {
        return calendar.compareTo(nowDate) < 0;
    }

    @Override
    public void onCalendarInterceptClick(Calendar calendar, boolean isClick) {
        showToast("无效日期");
    }

}

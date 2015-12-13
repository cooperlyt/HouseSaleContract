package com.dgsoft.cms.action;

import com.dgsoft.cms.model.Article;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by cooper on 12/11/15.
 */
@Name("eventsGroup")
public class EventsGroup {

    public class Event{

        private int year;

        private int month;

        private int date;

        private boolean current;

        private List<Article> events = new ArrayList<Article>();

        public Event(int year, int month, int date,boolean current) {
            this.year = year;
            this.month = month;
            this.date = date;
            this.current = current;
        }

        public Date getTime(){
            Calendar cd = Calendar.getInstance();
            cd.set(year,month - 1,date);
            return cd.getTime();
        }

        public boolean isEmptyEvent(){
            return events.isEmpty();
        }

        public void addEvent(Article article){
            events.add(article);
        }

        public int getYear() {
            return year;
        }

        public int getMonth() {
            return month;
        }

        public int getDate() {
            return date;
        }

        public List<Article> getEvents() {
            return events;
        }

        public boolean isCurrent() {
            return current;
        }
    }

    private int month;

    private int year;

    private int dayOfMonth;

    private Calendar getBeginDayOfWeek(){
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(year,month-1,dayOfMonth,0,0,0);
        int dayOfWeek =beginTime.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == 1){
            beginTime.add(Calendar.DAY_OF_MONTH,-6);
        }else if (dayOfWeek != 2){
            beginTime.add(Calendar.DAY_OF_MONTH, ((dayOfWeek -1 - 1)) * -1);
        }
        return beginTime;
    }

    private Calendar getEndDayOfWeek(){
        Calendar endTime = Calendar.getInstance();
        endTime.setTime(getBeginDayOfWeek().getTime());
        endTime.add(Calendar.DAY_OF_MONTH,6);

        endTime.set(Calendar.HOUR,13);
        endTime.set(Calendar.MINUTE,59);
        endTime.set(Calendar.SECOND,59);
        endTime.set(Calendar.MILLISECOND,999);
        return endTime;
    }

    public Date getBeginDateOfWeek(){
        return getBeginDayOfWeek().getTime();
    }

    public Date getEndDateOfWeek(){
        return getEndDayOfWeek().getTime();
    }

    public void nextMonth(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(year,month-1,1);
        calendar.add(Calendar.MONTH,1);
        setYear(calendar.get(Calendar.YEAR));
        setMonth(calendar.get(Calendar.MONTH) + 1);
    }

    public void previousMonth(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(year,month-1,1);
        calendar.add(Calendar.MONTH,-1);
        setYear(calendar.get(Calendar.YEAR));
        setMonth(calendar.get(Calendar.MONTH) + 1);
    }

    public void nextWeek(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(year,month - 1, dayOfMonth);
        calendar.add(Calendar.DAY_OF_MONTH,7);
        setYear(calendar.get(Calendar.YEAR));
        setMonth(calendar.get(Calendar.MONTH) + 1);
        setDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
    }

    public void previousWeek(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(year,month - 1, dayOfMonth);
        calendar.add(Calendar.DAY_OF_MONTH,-7);
        setYear(calendar.get(Calendar.YEAR));
        setMonth(calendar.get(Calendar.MONTH) + 1);
        setDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
    }

    public void nextDay(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(year,month - 1, dayOfMonth);
        calendar.add(Calendar.DAY_OF_MONTH,1);
        setYear(calendar.get(Calendar.YEAR));
        setMonth(calendar.get(Calendar.MONTH) + 1);
        setDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
    }

    public void previousDay(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(year,month - 1, dayOfMonth);
        calendar.add(Calendar.DAY_OF_MONTH,-1);
        setYear(calendar.get(Calendar.YEAR));
        setMonth(calendar.get(Calendar.MONTH) + 1);
        setDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
    }


    public List<Integer> getSelectYear(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        List<Integer> result = new ArrayList<Integer>(10);
        for(int i = (calendar.get(Calendar.YEAR) - 4); i <= (calendar.get(Calendar.YEAR) + 4); i++ ){
            result.add(i);
        }
        return result;
    }

    @In(create = true)
    private EntityManager entityManager;

    @Create
    public void init(){
        Calendar cd = Calendar.getInstance();
        cd.setTime(new Date());
        year = cd.get(Calendar.YEAR);
        month = cd.get(Calendar.MONTH) + 1 ;
        dayOfMonth = cd.get(Calendar.DAY_OF_MONTH);
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        if (month != this.month){
            resultList = null;
            weekResultList = null;
            dayResultList = null;
        }
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        if (year != this.year){
            resultList = null;
            weekResultList = null;
            dayResultList = null;
        }
        this.year = year;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(int dayOfMonth) {
        if (dayOfMonth != this.dayOfMonth){
            resultList = null;
            weekResultList = null;
            dayResultList = null;
        }
        this.dayOfMonth = dayOfMonth;
    }

    private List<Event> weekResultList;

    public List<Event> getWeekResult(){
        if(weekResultList == null){
            Calendar beginTime = getBeginDayOfWeek();

            Calendar endTime = getEndDayOfWeek();



            List<Article> eventList = entityManager.createQuery("select a from Article a where a.category.type = 'Events' and a.publishTime >= :beginTime and a.publishTime <= :toTime order by publishTime,id", Article.class)
                    .setParameter("beginTime",beginTime.getTime())
                    .setParameter("toTime", endTime.getTime()).getResultList();

            weekResultList = new ArrayList<Event>(7);

            while (beginTime.before(endTime)){
                Event dayEvent = new Event(beginTime.get(Calendar.YEAR),beginTime.get(Calendar.MONTH) + 1,beginTime.get(Calendar.DAY_OF_MONTH),
                        (beginTime.get(Calendar.YEAR) == year && (beginTime.get(Calendar.MONTH) + 1) == month));

                for(Article article: eventList){
                    Calendar c = Calendar.getInstance();
                    c.setTime(article.getPublishTime());
                    if (c.get(Calendar.YEAR) == beginTime.get(Calendar.YEAR) &&
                            c.get(Calendar.MONTH) == beginTime.get(Calendar.MONTH) &&
                            c.get(Calendar.DAY_OF_MONTH) == beginTime.get(Calendar.DAY_OF_MONTH)){
                        dayEvent.addEvent(article);
                    }else{
                        eventList.removeAll(dayEvent.getEvents());
                        break;
                    }
                }
                weekResultList.add(dayEvent);
                beginTime.add(Calendar.DAY_OF_MONTH,1);
            }

        }
        return weekResultList;
    }

    private List<Article> dayResultList;

    public List<Article> getDayResultList() {
        if (dayResultList == null){
            dayResultList = entityManager.createQuery("select a from Article a where a.category.type = 'Events' and year(a.publishTime) = :year and month(a.publishTime) = :month and day(a.publishTime) = :day order by publishTime,id", Article.class)
                    .setParameter("year",year)
                    .setParameter("month", month)
                    .setParameter("day",dayOfMonth).getResultList();
        }
        return dayResultList;
    }

    private List<List<Event>> resultList;

    //month
    public List<List<Event>> getResultList() {
        if (resultList == null){

            Calendar beginTime = Calendar.getInstance();

            beginTime.set(year,month-1,1,0,0,0);
            int beforMonthDays = beginTime.get(Calendar.DAY_OF_WEEK) - 1;
            if(beginTime.get(Calendar.DAY_OF_WEEK)  == 1){
                beforMonthDays = 6;
            }else{
                beforMonthDays = beforMonthDays - 1;
            }


            if (beforMonthDays > 0) {

                beginTime.add(Calendar.DAY_OF_MONTH, beforMonthDays * -1);
            }

            Calendar endTime = Calendar.getInstance();
            endTime.set(year,month-1,1,0,0,0);

            endTime.add(Calendar.MONTH,1);
            endTime.add(Calendar.DAY_OF_MONTH, -1);



            int afterMonthDays = endTime.get(Calendar.DAY_OF_WEEK) - 1;
            if (endTime.get(Calendar.DAY_OF_WEEK)  != 1){
                endTime.add(Calendar.DAY_OF_MONTH, 7 - afterMonthDays);
            }

            endTime.set(Calendar.HOUR,13);
            endTime.set(Calendar.MINUTE,59);
            endTime.set(Calendar.SECOND,59);
            endTime.set(Calendar.MILLISECOND,999);



            List<Article> eventList = entityManager.createQuery("select a from Article a where a.category.type = 'Events' and a.publishTime >= :beginTime and a.publishTime <= :toTime order by publishTime,id", Article.class)
                    .setParameter("beginTime",beginTime.getTime())
                    .setParameter("toTime", endTime.getTime()).getResultList();

            resultList = new ArrayList<List<Event>>();
            List<Event> curWeedEvent = new ArrayList<Event>(7);


            while (beginTime.before(endTime)){

                Event dayEvent = new Event(beginTime.get(Calendar.YEAR),beginTime.get(Calendar.MONTH) + 1,beginTime.get(Calendar.DAY_OF_MONTH),
                        (beginTime.get(Calendar.YEAR) == year && (beginTime.get(Calendar.MONTH) + 1) == month));

                for(Article article: eventList){
                    Calendar c = Calendar.getInstance();
                    c.setTime(article.getPublishTime());
                    if (c.get(Calendar.YEAR) == beginTime.get(Calendar.YEAR) &&
                            c.get(Calendar.MONTH) == beginTime.get(Calendar.MONTH) &&
                            c.get(Calendar.DAY_OF_MONTH) == beginTime.get(Calendar.DAY_OF_MONTH)){
                        dayEvent.addEvent(article);
                    }else{
                        eventList.removeAll(dayEvent.getEvents());
                        break;
                    }
                }

                curWeedEvent.add(dayEvent);

                if (curWeedEvent.size() == 7){
                    resultList.add(curWeedEvent);
                    curWeedEvent = new ArrayList<Event>(7);
                }

                beginTime.add(Calendar.DAY_OF_MONTH,1);
            }

        }
        return resultList;
    }
}

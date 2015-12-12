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
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        if (month != this.month){
            resultList = null;
        }
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        if (year != this.year){
            resultList = null;
        }
        this.year = year;
    }

    private List<List<Event>> resultList;

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

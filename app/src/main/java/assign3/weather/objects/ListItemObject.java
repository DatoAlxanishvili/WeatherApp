package assign3.weather.objects;

/**
 * Created by dalkh on 05-Dec-15.
 */
public class ListItemObject {
    private String date, weekDay, icon, tempMax, tempMin;

    public ListItemObject(String date,String weekDay, String icon, String tempMax, String tempMin) {
        this.date = date;
        this.weekDay=weekDay;
        this.icon = icon;
        this.tempMax = tempMax;
        this.tempMin = tempMin;
    }

    public String getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(String weekDay) {
        this.weekDay = weekDay;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTempMax() {
        return tempMax;
    }

    public void setTempMax(String tempMax) {
        this.tempMax = tempMax;
    }

    public String getTempMin() {
        return tempMin;
    }

    public void setTempMin(String tempMin) {
        this.tempMin = tempMin;
    }
}
